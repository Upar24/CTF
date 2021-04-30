package com.project.data.collections

data class CommentPost(
    val _id : String,
    val postId : String,
    val idUser : String,
    val commentText : String,
    val date : Long
)