package com.project

import com.project.data.checkPasswordForEmail
import com.project.routes.*
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install (DefaultHeaders)
    install (CallLogging)
    install (ContentNegotiation){
        gson{
            setPrettyPrinting()
        }
    }
    install(Authentication){
        configureAuth()
    }
    install (Routing){
        registerRoute()
        loginRoute()
        followRoute()
        pestaRoute()
        postRoute()
        tradingRoute()
        commentRoute()
    }
}
private fun Authentication.Configuration.configureAuth(){
    basic{
        realm = "CTF Server"
        validate { crudentials ->
            val email = crudentials.name
            val password = crudentials.password
            if(checkPasswordForEmail(email,password)){
                UserIdPrincipal(email)
            }else null
        }
    }
}




