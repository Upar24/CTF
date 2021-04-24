package com.project.routes

import com.project.data.collections.Post
import com.project.data.reponses.SimpleResponse
import com.project.data.requests.PostRequest
import com.project.data.savePost
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.HttpStatusCode.Companion.Conflict
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.postRoute() {
    route("/Post"){
        authenticate {
            post {
                val post = try {
                    call.receive<PostRequest>()
                }catch (e : ContentTransformationException){
                    call.respond(OK,SimpleResponse(false,"post is not uploading"))
                    return@post
                }
                if(savePost(Post(post.id,post.idUser,post.date,post.text,post.type))){
                    call.respond(OK)
                }else{
                    call.respond(Conflict)
                }

            }
        }
    }
}