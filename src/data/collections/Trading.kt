package com.project.data.collections



data class Trading(
    val _id : String="",
    val email:String,
    val date : Long,
    val text : String,
    val description : String,
    val selling : String,
    val buying : String,
    val amountSelling : String,
    val amountBuying : String,
    val commentList : List<String> = listOf("="),
    val memberCommentList : List<String> = listOf(email),
    val likedBy: List<String> = listOf(),
    val dislikedBy: List<String> = listOf()
)