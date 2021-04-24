package com.project.routes

import com.project.data.*
import com.project.data.collections.Pesta
import com.project.data.reponses.SimpleResponse
import com.project.data.requests.GetPestas
import com.project.data.requests.PotRequest
import com.project.data.requests.PestaRequest
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.pestaRoute(){
    route("/pesta"){
        authenticate {
            post {
                val pesta = try {
                    call.receive<PestaRequest>()
                } catch (e: ContentTransformationException) {
                    call.respond(OK, SimpleResponse(false, "pesta not saved"))
                    return@post
                }
                if (savePesta(Pesta(pesta._id, pesta.name, pesta.duration)))
                    call.respond(OK) else call.respond(BadRequest)
            }
            get{
                val group = try{
                    call.receive<GetPestas>()
                }catch (e:ContentTransformationException){
                    call.respond(BadRequest)
                    return@get
                }
                val pestas = getPesta(group.group)
                call.respond(OK,pestas)
            }
        }
    }
    route("/Pot"){
            get{
                val pot = try {
                    call.receive<PotRequest>()
                }catch (e:ContentTransformationException){
                    call.respond(BadRequest)
                    return@get
                }
                val pesta = getPot(pot.status)
                call.respond(OK,pesta)
            }
            post {
                val listPotdPpotd = try {
                    call.receive<PotRequest>()
                }catch (e:ContentTransformationException){
                    call.respond(BadRequest)
                    return@post
                }
                val pesta =  getSudahs(listPotdPpotd.status)
                call.respond(OK,pesta)
            }
    }
}





