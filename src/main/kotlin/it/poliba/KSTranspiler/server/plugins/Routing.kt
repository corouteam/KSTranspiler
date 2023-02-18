package com.example.plugins

import com.strumenta.kolasu.model.errors
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import it.poliba.KSTranspiler.FileExpected
import it.poliba.KSTranspiler.ScriptExpected
import it.poliba.KSTranspiler.facade.KSTFacade
import it.poliba.KSTranspiler.facade.KotlinParserFacade
import it.poliba.KSTranspiler.facade.KotlinParserFacadeScript
import it.poliba.KSTranspiler.generateCode
import it.poliba.KSTranspiler.server.*
import java.lang.Exception

fun Application.configureRouting() {
    routing {
        get("/kotlin/transpileToSwift") {
            try{
                var code = call.receiveText().trimIndent()
                val result = KSTFacade.transpileKotlinToSwift(code)
                when (result){
                    is KSTranspileResultSuccess -> call.respond(result.code)
                    is KSTranspileResultError -> call.respond(HttpStatusCode.BadRequest, result.error)
                }
            }catch (e: Throwable){
                    call.respond(HttpStatusCode.InternalServerError, "Si è verificato un errore nella traduzione\n${e.localizedMessage}" )
            }
        }

        get("/swift/transpileToKotlin") {
            try{
                var code = call.receiveText().trimIndent()
                val result = KSTFacade.transpileSwiftToKotlin(code)
                when (result){
                    is KSTranspileResultSuccess -> call.respond(result.code)
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
