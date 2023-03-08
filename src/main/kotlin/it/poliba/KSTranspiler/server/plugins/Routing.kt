package com.example.plugins

import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import it.poliba.KSTranspiler.facade.KSTFacade
import it.poliba.KSTranspiler.server.*
import it.poliba.KSTranspiler.server.plugins.TranspileRequest

fun Application.configureRouting() {
    routing {
        post("/kotlin/transpileToSwift") {
            try{
               // var request = call.receive<TranspileRequest>()
                var code = call.receiveText()
                val result = KSTFacade.transpileKotlinToSwift(code)
                when (result){
                    is KSTranspileResultSuccess -> call.respondText(result.code)
                    is KSTranspileResultError -> call.respond(HttpStatusCode.BadRequest, result.error)
                }
                /*
                when (result){
                    is KSTranspileResultSuccess -> call.respond(TranspileRequest(code = result.code))
                    is KSTranspileResultError -> call.respond(HttpStatusCode.BadRequest, result.error)
                }*/
            }catch (e: Throwable){
                    call.respond(HttpStatusCode.InternalServerError, "Si è verificato un errore nella traduzione\n${e.localizedMessage}" )
            }
        }

        post("/swift/transpileToKotlin") {
            try{
                var request = call.receive<TranspileRequest>()
                val result = KSTFacade.transpileSwiftToKotlin(request.code)
                when (result){
                    is KSTranspileResultSuccess -> call.respond(TranspileRequest(code = result.code))
                    is KSTranspileResultError -> call.respond(HttpStatusCode.BadRequest, result.error)
                }
            }catch (e: Throwable){
                call.respond(HttpStatusCode.InternalServerError, "Si è verificato un errore nella traduzione\n${e.localizedMessage}" )
            }
        }
        get("/") {
            call.respondText("KSTranspiler is up and running, enjoy! ")
        }
    }


}
