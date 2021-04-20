package com.project.data.collections

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class User(
    val email : String,
    val password : String,
    val username : String,
    val followers : List<String>,
    @BsonId
    val idUser : String = ObjectId().toString()
)