package it.poliba.KSTranspiler.facade

import it.poliba.KSTranspiler.FileExpectedException
import it.poliba.KSTranspiler.generateCode
import it.poliba.KSTranspiler.server.*

object KSTFacade {
    fun transpileKotlinToSwift(code: String): KSTranspileResult {
        return try {
            transpileKotlinToSwiftFile(code)
        }catch (e: FileExpectedException){
            transpileKotlinToSwiftScript(code)
        }

    }

    private fun transpileKotlinToSwiftFile(code: String): KSTranspileResult{
        var parseResult = KotlinParserFacade.parse(code)
        if(parseResult.errors.isEmpty()){
            val result  = parseResult.root!!.generateCode()
            return KSTranspileResultSuccess(result)
        }else{
            val result = ParseErrorResult("Si è verificato un errore", parseResult.errors.map { ParseError(it.message, it.position?.toString() ?: "") })
            return  KSTranspileResultError(result)
        }
    }

    private fun transpileKotlinToSwiftScript(code: String): KSTranspileResult{
        var parseResult = KotlinParserFacadeScript.parse(code)
        if(parseResult.errors.isEmpty()){
            val result  = parseResult.root!!.generateCode()
            return KSTranspileResultSuccess(result)
        }else{
            val result = ParseErrorResult("Si è verificato un errore", parseResult.errors.map { ParseError(it.message, it.position?.toString() ?: "") })
            return  KSTranspileResultError(result)
        }
    }

    fun transpileSwiftToKotlin(code: String): KSTranspileResult {
        return try {
            transpileSwiftToKotlinFile(code)
        }catch (e: FileExpectedException){
            transpileSwiftToKotlinScript(code)
        }

    }

    private fun transpileSwiftToKotlinFile(code: String): KSTranspileResult{
        var parseResult = SwiftParserFacade.parse(code)
        if(parseResult.errors.isEmpty()){
            val result  = parseResult.root!!.generateCode()
            return KSTranspileResultSuccess(result)
        }else{
            val result = ParseErrorResult("Si è verificato un errore", parseResult.errors.map { ParseError(it.message, it.position?.toString() ?: "") })
            return  KSTranspileResultError(result)
        }
    }

    private fun transpileSwiftToKotlinScript(code: String): KSTranspileResult{
        var parseResult = SwiftAntlrParserFacadeScript.parse(code)
        if(parseResult.errors.isEmpty()){
            throw Exception("Not implemented yet")
            /*val result  = parseResult.root!!.generateCode()
            return KSTranspileResultSuccess(result)*/
        }else{
            val result = ParseErrorResult("Si è verificato un errore", parseResult.errors.map { ParseError(it.message, it.position?.toString() ?: "") })
            return  KSTranspileResultError(result)
        }
    }
}

