package it.poliba.KSTranspiler.facade

import it.poliba.KSTranspiler.*
import it.poliba.KSTranspiler.tools.ErrorHandler.attachErrorHandler
import org.antlr.v4.runtime.*
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.nio.charset.Charset
import it.poliba.KSTranspiler.KotlinParser
import it.poliba.KSTranspiler.KotlinLexer
import it.poliba.KSTranspiler.KotlinParser.KotlinFileContext
import it.poliba.KSTranspiler.KotlinParser.KotlinScriptContext
import it.poliba.KSTranspiler.tools.ErrorHandler.getLexicalAndSyntaticErrors

data class AntlrParsingResult(
    val root : KotlinFileContext?,
    val errors: List<Error>
) {
    fun isCorrect() = errors.isEmpty() && root != null
}

fun String.toStream(charset: Charset = Charsets.UTF_8) = ByteArrayInputStream(toByteArray(charset))

object KotlinAntlrParserFacade {
    fun parse(code: String): AntlrParsingResult = parse(code.toStream())

    fun parse(file: File): AntlrParsingResult = parse(FileInputStream(file))

    fun parse(inputStream: InputStream) : AntlrParsingResult {
        val lexer = KotlinLexer(ANTLRInputStream(inputStream))
            .attachErrorHandler()

        val parser = KotlinParser(CommonTokenStream(lexer))
            .attachErrorHandler()

        val antlrRoot = parser.file() as? KotlinFileContext ?: throw  FileExpected()

        return AntlrParsingResult(antlrRoot, getLexicalAndSyntaticErrors())
    }
}



data class AntlrParsingResultScript(
    val root : KotlinScriptContext?,
    val errors: List<Error>
) {
    fun isCorrect() = errors.isEmpty() && root != null
}

object KotlinAntlrParserFacadeScript {
    fun parse(code: String): AntlrParsingResultScript = parse(code.toStream())

    fun parse(file: File): AntlrParsingResultScript = parse(FileInputStream(file))

    fun parse(inputStream: InputStream) : AntlrParsingResultScript {
        val lexer = KotlinLexer(ANTLRInputStream(inputStream))
            .attachErrorHandler()

        val parser = KotlinParser(CommonTokenStream(lexer))
            .attachErrorHandler()

        val antlrRoot = parser.file() as? KotlinScriptContext ?: throw  ScriptExpected()

        val lexicalAndSyntaticErrors = getLexicalAndSyntaticErrors()

        return AntlrParsingResultScript(antlrRoot, lexicalAndSyntaticErrors)
    }
}