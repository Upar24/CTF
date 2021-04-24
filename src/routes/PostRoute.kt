package com.project.routes

import com.project.data.collections.Post
import com.project.data.getPostForUser
import com.project.data.reponses.SimpleResponse
import com.project.data.savePost
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.HttpStatusCode.Companion.Conflict
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.postRoute() {
    route("/savePost"){
        authenticate {
            post {
                val post = try {
                    call.receive<Post>()
                }catch (e : ContentTransformationException){
                    call.respond(OK,SimpleResponse(false,"post is not uploading"))
                    return@post
                }
                if(savePost(Post(post._id,post.email,post.date,post.text,post.type))){
                    call.respond(OK)
                }else{
                    call.respond(Conflict)
                }

            }
        }
    }
    route("/getPostUser"){
        authenticate {
            get {
                val email = call.principal<UserIdPrincipal>()!!.name
                val posts = getPostForUser(email)
                call.respond(OK,posts)
            }
        }
    }
}