package com.project.routes

import com.project.data.isFollowingUser
import com.project.data.reponses.SimpleResponse
import com.project.data.requests.FollowRequest
import com.project.data.toggleFollowUser
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.followRoute(){
    route("/follow"){
        authenticate {
            get {
                val request = try {
                    call.receive<FollowRequest>()
                }catch (e:ContentTransformationException){
                    call.respond(BadRequest)
                    return@get
                }
                val isFollowing = isFollowingUser(request.idUser, request.email)
                if(isFollowing){
                    call.respond(OK,SimpleResponse(true,"unfollow?"))
                }else{
                    call.respond(OK,SimpleResponse(false,"follow?"))
                }
            }
            post {
                val request = try {
                    call.receive<FollowRequest>()
                }catch (e:ContentTransformationException){
                    call.respond(BadRequest)
                    return@post
                }
                val string = toggleFollowUser(request.idUser, request.email)
                call.respond(OK,SimpleResponse(true, string))
            }
        }
    }
}