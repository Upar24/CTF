package com.project.data.collections

data class Pesta(
    val _id: String,
    val name : String,
    val duration : String,
    val group : String = "I",
    val status : String = "-",
    val likeList : List<String> = listOf(),
    val unLikeList : List<String> = listOf()
)