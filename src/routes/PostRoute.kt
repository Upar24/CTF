package com.project.routes

import com.project.data.checkIfPostExist
import com.project.data.collections.Post
import com.project.data.reponses.SimpleResponse
import com.project.data.savePost
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.postRoute() {
    route("/Post"){
        authenticate {
            post {
                val post = try {
                    call.receive<Post>()
                }catch (e : ContentTransformationException){
                    call.respond(OK,SimpleResponse(false,"post is not uploading"))
                    return@post
                }
                val postExits = checkIfPostExist(post._id)
                if(!postExits){
                    if(savePost(post)){
                        call.respond(OK,SimpleResponse(true,"successfully posting"))
                    }else{
                        call.respond(OK,SimpleResponse(false,"unknown error occured"))
                    }
                }else{
                    call.respond(OK,SimpleResponse(false,"cannt edit post"))
                }
            }
        }
    }
}