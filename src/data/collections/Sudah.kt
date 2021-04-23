package com.project.data.collections

data class Sudah(
    val _id: String,
    val name : String,
    val duration : String,
    val group : String = "",
    val status : String = "-",
    val likeList : List<String> = listOf(),
    val unLikeList : List<String> = listOf()
)