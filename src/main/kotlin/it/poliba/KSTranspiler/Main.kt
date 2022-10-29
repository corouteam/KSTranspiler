package it.poliba.KSTranspiler

import org.antlr.v4.runtime.CharStreams
import it.poliba.KSTranspiler.SandyLexer

object App {
    @JvmStatic
    fun main(args: Array<String>) {

       println("Kotlin main is running here!")
        val inputStream = CharStreams.fromString(
            "I would like to [b]emphasize[/b] this and [u]underline [b]that[/b][/u]. " +
                    "Let's not forget to quote: [quote author=\"John\"]You're wrong![/quote]"
        )
        val markupLexer = SandyLexer(inputStream)

        println("Kotlin main is running here!")
        /* val commonTokenStream = CommonTokenStream(markupLexer)
         val markupParser = it.poliba.KSTranspiler.SandyParser(commonTokenStream)*/

    }
}
