package com.project.data.requests

data class PostRequest(
    val id : String,
    val idUser : String,
    val date : Long,
    val text : String,
    val type : String
)