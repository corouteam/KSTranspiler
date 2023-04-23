package it.poliba.KSTranspiler

import it.poliba.KSTranspiler.facade.KotlinParserFacade
import it.poliba.KSTranspiler.facade.SwiftParserFacadeScript


object App {
    @JvmStatic
    fun main(args: Array<String>) {
        var code = """
            fun printMessage(message: String, times: Int){
                for (i in 0..times) {
                    print(message)
                }
            }
        """.trimIndent().trim()
        var code2 = """val a = "Hello world""""
        var actual = code
        val parseResult = KotlinParserFacade.parse(actual)

        if (!parseResult.isCorrect()) {
            println("ERRORS FOUND")
            parseResult.errors.forEach {
                println(" * ${it.position}: ${it.message}")
            }
        }

        println(parseResult.root?.generateCode())
    }

}
