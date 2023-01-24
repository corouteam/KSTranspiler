package it.poliba.KSTranspiler

import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CharStreams
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import it.poliba.KSTranspiler.tools.ErrorHandler
import it.poliba.KSTranspiler.tools.ErrorHandler.attachErrorHandler
import java.util.*

class SWIFTLexerTest {
   private fun lexerForCode(code: String): SwiftLexer {
       val lexer = it.poliba.KSTranspiler.SwiftLexer(CharStreams.fromString(code))
           .attachErrorHandler()

       val errors = ErrorHandler.getLexicalAndSyntaticErrors()

       if (errors.isNotEmpty()) {
           throw errors.first
       }

       return lexer
   }
    private fun lexerForResource(resourceName: String) = it.poliba.KSTranspiler.SwiftLexer(
        ANTLRInputStream(this.javaClass.getResourceAsStream("/${resourceName}.Swift"))
    ).attachErrorHandler()

    fun tokens(lexer: SwiftLexer): List<String> {
        val tokens = LinkedList<String>()

        do {
            val t = lexer.nextToken()
            when (t.type) {
                -1 -> tokens.add("EOF")
                else -> if (t.type != KotlinLexer.WS) tokens.add(lexer.ruleNames[t.type - 1])
            }
        } while (t.type != -1)

        return tokens
    }



    @Test
    fun parseTextComposable(){
        val code = "Text(\"Hello world\")"
        val result = listOf("TEXT_WIDGET", "LPAREN", "QUOTE_OPEN", "LineStrText", "QUOTE_CLOSE", "RPAREN", "EOF")
        assertEquals(result, tokens(lexerForCode(code)))
    }
    @Test
    fun parseTextComposableWithVariable(){
        val code = "Text(greet)"
        val result = listOf("TEXT_WIDGET", "LPAREN", "ID", "RPAREN", "EOF")
        assertEquals(result, tokens(lexerForCode(code)))
    }


    @Test
    fun parseBlueColor(){
        val code = "Color.blue"
        val result = listOf("COLOR", "DOT", "COLOR_BLUE", "EOF")
        assertEquals(result, tokens(lexerForCode(code)))
    }

    @Test
    fun parseBoldFontWeight(){
        val code = ".fontWeight(Font.Weight.bold)"
        val result = listOf("DOT", "FONT_WEIGHT_PARAM","LPAREN","FONT","DOT", "WEIGHT","DOT","FONT_WEIGHT_BOLD", "RPAREN", "EOF")
        assertEquals(result, tokens(lexerForCode(code)))
    }

    @Test
    fun parseTextWithParameter(){
        val code = "Text( \"Music\").bold()"
        val result = listOf("TEXT_WIDGET", "LPAREN", "QUOTE_OPEN", "LineStrText",
            "QUOTE_CLOSE", "RPAREN","DOT", "FONT_WEIGHT_BOLD","LPAREN", "RPAREN", "EOF")
        assertEquals(result, tokens(lexerForCode(code)))
    }


    @Test
    fun parseImageComposable(){
        val code = "Image(\"nome-immagine-test\")"
        val result = listOf("IMAGE_WIDGET", "LPAREN", "QUOTE_OPEN", "LineStrText", "QUOTE_CLOSE", "RPAREN", "EOF")
        assertEquals(result, tokens(lexerForCode(code)))
    }

    @Test
    fun parseAspectRatioWithParameterFit(){
        val code = "Image(\"nome-immagine-test\").aspectRatio(contentMode: ContentMode.fit)"
        val result = listOf("IMAGE_WIDGET", "LPAREN", "QUOTE_OPEN", "LineStrText", "QUOTE_CLOSE", "RPAREN","DOT", "ASPECT_RATIO_PARAM","LPAREN","CONTENT_MODE_PARAM", "COLON", "CONTENT_MODE", "DOT", "CONTENT_FIT", "RPAREN", "EOF")
        assertEquals(result, tokens(lexerForCode(code)))
    }

    @Test
    fun parseAspectRatioWithParameterFill(){
        val code = "Image(\"nome-immagine-test\").aspectRatio(contentMode: ContentMode.fill)"
        val result = listOf("IMAGE_WIDGET", "LPAREN", "QUOTE_OPEN", "LineStrText", "QUOTE_CLOSE", "RPAREN", "DOT", "ASPECT_RATIO_PARAM","LPAREN","CONTENT_MODE_PARAM", "COLON", "CONTENT_MODE", "DOT", "CONTENT_FILL", "RPAREN", "EOF")
        assertEquals(result, tokens(lexerForCode(code)))
    }

    @Test
    fun parseImageWithParameters(){
        val code = "Image(\"nome-immagine-test\").resizable().aspectRatio(contentMode: ContentMode.fill)"
        val result = listOf("IMAGE_WIDGET", "LPAREN", "QUOTE_OPEN", "LineStrText", "QUOTE_CLOSE", "RPAREN", "DOT", "RESIZABLE","LPAREN", "RPAREN", "DOT", "ASPECT_RATIO_PARAM","LPAREN","CONTENT_MODE_PARAM", "COLON", "CONTENT_MODE", "DOT", "CONTENT_FILL", "RPAREN", "EOF")
        assertEquals(result, tokens(lexerForCode(code)))
    }
}