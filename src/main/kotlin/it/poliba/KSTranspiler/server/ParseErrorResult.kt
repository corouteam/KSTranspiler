package it.poliba.KSTranspiler.server

import it.poliba.KSTranspiler.Error
import kotlinx.serialization.Serializable

@Serializable
data class ParseErrorResult (
    val message: String,
    val errors: List<ParseError>
)

@Serializable
data class ParseError(
    val message: String,
    val position: String
)


@Serializable
sealed class KSTranspileResult
@Serializable
class KSTranspileResultSuccess(val code: String): KSTranspileResult()
@Serializable
class KSTranspileResultError(val error: ParseErrorResult): KSTranspileResult()


