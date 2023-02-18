package it.poliba.KSTranspiler.facade

import it.poliba.KSTranspiler.*
import it.poliba.KSTranspiler.SwiftParser.SwiftFileContext
import it.poliba.KSTranspiler.SwiftParser.SwiftScriptContext
import it.poliba.KSTranspiler.tools.ErrorHandler
import it.poliba.KSTranspiler.tools.ErrorHandler.attachErrorHandler
import org.antlr.v4.runtime.*
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

data class AntlrParsingResultSwift(
    val root : SwiftFileContext?,
    val errors: List<Error>
) {
    fun isCorrect() = errors.isEmpty() && root != null
}

object SwiftAntlrParserFacade {
    fun parse(code: String) : AntlrParsingResultSwift = parse(code.toStream())

    fun parse(file: File) : AntlrParsingResultSwift = parse(FileInputStream(file))

    fun parse(inputStream: InputStream) : AntlrParsingResultSwift {
        val lexer = it.poliba.KSTranspiler.SwiftLexer(ANTLRInputStream(inputStream))
            .attachErrorHandler()

        val parser = it.poliba.KSTranspiler.SwiftParser(CommonTokenStream(lexer))
            .attachErrorHandler()

        val antlrRoot = parser.file() as? SwiftFileContext ?: throw FileExpectedException()
        return AntlrParsingResultSwift(antlrRoot, ErrorHandler.getLexicalAndSyntaticErrors())
    }

}
data class AntlrParsingResultScriptSwift(
    val root : SwiftScriptContext?,
    val errors: List<Error>
) {
    fun isCorrect() = errors.isEmpty() && root != null
}


object SwiftAntlrParserFacadeScript {
    fun parse(code: String): AntlrParsingResultScriptSwift {
        return parse(code.toStream())
    }

    fun parse(file: File): AntlrParsingResultScriptSwift = parse(FileInputStream(file))

    fun parse(inputStream: InputStream): AntlrParsingResultScriptSwift {
        val lexer = it.poliba.KSTranspiler.SwiftLexer(ANTLRInputStream(inputStream))
            .attachErrorHandler()

        val parser = it.poliba.KSTranspiler.SwiftParser(CommonTokenStream(lexer))
            .attachErrorHandler()

        val antlrRoot = parser.file() as? SwiftScriptContext ?: throw  ScriptExpectedException()
        return AntlrParsingResultScriptSwift(antlrRoot, ErrorHandler.getLexicalAndSyntaticErrors())
    }
}