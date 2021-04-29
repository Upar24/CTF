package com.project.routes

import com.project.data.collections.Comment
import com.project.data.reponses.SimpleResponse
import com.project.data.saveCommentPost
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.Conflict
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.commentPostRoute(){
    route("/savecommentpost"){
        authenticate {
            post {
                val comment = try {
                    call.receive<Comment>()
                }catch (e : ContentTransformationException){
                    call.respond(OK, SimpleResponse(false,"Comment is not uploading"))
                    return@post
                }
                if(saveCommentPost(Comment(comment._id,comment.postingId,comment.idUser,
                    comment.commentText,comment.date))){
                    call.respond(OK)
                }else{
                    call.respond(Conflict)
                }
            }
        }
    }
}