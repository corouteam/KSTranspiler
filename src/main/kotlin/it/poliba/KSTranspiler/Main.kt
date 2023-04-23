package it.poliba.KSTranspiler

import it.poliba.KSTranspiler.facade.SwiftParserFacadeScript


object App {
    @JvmStatic
    fun main(args: Array<String>) {
        var code = """
            for i in 1...42 {
	print("Hello world")
}
        """.trimIndent().trim()
        var code2 = "let a = 5 + 2"
        var actual = code
        val parseResult = SwiftParserFacadeScript.parse(actual)

        if (!parseResult.isCorrect()) {
            println("ERRORS FOUND")
            parseResult.errors.forEach {
                println(" * ${it.position}: ${it.message}")
            }
        }

        println(parseResult.root?.generateCode())
    }

}
