package it.poliba.KSTranspiler

import it.poliba.KSTranspiler.facade.KotlinParserFacade
import it.poliba.KSTranspiler.facade.KotlinParserFacadeScript


object App {
    @JvmStatic
    fun main(args: Array<String>) {
        var code = """if (1 <= 2) {
                print("1")
            }""".trimMargin().trim()
        var actual = code
        val parseResult = KotlinParserFacadeScript.parse(actual)

        if (!parseResult.isCorrect()) {
            println("ERRORS FOUND")
            parseResult.errors.forEach {
                println(" * ${it.position?.start}: ${it.message}")
            }
        }

        println(parseResult.root?.generateCode())
    }

}
