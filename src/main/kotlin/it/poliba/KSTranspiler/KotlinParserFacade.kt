package it.poliba.KSTranspiler.parsing


import it.poliba.KSTranspiler.KotlinLexer
import it.poliba.KSTranspiler.KotlinParser
import it.poliba.KSTranspiler.tools.ErrorHandler
import it.poliba.KSTranspiler.tools.ErrorHandler.attachErrorHandler
import org.antlr.v4.runtime.*
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.nio.charset.Charset

data class Error(val message: String)

data class ParsingResult(
    val root : KotlinParser.KotlinFileContext?,
    val errors: List<Exception>
) {
    fun isCorrect() = errors.isEmpty() && root != null
}

fun String.toStream(charset: Charset = Charsets.UTF_8) = ByteArrayInputStream(toByteArray(charset))

object KotlinParserFacade {
    fun parse(code: String) : ParsingResult = parse(code.toStream())

    fun parse(file: File) : ParsingResult = parse(FileInputStream(file))

    fun parse(inputStream: InputStream) : ParsingResult {
        val lexer = KotlinLexer(ANTLRInputStream(inputStream))
            .attachErrorHandler()

        val parser = KotlinParser(CommonTokenStream(lexer))
            .attachErrorHandler()

        val root = parser.kotlinFile()

        return ParsingResult(root, ErrorHandler.getErrors())
    }
}