package it.poliba.KSTranspiler.facade

import com.strumenta.kolasu.parsing.toStream
import it.poliba.KSTranspiler.*
import it.poliba.KSTranspiler.tools.ErrorHandler
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

data class SwiftParsingResult(
    val root : AstScript?,
    val errors: List<Error>
) {
    fun isCorrect() = errors.isEmpty() && root != null
}

object SwiftParserFacade {
    fun parse(code: String) : SwiftParsingResult = parse(code.toStream())

    fun parse(file: File) : SwiftParsingResult = parse(FileInputStream(file))

    fun parse(inputStream: InputStream) : SwiftParsingResult {
        val antlrResult = SwiftAntlrParserFacade.parse(inputStream)
        val (antlrRoot, lexicalAndSyntacticErrors) = antlrResult

        val astRoot = if (lexicalAndSyntacticErrors.isEmpty()) {
            try {
                antlrRoot?.toAst()
            } catch (e: Error) {
                return SwiftParsingResult(null, lexicalAndSyntacticErrors + e)
            }
        } else {
            null
        }

        val semanticErrors = astRoot?.validate() ?: emptyList()
        return SwiftParsingResult(astRoot, lexicalAndSyntacticErrors + semanticErrors)
    }

}

data class SwiftScriptParsingResult(
    val root : AstScript?,
    val errors: List<Error>
) {
    fun isCorrect() = errors.isEmpty() && root != null
}


object SwiftParserFacadeScript {
    fun parse(code: String) : SwiftScriptParsingResult {
        return parse(code.toStream())
    }

    fun parse(file: File) : SwiftScriptParsingResult = parse(FileInputStream(file))

    fun parse(inputStream: InputStream) : SwiftScriptParsingResult {
        val antlrResult = SwiftAntlrParserFacade.parse(inputStream)
        val (antlrRoot, lexicalAndSyntacticErrors) = antlrResult

        val astRoot: AstScript? = if (lexicalAndSyntacticErrors.isEmpty()) {
            try {
                antlrRoot?.toAst(considerPosition = true)
            } catch (e: Error) {
                return SwiftScriptParsingResult(null, lexicalAndSyntacticErrors + e)
            }
        } else {
            null
        }

        val semanticErrors = astRoot?.validate() ?: emptyList()
        return SwiftScriptParsingResult(astRoot, ErrorHandler.getLexicalAndSyntaticErrors() + semanticErrors)
    }
}