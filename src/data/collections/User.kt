package com.project.data.collections

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class User(
    val email : String = "",
    val password : String,
    val username : String = "",
    val followers : List<String> = listOf(),
    @BsonId
    val _id : String = ObjectId().toString()
)