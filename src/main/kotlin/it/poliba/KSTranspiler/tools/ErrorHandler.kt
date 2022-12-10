package it.poliba.KSTranspiler.tools

import it.poliba.KSTranspiler.KotlinParser
import org.antlr.v4.runtime.ANTLRErrorListener
import org.antlr.v4.runtime.Parser
import org.antlr.v4.runtime.RecognitionException
import org.antlr.v4.runtime.Recognizer
import org.antlr.v4.runtime.atn.ATNConfigSet
import org.antlr.v4.runtime.dfa.DFA
import java.util.*

object ErrorHandler {
    fun KotlinParser.withCustomErrorHandler(): KotlinParser {
        return this.apply {
            val errorListener = object : ANTLRErrorListener {
                override fun reportAmbiguity(
                    p0: Parser?,
                    p1: DFA?,
                    p2: Int,
                    p3: Int,
                    p4: Boolean,
                    p5: BitSet?,
                    p6: ATNConfigSet?
                ) {
                    // Ignored for now
                    print("reportAmbiguity")
                }

                override fun reportAttemptingFullContext(
                    p0: Parser?,
                    p1: DFA?,
                    p2: Int,
                    p3: Int,
                    p4: BitSet?,
                    p5: ATNConfigSet?
                ) {
                    // Ignored for now
                    print("reportAttemptingFullContext")
                }

                override fun syntaxError(
                    recognizer: Recognizer<*, *>?,
                    offendingSymbol: Any?,
                    line: Int,
                    charPositionInline: Int,
                    msg: String?,
                    ex: RecognitionException?
                ) {
                    throw Exception(
                        """
                            $msg
                            PARSING ERROR at line $line, position $charPositionInline. Offending symbol is: $offendingSymbol.
                        """.trimIndent()
                    )
                }

                override fun reportContextSensitivity(
                    p0: Parser?,
                    p1: DFA?,
                    p2: Int,
                    p3: Int,
                    p4: Int,
                    p5: ATNConfigSet?
                ) {
                    // Ignored for now
                }
            }

            removeErrorListeners()
            addErrorListener(errorListener)
        }
    }
}