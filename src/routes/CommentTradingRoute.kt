package com.project.routes

import com.project.data.collections.Comment
import com.project.data.reponses.SimpleResponse
import com.project.data.saveCommentTrading
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.HttpStatusCode.Companion.Conflict
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.commentTradingRoute(){
    route("/savecommenttrading"){
        authenticate {
            post {
                val trading = try {
                    call.receive<Comment>()
                }catch (e : ContentTransformationException){
                    call.respond(OK, SimpleResponse(false,"Comment is not uploading"))
                    return@post
                }
                if(saveCommentTrading(Comment(trading._id,trading.postingId,trading.idUser,
                    trading.commentText,trading.date)
                    )
                ){
                    call.respond(OK)
                }else{
                    call.respond(Conflict)
                }
            }
        }
    }
}