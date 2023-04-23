package it.poliba.KSTranspiler.facade

import com.strumenta.kolasu.parsing.toStream
import it.poliba.KSTranspiler.*
import it.poliba.KSTranspiler.tools.ErrorHandler.attachErrorHandler
import org.antlr.v4.runtime.*
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import it.poliba.KSTranspiler.KotlinParser
import it.poliba.KSTranspiler.KotlinLexer
import it.poliba.KSTranspiler.KotlinParser.KotlinScriptContext
import it.poliba.KSTranspiler.tools.ErrorHandler.getLexicalAndSyntaticErrors


data class AntlrParsingResult(
    val root : KotlinScriptContext?,
    val errors: List<Error>
) {
    fun isCorrect() = errors.isEmpty() && root != null
}

object KotlinAntlrParserFacade{
    fun parse(code: String): AntlrParsingResult = parse(code.toStream())

    fun parse(file: File): AntlrParsingResult = parse(FileInputStream(file))

    fun parse(inputStream: InputStream) : AntlrParsingResult {
        val lexer = KotlinLexer(ANTLRInputStream(inputStream))
            .attachErrorHandler()

        val parser = KotlinParser(CommonTokenStream(lexer))
            .attachErrorHandler()

        val antlrRoot = parser.file() as? KotlinScriptContext

        val lexicalAndSyntaticErrors = getLexicalAndSyntaticErrors()

        return AntlrParsingResult(antlrRoot, lexicalAndSyntaticErrors)
    }
}