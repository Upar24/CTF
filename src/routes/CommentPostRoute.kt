package com.project.routes

import com.project.data.collections.CommentPost
import com.project.data.deleteCommentPost
import com.project.data.getCommentPost
import com.project.data.outFromMemberCommentPost
import com.project.data.reponses.SimpleResponse
import com.project.data.requests.DeleteRequest
import com.project.data.requests.SearchRequest
import com.project.data.saveCommentPost
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.HttpStatusCode.Companion.BadRequest
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
                    call.receive<CommentPost>()
                }catch (e : ContentTransformationException){
                    call.respond(OK, SimpleResponse(false,"Comment is not uploading"))
                    return@post
                }
                if(saveCommentPost(CommentPost(comment._id,comment.postId,comment.idUser,
                    comment.commentText,comment.date))){
                    call.respond(OK)
                }else{
                    call.respond(Conflict)
                }
            }
        }
    }
    route("/deletecommentpost"){
        authenticate {
            post {
                val maybe = try {
                    call.receive<DeleteRequest>()
                }catch (e:ContentTransformationException){
                    call.respond(OK,SimpleResponse(false,"havent delete comment"))
                    return@post
                }
                if(deleteCommentPost(maybe.idDelete)) call.respond(OK) else call.respond(Conflict)
            }
            get {                           //out comment member from post
                val maybelmao = try {
                    call.receive<DeleteRequest>()
                }catch (e : ContentTransformationException){
                    call.respond(OK,SimpleResponse(false,"havent followed thread"))
                    return@get
                }
                val email = call.principal<UserIdPrincipal>()!!.name
                if(outFromMemberCommentPost(maybelmao.idDelete,email)){
                    call.respond(OK,SimpleResponse(true,"you are not getting notif again"))
                }else{
                    call.respond(OK,SimpleResponse(false,"you are not a member anylonger"))
                }
            }
        }
    }
    route("/getcommentpost"){
        authenticate {
            get {
                val comment =try {
                    call.receive<SearchRequest>()
                }catch (e:ContentTransformationException){
                    call.respond(BadRequest)
                    return@get
                }
                val comments = getCommentPost(comment.search)
                call.respond(OK,comments)
            }
        }
    }
}











