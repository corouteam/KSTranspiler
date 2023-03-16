package it.poliba.KSTranspiler

import it.poliba.KSTranspiler.facade.KotlinParserFacade
import it.poliba.KSTranspiler.facade.KotlinParserFacadeScript


object App {
    @JvmStatic
    fun main(args: Array<String>) {
        var code = """
            arrayOf<Int>(1,2,3)
        """.trimIndent().trim()
        var code2 = "let a = 5 + 2"
        var actual = code
        val parseResult = KotlinParserFacadeScript.parse(actual)

        if (!parseResult.isCorrect()) {
            println("ERRORS FOUND")
            parseResult.errors.forEach {
                println(" * ${it.position}: ${it.message}")
            }
        }

        println(parseResult.root?.generateCode())
    }

}
