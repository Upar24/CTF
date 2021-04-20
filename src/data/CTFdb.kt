package com.project.data

import com.project.data.collections.User
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo

private val client = KMongo.createClient().coroutine
private val db = client.getDatabase("CTFDb")
private val users = db.getCollection<User>()

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