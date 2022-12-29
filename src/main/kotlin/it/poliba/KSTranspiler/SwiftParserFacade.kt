package it.poliba.KSTranspiler.parsing


import com.strumenta.kolasu.model.debugPrint
import it.poliba.KSTranspiler.KotlinParser
import it.poliba.KSTranspiler.SwiftLexer
import it.poliba.KSTranspiler.SwiftParser
import it.poliba.KSTranspiler.SwiftParser.KotlinFileContext
import it.poliba.KSTranspiler.SwiftParser.KotlinScriptContext
import it.poliba.KSTranspiler.tools.ErrorHandler
import it.poliba.KSTranspiler.tools.ErrorHandler.attachErrorHandler
import org.antlr.v4.runtime.*
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.nio.charset.Charset
import java.util.*


object SwiftParserFacade {
    fun parse(code: String) : ParsingResultSwift = parse(code.toStream())

    fun parse(file: File) : ParsingResultSwift = parse(FileInputStream(file))

    fun parse(inputStream: InputStream) : ParsingResultSwift {
        val lexer = SwiftLexer(ANTLRInputStream(inputStream))
            .attachErrorHandler()

        val parser = SwiftParser(CommonTokenStream(lexer))
            .attachErrorHandler()
        val root = parser.file() as? KotlinFileContext ?: throw  Exception("File expected")


        return ParsingResultSwift(root, ErrorHandler.getErrors())
    }

}
data class ParsingResultSwift(
    val root : SwiftParser.KotlinFileContext?,
    val errors: List<Exception>
) {
    fun isCorrect() = errors.isEmpty() && root != null
}

data class ParsingResultScriptSwift(
    val root : SwiftParser.KotlinScriptContext?,
    val errors: List<Exception>
) {
    fun isCorrect() = errors.isEmpty() && root != null
}


object SwiftParserFacadeScript {
    fun parse(code: String) : ParsingResultScriptSwift  {
        return parse(code.toStream())
    }

    fun parse(file: File) : ParsingResultScriptSwift = parse(FileInputStream(file))

    fun parse(inputStream: InputStream) : ParsingResultScriptSwift {
        val lexer = SwiftLexer(ANTLRInputStream(inputStream))
            .attachErrorHandler()

        val parser = SwiftParser(CommonTokenStream(lexer))
            .attachErrorHandler()
        val root = parser.file() as? KotlinScriptContext ?: throw  Exception("Script expected")
        return ParsingResultScriptSwift(root, ErrorHandler.getErrors())
    }

}