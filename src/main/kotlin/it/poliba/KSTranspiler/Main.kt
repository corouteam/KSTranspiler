package it.poliba.KSTranspiler

import it.poliba.KSTranspiler.facade.KSTFacade
import it.poliba.KSTranspiler.server.KSTranspileResultError
import it.poliba.KSTranspiler.server.KSTranspileResultSuccess


object App {
    @JvmStatic
    fun main(args: Array<String>) {
        var code = """let a = 5"""
        val parseResult = KSTFacade.transpileSwiftToKotlin(code)
        when(parseResult){
            is KSTranspileResultError -> TODO()
            is KSTranspileResultSuccess -> println(parseResult.code)
        }

    }

}
