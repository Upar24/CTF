package com.project.routes

import com.project.data.collections.Post
import com.project.data.deletePost
import com.project.data.getPostFollowing
import com.project.data.getPostForUser
import com.project.data.reponses.SimpleResponse
import com.project.data.requests.DeleteRequest
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
                if(savePost(Post(post._id,post.email,post.date,post.text))){
                    call.respond(OK)
                }else{
                    call.respond(Conflict)
                }
            }
        }
    }
    route("/deletepost"){
        authenticate {
            post {
                val post = try{
                    call.receive<DeleteRequest>()
                }catch (e : ContentTransformationException){
                    call.respond(OK,SimpleResponse(true,"post deleted"))
                    return@post
                }
                if(deletePost(post.idDelete)) call.respond(OK) else call.respond(Conflict)
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
    route("/getPostFollowing"){
        authenticate {
            get {
                val email = call.principal<UserIdPrincipal>()!!.name
                val posts = getPostFollowing(email) ?: return@get
                call.respond(OK,posts)
            }
        }
    }
}















