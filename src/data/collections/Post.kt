package com.project.data.collections



data class Post(
    val _id : String="",
    val email:String,
    val date : Long,
    val text : String,
    val type : String,
    val likedBy: List<String> = listOf(),
    val dislikedBy: List<String> = listOf()
)