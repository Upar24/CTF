package com.project.data

import com.project.data.collections.Pesta
import com.project.data.collections.Post
import com.project.data.collections.Sudah
import com.project.data.collections.User
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo
import org.litote.kmongo.setValue

private val client = KMongo.createClient().coroutine
private val db = client.getDatabase("CTFDb")
private val users = db.getCollection<User>("user")
private val pestas = db.getCollection<Pesta>("pesta")
private val sudahs = db.getCollection<Sudah>("sudah")
private val posts = db.getCollection<Post>("post")

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
    return email in user.followers
}
suspend fun toggleFollowUser(idUser: String, email: String) : String {
    val isFollowing = isFollowingUser(idUser, email)
    if (isFollowing) {
        val newFollowers = users.findOne(User::email eq idUser)!!.followers - email
        users.updateOne(User::email eq idUser, setValue(User::followers, newFollowers))
        return "unfollowing"
    } else {
        val newFollowers = users.findOne(User::email eq idUser)!!.followers + email
        users.updateOne(User::email eq idUser, setValue(User::followers, newFollowers))
        return "following"
    }
}
suspend fun savePesta(pesta: Pesta):Boolean{
    val pestaExist = pestas.findOneById(pesta._id) != null
    return if(pestaExist){
        pestas.updateOneById(pesta._id,pesta).wasAcknowledged()
    }else{
        pestas.insertOne(pesta).wasAcknowledged()
    }
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
















