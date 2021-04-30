package com.project.routes

import com.project.data.*
import com.project.data.collections.CommentPost
import com.project.data.collections.CommentTrading
import com.project.data.reponses.SimpleResponse
import com.project.data.requests.DeleteRequest
import com.project.data.requests.SearchRequest
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
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
                    call.receive<CommentTrading>()
                }catch (e : ContentTransformationException){
                    call.respond(OK, SimpleResponse(false,"Comment is not uploading"))
                    return@post
                }
                if(saveCommentTrading(
                        CommentTrading(trading._id,trading.tradingId,trading.idUser,
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
    route("/getcommenttrading"){
        authenticate {
            get {
                val comment =try {
                    call.receive<SearchRequest>()
                }catch (e:ContentTransformationException){
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }
                val comments = getCommentTrading(comment.search)
                call.respond(OK,comments)
            }
        }
    }
    route("/deletecommenttrading"){
        authenticate {
            post {
                val maybe = try {
                    call.receive<DeleteRequest>()
                }catch (e:ContentTransformationException){
                    call.respond(OK,SimpleResponse(false,"havent delete comment"))
                    return@post
                }
                if(deleteCommentTrading(maybe.idDelete)) call.respond(OK) else call.respond(Conflict)
            }
            get {                           //out comment member from post
                val maybelmao = try {
                    call.receive<DeleteRequest>()
                }catch (e : ContentTransformationException){
                    call.respond(OK,SimpleResponse(false,"havent followed thread"))
                    return@get
                }
                val email = call.principal<UserIdPrincipal>()!!.name
                if(outFromMemberCommentTrading(maybelmao.idDelete,email)){
                    call.respond(OK,SimpleResponse(true,"you are not getting notif again"))
                }else{
                    call.respond(OK,SimpleResponse(false,"you are not a member anylonger"))
                }
            }
        }
    }
}