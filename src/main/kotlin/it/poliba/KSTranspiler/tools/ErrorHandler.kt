package it.poliba.KSTranspiler.tools

import it.poliba.KSTranspiler.KotlinLexer
import it.poliba.KSTranspiler.KotlinParser
import it.poliba.KSTranspiler.SwiftLexer
import it.poliba.KSTranspiler.SwiftParser
import it.poliba.KSTranspiler.parsing.Error
import org.antlr.v4.runtime.ANTLRErrorListener
import org.antlr.v4.runtime.Parser
import org.antlr.v4.runtime.RecognitionException
import org.antlr.v4.runtime.Recognizer
import org.antlr.v4.runtime.atn.ATNConfigSet
import org.antlr.v4.runtime.dfa.DFA
import java.util.*
import kotlin.jvm.internal.Intrinsics.Kotlin

object ErrorHandler {
    private val errors = LinkedList<Exception>()
    private val errorListener = object : ANTLRErrorListener {
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
            errors.add(Exception("Error at L$line:$charPositionInline: $msg"))
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

    fun getErrors() = errors

    fun KotlinLexer.attachErrorHandler(): KotlinLexer {
        errors.clear()

        return this.apply {
            removeErrorListeners()
            addErrorListener(errorListener)
        }
    }

    fun KotlinParser.attachErrorHandler(): KotlinParser {
        errors.clear()

        return this.apply {
            removeErrorListeners()
            addErrorListener(errorListener)
        }
    }

    fun SwiftLexer.attachErrorHandler(): SwiftLexer {
        errors.clear()

        return this.apply {
            removeErrorListeners()
            addErrorListener(errorListener)
        }
    }

    fun SwiftParser.attachErrorHandler(): SwiftParser {
        errors.clear()

        return this.apply {
            removeErrorListeners()
            addErrorListener(errorListener)
        }
    }
}