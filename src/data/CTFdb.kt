package com.project.data

import com.project.data.collections.*
import org.litote.kmongo.*
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

private val client = KMongo.createClient().coroutine
private val db = client.getDatabase("CTFDb")
private val users = db.getCollection<User>("user")
private val pestas = db.getCollection<Pesta>("pesta")
private val sudahs = db.getCollection<Sudah>("sudah")
private val posts = db.getCollection<Post>("post")
private val tradings = db.getCollection<Trading>("trading")
private val commentPosts = db.getCollection<CommentPost>("commentPost")

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
    return tradings.deleteOneById(trading._id).wasAcknowledged()
}
suspend fun isMemberCommentOfPost(idPost:String,email:String):Boolean{
     val post = posts.findOneById(idPost) ?: return false
    return email in post.memberCommentList
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













