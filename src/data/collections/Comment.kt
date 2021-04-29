package com.project.data.collections

data class Comment(
    val _id : String,
    val postingId : String,
    val idUser : String,
    val commentText : String,
    val date : Long
)