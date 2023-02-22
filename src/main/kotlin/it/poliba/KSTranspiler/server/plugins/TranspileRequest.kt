package it.poliba.KSTranspiler.server.plugins

import kotlinx.serialization.Serializable

@Serializable
data class TranspileRequest (val code: String)