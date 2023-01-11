package it.poliba.KSTranspiler

import it.poliba.KSTranspiler.facade.KotlinParserFacade
import it.poliba.KSTranspiler.facade.KotlinParserFacadeScript


object App {
    @JvmStatic
    fun main(args: Array<String>) {
        var code = """val test = listOf<Int>(1, 2, "A")""".trim()
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
