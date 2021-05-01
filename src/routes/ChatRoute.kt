package com.project.routes

import com.project.data.collections.Chat
import com.project.data.getChat
import com.project.data.reponses.SimpleResponse
import com.project.data.saveChat
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.chatRoute(){
    route("/savechat"){
        authenticate {
            post {
                val chat = try {
                    call.receive<Chat>()
                }catch (e:ContentTransformationException){
                    call.respond(OK,SimpleResponse(false,"try again"))
                    return@post
                }
                if(saveChat(Chat(chat._id,chat.idUser,chat.text,chat.time)))
                    call.respond(OK) else call.respond(BadRequest)
            }
            get {
                val chats = getChat()
                call.respond(OK,chats)
            }
        }
    }
}