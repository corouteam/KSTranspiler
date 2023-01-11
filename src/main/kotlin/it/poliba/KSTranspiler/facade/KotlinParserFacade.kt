package it.poliba.KSTranspiler.facade

import it.poliba.KSTranspiler.*
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

data class KotlinParsingResult(
    val root : AstFile?,
    val errors: List<Error>
) {
    fun isCorrect() = errors.isEmpty() && root != null
}

object KotlinParserFacade {
    fun parse(code: String) : KotlinParsingResult = parse(code.toStream())

    fun parse(file: File) : KotlinParsingResult = parse(FileInputStream(file))

    fun parse(inputStream: InputStream) : KotlinParsingResult {
        val antlrResult = KotlinAntlrParserFacade.parse(inputStream)
        val (antlrRoot, lexicalAndSyntacticErrors) = antlrResult

        val astRoot = if (lexicalAndSyntacticErrors.isEmpty()) {
            antlrRoot?.toAst(considerPosition = true)
        } else {
            null
        }

        val semanticErrors = astRoot?.validate() ?: emptyList()
        return KotlinParsingResult(astRoot, lexicalAndSyntacticErrors + semanticErrors)
    }
}

data class KotlinScriptParsingResult(
    val root : AstScript?,
    val errors: List<Error>
) {
    fun isCorrect() = errors.isEmpty() && root != null
}

object KotlinParserFacadeScript {
    fun parse(code: String) : KotlinScriptParsingResult = parse(code.toStream())

    fun parse(file: File) : KotlinScriptParsingResult = parse(FileInputStream(file))

    fun parse(inputStream: InputStream) : KotlinScriptParsingResult {
        val antlrResult = KotlinAntlrParserFacadeScript.parse(inputStream)
        val (antlrRoot, lexicalAndSyntacticErrors) = antlrResult

        val astRoot: AstScript? = if (lexicalAndSyntacticErrors.isEmpty()) {
            antlrRoot?.toAst(considerPosition = true)
        } else {
            null
        }

        val semanticErrors = astRoot?.validate() ?: emptyList()
        return KotlinScriptParsingResult(astRoot, lexicalAndSyntacticErrors + semanticErrors)
    }
}