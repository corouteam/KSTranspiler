package it.poliba.KSTranspiler.parsing


import com.strumenta.kolasu.model.debugPrint
import it.poliba.KSTranspiler.KotlinLexer
import it.poliba.KSTranspiler.KotlinParser
import it.poliba.KSTranspiler.KotlinParser.KotlinFileContext
import it.poliba.KSTranspiler.KotlinParser.KotlinScriptContext
import it.poliba.KSTranspiler.tools.ErrorHandler
import it.poliba.KSTranspiler.tools.ErrorHandler.attachErrorHandler
import org.antlr.v4.runtime.*
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.nio.charset.Charset
import java.util.*

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
        val root = parser.file() as? KotlinFileContext ?: throw  Exception("File expected")
        return ParsingResult(root, ErrorHandler.getErrors())
    }

}



data class ParsingResultScript(
    val root : KotlinParser.KotlinScriptContext?,
    val errors: List<Exception>
) {
    fun isCorrect() = errors.isEmpty() && root != null
}

object KotlinParserFacadeScript {
    fun parse(code: String) : ParsingResultScript = parse(code.toStream())

    fun parse(file: File) : ParsingResultScript = parse(FileInputStream(file))

    fun parse(inputStream: InputStream) : ParsingResultScript {
        val lexer = KotlinLexer(ANTLRInputStream(inputStream))
            .attachErrorHandler()

        val parser = KotlinParser(CommonTokenStream(lexer))
            .attachErrorHandler()
        val root = parser.file() as? KotlinScriptContext ?: throw  Exception("Script expected")
        return ParsingResultScript(root, ErrorHandler.getErrors())
    }

}