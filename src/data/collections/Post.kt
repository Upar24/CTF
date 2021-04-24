package com.project.data.collections

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Post(
    @BsonId
    val _id : String = ObjectId().toString(),
    val idUser:String,
    val date : Long,
    val text : String,
    val type : String,
    val likedBy: List<String> = listOf(),
    val dislikedBy: List<String> = listOf()
)