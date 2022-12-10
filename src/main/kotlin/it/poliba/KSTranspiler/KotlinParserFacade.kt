package it.poliba.KSTranspiler.parsing


import it.poliba.KSTranspiler.KotlinLexer
import it.poliba.KSTranspiler.KotlinParser
import it.poliba.KSTranspiler.tools.ErrorHandler.withCustomErrorHandler
import org.antlr.v4.runtime.*
import org.antlr.v4.runtime.atn.ATNConfigSet
import org.antlr.v4.runtime.dfa.DFA
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.nio.charset.Charset
import java.util.*

data class Error(val message: String)

data class ParsingResult(val root : KotlinParser.KotlinFileContext?, val errors: List<Error>) {
    fun isCorrect() = errors.isEmpty() && root != null
}

fun String.toStream(charset: Charset = Charsets.UTF_8) = ByteArrayInputStream(toByteArray(charset))

object KotlinParserFacade {

    fun parse(code: String) : ParsingResult = parse(code.toStream())

    fun parse(file: File) : ParsingResult = parse(FileInputStream(file))

    fun parse(inputStream: InputStream) : ParsingResult {
        val errors = LinkedList<Error>()
        val errorListener = object : ANTLRErrorListener {
            override fun reportAmbiguity(p0: Parser?, p1: DFA?, p2: Int, p3: Int, p4: Boolean, p5: BitSet?, p6: ATNConfigSet?) {
                // Ignored for now
                print("reportAmbiguity")
            }

            override fun reportAttemptingFullContext(p0: Parser?, p1: DFA?, p2: Int, p3: Int, p4: BitSet?, p5: ATNConfigSet?) {
                // Ignored for now
                print("reportAttemptingFullContext")
            }

            override fun syntaxError(recognizer: Recognizer<*, *>?, offendingSymbol: Any?, line: Int, charPositionInline: Int, msg: String?, ex: RecognitionException?) {
                errors.add(Error("Error at L$line:$charPositionInline: $msg"))
            }

            override fun reportContextSensitivity(p0: Parser?, p1: DFA?, p2: Int, p3: Int, p4: Int, p5: ATNConfigSet?) {
                // Ignored for now
            }
        }

        val lexer = KotlinLexer(ANTLRInputStream(inputStream))
        lexer.removeErrorListeners()
        lexer.addErrorListener(errorListener)
        val parser =
            KotlinParser(CommonTokenStream(lexer))
                .withCustomErrorHandler()

        val root = parser.kotlinFile()
        return ParsingResult(root, errors)
    }

}