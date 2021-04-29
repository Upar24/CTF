package com.project.data.collections



data class Post(
    val _id : String="",
    val email:String,
    val date : Long,
    val text : String,
    val commentList : List<String> = listOf("="),
    val memberCommentList : List<String> = listOf(email),
    val likedBy: List<String> = listOf(),
    val dislikedBy: List<String> = listOf()
)