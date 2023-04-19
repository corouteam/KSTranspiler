package it.poliba.KSTranspiler.facade

import com.strumenta.kolasu.parsing.toStream
import it.poliba.KSTranspiler.*
import it.poliba.KSTranspiler.SwiftParser.SwiftScriptContext
import it.poliba.KSTranspiler.tools.ErrorHandler
import it.poliba.KSTranspiler.tools.ErrorHandler.attachErrorHandler
import org.antlr.v4.runtime.*
import java.io.File
import java.io.FileInputStream
import java.io.InputStream


data class AntlrParsingResultSwift(
    val root : SwiftScriptContext?,
    val errors: List<Error>
) {
    fun isCorrect() = errors.isEmpty() && root != null
}


object SwiftAntlrParserFacade {
    fun parse(code: String): AntlrParsingResultSwift {
        return parse(code.toStream())
    }

    fun parse(file: File): AntlrParsingResultSwift = parse(FileInputStream(file))

    fun parse(inputStream: InputStream): AntlrParsingResultSwift {
        val lexer = it.poliba.KSTranspiler.SwiftLexer(ANTLRInputStream(inputStream))
            .attachErrorHandler()

        val parser = it.poliba.KSTranspiler.SwiftParser(CommonTokenStream(lexer))
            .attachErrorHandler()

        val antlrRoot = parser.file() as? SwiftScriptContext
        return AntlrParsingResultSwift(antlrRoot, ErrorHandler.getLexicalAndSyntaticErrors())
    }
}