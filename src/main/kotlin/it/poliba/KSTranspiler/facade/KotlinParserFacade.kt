package it.poliba.KSTranspiler.facade

import com.strumenta.kolasu.parsing.toStream
import it.poliba.KSTranspiler.*
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

data class KotlinParsingResult(
    val root : AstScript?,
    val errors: List<Error>
) {
    fun isCorrect() = errors.isEmpty() && root != null
}

object KotlinParserFacade{
    fun parse(code: String) : KotlinParsingResult = parse(code.toStream())

    fun parse(file: File) : KotlinParsingResult = parse(FileInputStream(file))

    fun parse(inputStream: InputStream) : KotlinParsingResult {
        val antlrResult = KotlinAntlrParserFacade.parse(inputStream)
        val (antlrRoot, lexicalAndSyntacticErrors) = antlrResult
        val astRoot = if (lexicalAndSyntacticErrors.isEmpty()) {
            try {
                antlrRoot?.toAst(considerPosition = true)
            } catch (e: Error) {
                return KotlinParsingResult(null, lexicalAndSyntacticErrors + e)
            }
        } else {
            null
        }

        val semanticErrors = astRoot?.validate() ?: emptyList()
        return KotlinParsingResult(astRoot, lexicalAndSyntacticErrors + semanticErrors)
    }
}
