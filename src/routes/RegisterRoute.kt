package com.project.routes

import com.project.data.checkIfUserExist
import com.project.data.collections.User
import com.project.data.registerUser
import com.project.data.reponses.SimpleResponse
import com.project.data.requests.AccountRequest
import io.ktor.application.*
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import javax.lang.model.util.SimpleElementVisitor6

fun Route.registerRoute(){
    route("/register"){
        post {
            val request = try{
                call.receive<AccountRequest>()
            }catch (e : ContentTransformationException){
                call.respond(BadRequest)
                return@post
            }
            val userExist = checkIfUserExist(request.email)
            if (!userExist) {
                if (registerUser(User(request.email, request.password))) {
                    call.respond(OK, SimpleResponse(true, "register successfully"))
                } else {
                    call.respond(OK, SimpleResponse(false, "An unknown occured"))
                }
            }else{
                call.respond(OK,SimpleResponse(false,"user already exist"))
            }
        }
    }
}