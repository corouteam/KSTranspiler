package it.poliba.KSTranspiler.tools

import com.strumenta.kolasu.model.Point
import com.strumenta.kolasu.model.Position
import com.strumenta.kolasu.model.pos
import it.poliba.KSTranspiler.Error
import it.poliba.KSTranspiler.KotlinLexer
import it.poliba.KSTranspiler.KotlinParser
import it.poliba.KSTranspiler.SwiftLexer
import it.poliba.KSTranspiler.SwiftParser
import org.antlr.v4.runtime.*
import org.antlr.v4.runtime.atn.ATNConfigSet
import org.antlr.v4.runtime.dfa.DFA
import java.util.*

object ErrorHandler {
    private val lexicalAndSyntaticErrors = LinkedList<Error>()

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
        }

        override fun syntaxError(
            recognizer: Recognizer<*, *>?,
            offendingSymbol: Any?,
            line: Int,
            charPositionInline: Int,
            msg: String?,
            ex: RecognitionException?
        ) {
            ex?.printStackTrace()
            val token = offendingSymbol as? CommonToken
            val message = if(token != null){
                "Unrecognized symbol ${token.text}"
            }else {
                msg ?: "Unexpected symbol"
            }


            lexicalAndSyntaticErrors.add(Error(message,
                Position(
                    Point(line, charPositionInline),
                    Point(line, charPositionInline+1))
            ))
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

    fun getLexicalAndSyntaticErrors() = lexicalAndSyntaticErrors

    fun KotlinLexer.attachErrorHandler(): KotlinLexer {
        lexicalAndSyntaticErrors.clear()

        return this.apply {
            removeErrorListeners()
            addErrorListener(errorListener)
        }
    }

    fun KotlinParser.attachErrorHandler(): KotlinParser {
        lexicalAndSyntaticErrors.clear()

        return this.apply {
            removeErrorListeners()
            addErrorListener(errorListener)
        }
    }

    fun SwiftLexer.attachErrorHandler(): SwiftLexer {
        lexicalAndSyntaticErrors.clear()

        return this.apply {
            removeErrorListeners()
            addErrorListener(errorListener)
        }
    }

    fun SwiftParser.attachErrorHandler(): SwiftParser {
        lexicalAndSyntaticErrors.clear()

        return this.apply {
            removeErrorListeners()
            addErrorListener(errorListener)
        }
    }
}