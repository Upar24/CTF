package com.project.routes

import com.project.data.collections.CommentPost
import com.project.data.collections.Post
import com.project.data.reponses.SimpleResponse
import com.project.data.saveCommentPost
import com.project.data.savePost
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.Conflict
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.commentRoute(){
    route("/saveCommentPost"){
        authenticate {
            post {
                val comment = try {
                    call.receive<CommentPost>()
                }catch (e : ContentTransformationException){
                    call.respond(OK, SimpleResponse(false,"Comment is not uploading"))
                    return@post
                }
                if(saveCommentPost(CommentPost(comment._id,comment.postId,comment.idUser,
                    comment.commentText,comment.date))){
                    call.respond(OK)
                }else{
                    call.respond(Conflict)
                }
            }
        }
    }
}