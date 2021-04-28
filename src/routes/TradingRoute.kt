package com.project.routes

import com.project.data.*
import com.project.data.collections.Trading
import com.project.data.reponses.SimpleResponse
import com.project.data.requests.DeleteRequest
import com.project.data.requests.SearchRequest
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.Conflict
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.tradingRoute(){
    route("/savetrading"){
        authenticate {
            post {
                val trading = try {
                    call.receive<Trading>()
                }catch (e:ContentTransformationException){
                    call.respond(OK,SimpleResponse(true,"trading is posting"))
                    return@post
                }
                if(saveTrading(Trading(trading._id,trading.email,trading.date,trading.text,
                    trading.description,trading.selling,trading.amountSelling,trading.buying,
                    trading.amountBuying))) call.respond(OK) else call.respond(Conflict)
            }
        }
    }
    route("/searchtrading"){
        post{
            val search = try{
                call.receive<SearchRequest>()
            }catch (e:ContentTransformationException){
                call.respond(BadRequest)
                return@post
            }
            val trading = getTrading(search.search)
            call.respond(OK,trading)
        }
    }
    route("/tradingfilter"){
        post{                        //only to get buying search trading  lmao
            val search = try{
                call.receive<SearchRequest>()
            }catch (e:ContentTransformationException){
                call.respond(BadRequest)
                return@post
            }
            val trading = getBuying(search.search)
            call.respond(OK,trading)
        }
        get{                        //only to get selling search trading  lmao
            val search = try{
                call.receive<SearchRequest>()
            }catch (e:ContentTransformationException){
                call.respond(BadRequest)
                return@get
            }
            val trading = getSelling(search.search)
            call.respond(OK,trading)
        }
    }
    route("/deletetrading"){
        post{
            val delete = try {
                call.receive<DeleteRequest>()
            }catch (e : ContentTransformationException){
                call.respond(BadRequest)
                return@post
            }
            if(deleteTrading(delete.idDelete)) call.respond(OK) else call.respond(BadRequest)
        }
    }
}

















