package it.poliba.KSTranspiler

import it.poliba.KSTranspiler.facade.KotlinParserFacade
import it.poliba.KSTranspiler.facade.KotlinParserFacadeScript


object App {
    @JvmStatic
    fun main(args: Array<String>) {
        var code = """
            val a = 1
            
            fun test() {
                val b = 2
                val a = 2
            }
            
        """.trimIndent().trim()
        var code2 = "let a = 5 + 2"
        var actual = code
        val parseResult = KotlinParserFacade.parse(actual)

        if (!parseResult.isCorrect()) {
            println("ERRORS FOUND")
            parseResult.errors.forEach {
                println(" * ${it.position?.start}: ${it.message}")
            }
        }

        println(parseResult.root?.generateCode())
    }

}
