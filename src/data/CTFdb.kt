package com.project.data

import com.project.data.collections.*
import org.litote.kmongo.*
import org.litote.kmongo.coroutine.CoroutineFindPublisher
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import kotlin.reflect.KProperty0

private val client = KMongo.createClient().coroutine
private val db = client.getDatabase("CTFDb")
private val users = db.getCollection<User>("user")
private val pestas = db.getCollection<Pesta>("pesta")
private val sudahs = db.getCollection<Sudah>("sudah")
private val posts = db.getCollection<Post>("post")
private val tradings = db.getCollection<Trading>("trading")
private val commentPosts = db.getCollection<CommentPost>("commentPost")
private val commentTradings = db.getCollection<CommentTrading>("commentTrading")

suspend fun registerUser(user: User) : Boolean{
    return users.insertOne(user).wasAcknowledged()
}
suspend fun checkIfUserExist(email:String):Boolean{
    return users.findOne(User::email eq email) != null
}
suspend fun checkPasswordForEmail(email:String, passwordToCheck:String):Boolean{
    val actualPassword = users.findOne(User::email eq email)?.password ?: return false
    return actualPassword==passwordToCheck
}
suspend fun isFollowingUser(idUser:String, email:String): Boolean{
    val user = users.findOne(User::email eq idUser) ?: return false
    return email in user.followings
}
suspend fun toggleFollowUser(idUser: String, email: String) : String {
    val isFollowing = isFollowingUser(idUser, email)
    if (isFollowing) {
        val newFollowings = users.findOne(User::email eq idUser)!!.followings - email
        users.updateOne(User::email eq idUser, setValue(User::followings, newFollowings))
        return "unfollowing"
    } else {
        val newFollowoings = users.findOne(User::email eq idUser)!!.followings + email
        users.updateOne(User::email eq idUser, setValue(User::followings, newFollowoings))
        return "following"
    }
}
suspend fun savePesta(pesta: Pesta):Boolean{
//    val pestaExist = pestas.findOneById(pesta._id) != null
//    return if(pestaExist){
//        pestas.updateOneById(pesta._id,pesta).wasAcknowledged()
//    }else{
        return pestas.insertOne(pesta).wasAcknowledged()
//    }
}
suspend fun getPesta(group:String):List<Pesta>{
    return pestas.find(Pesta::group eq group).toList()
}
suspend fun getPot(status : String): Pesta {
    return pestas.findOne(Pesta::status eq status)!!
}
suspend fun getSudahs(status: String): List<Sudah> {
    return sudahs.find(Sudah::status eq status).toList()
}
suspend fun savePost(post: Post) : Boolean{
        return posts.insertOne(post).wasAcknowledged()
}
suspend fun getPostForUser(email : String): List<Post>{
    return posts.find(Post::email eq email).toList()
}
suspend fun getPostFollowing(email: String): List<Post>? {
    val followings = users.findOne(User::email eq email)?.followings ?: return null
    return posts.find(Post::email `in` followings).toList()
}
suspend fun deletePost(idPost : String):Boolean{
    val post = posts.findOne(Post::_id eq idPost) ?: return false
    deleteAllCommentCosPostDeleted(post._id)
    return posts.deleteOneById(post._id).wasAcknowledged()
}
suspend fun saveTrading(trading: Trading):Boolean{
    return tradings.insertOne(trading).wasAcknowledged()
}
suspend fun getTrading(query : String):List<Trading>{
    return tradings.find(or(Trading::selling eq query,Trading::buying eq query)).toList()
}
suspend fun getBuying(query: String):List<Trading>{
    return tradings.find(Trading::buying eq query).toList()
}
suspend fun getSelling(query: String):List<Trading>{
    return tradings.find(Trading::selling eq query).sort(descending(Trading::date)).toList()
}
suspend fun deleteTrading(idTrading : String):Boolean{
    val trading = tradings.findOne(Trading::_id eq idTrading) ?: return false
    deleteAllCommentCosTradingDeleted(trading._id)
    return tradings.deleteOneById(trading._id).wasAcknowledged()
}
suspend fun isMemberCommentOfPost(idPost:String,email:String):Boolean{
     val post = posts.findOneById(idPost) ?: return false
    return email in post.memberCommentList
}
suspend fun isMemberCommentOfTrading(idTrading:String,email:String):Boolean{
    val trading = tradings.findOneById(idTrading) ?: return false
    return email in trading.memberCommentList
}
suspend fun outFromMemberCommentPost(idPost: String,email: String):Boolean{
    return if(isMemberCommentOfPost(idPost,email)){
        val post = posts.findOneById(idPost) ?: return false
        val newMember = post.memberCommentList - email
        return posts.updateOne(Post::_id eq post._id, setValue(Post::memberCommentList,newMember)).wasAcknowledged()
        } else false
}
suspend fun outFromMemberCommentTrading(idTrading: String,email: String):Boolean{
    return if(isMemberCommentOfTrading(idTrading,email)){
        val trading = tradings.findOneById(idTrading) ?: return false
        val newMember = trading.memberCommentList - email
        return tradings.updateOne(Trading::_id eq trading._id, setValue(Trading::memberCommentList,newMember)).wasAcknowledged()
    } else false
}
suspend fun saveCommentPost(commentPost: CommentPost) : Boolean{
    val idComment = commentPost._id
    val idPost = commentPost.postId
    val email = commentPost.idUser
    commentPosts.insertOne(commentPost).wasAcknowledged()
    if (!isMemberCommentOfPost(idPost, email)) {
        val member = posts.findOneById(idPost)?.memberCommentList ?: return false
        posts.updateOneById(idPost, setValue(Post::memberCommentList, member + email)).wasAcknowledged()
        }
    val newComment = posts.findOne(Post::_id eq idPost)?.commentList ?: return false
    return posts.updateOneById(idPost, setValue(Post::commentList, newComment + idComment)).wasAcknowledged()
}
suspend fun saveCommentTrading(commentTrading: CommentTrading) : Boolean{
    val idComment = commentTrading._id
    val idTrading = commentTrading.tradingId
    val email = commentTrading.idUser
    commentTradings.insertOne(commentTrading).wasAcknowledged()
    if (!isMemberCommentOfTrading(idTrading, email)) {
        val member = tradings.findOneById(idTrading)?.memberCommentList ?: return false
        tradings.updateOneById(idTrading, setValue(Trading::memberCommentList, member + email)).wasAcknowledged()
    }
    val newComment = tradings.findOne(Trading::_id eq idTrading)?.commentList ?: return false
    return tradings.updateOneById(idTrading, setValue(Trading::commentList, newComment + idComment)).wasAcknowledged()
}
suspend fun getCommentPost(idPost: String):List<CommentPost>{
    return commentPosts.find(CommentPost::postId eq idPost).toList()
}
suspend fun getCommentTrading(idTrading:String):List<CommentTrading>{
    return commentTradings.find(CommentTrading::tradingId eq idTrading).toList()
}
suspend fun deleteCommentPost(idComment:String):Boolean{
    val comment = commentPosts.findOne(CommentPost::_id eq idComment) ?: return false
    val idPost = comment.postId
    val commentListPost = posts.findOne(Post::_id eq idPost)?.commentList!!
    val newCommentList = commentListPost - idComment
    posts.updateOne(Post::_id eq idPost, setValue(Post::commentList,newCommentList))
    return commentPosts.deleteOneById(comment._id).wasAcknowledged()
}
suspend fun deleteCommentTrading(idComment : String):Boolean{
    val comment = commentTradings.findOne(CommentTrading::_id eq idComment) ?: return false
    val idTrading = comment.tradingId
    val commentListPost = tradings.findOne(Trading::_id eq idTrading)?.commentList!!
    val newCommentList = commentListPost - idComment
    tradings.updateOne(Trading::_id eq idTrading, setValue(Trading::commentList,newCommentList))
   return commentTradings.deleteOneById(comment._id).wasAcknowledged()
}
suspend fun deleteAllCommentCosPostDeleted(idPost:String):Boolean{
    return commentPosts.deleteMany(CommentPost::postId eq idPost).wasAcknowledged()
}
suspend fun deleteAllCommentCosTradingDeleted(idTrading: String):Boolean{
    return commentTradings.deleteMany(CommentTrading::tradingId eq idTrading).wasAcknowledged()
}












