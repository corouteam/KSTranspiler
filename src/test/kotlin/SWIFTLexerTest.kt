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
    fun parseZStack(){
        val code = "ZStack {}"
        val result = listOf("ZSTACK", "LCURL", "RCURL", "EOF")
        assertEquals(result, tokens(lexerForCode(code)))
    }

    @Test
    fun parseStruct(){
        val code = """struct MainView: View {
    var body: some View {
        Text("Ciao")
    }
} """

        val result = listOf("STRUCT", "ID", "COLON", "ID",
            "LCURL", "NL","VAR", "ID", "COLON", "SOME", "ID", "LCURL",
            "NL", "TEXT_WIDGET", "LPAREN", "QUOTE_OPEN", "LineStrText", "QUOTE_CLOSE", "RPAREN",
            "NL", "RCURL", "NL", "RCURL", "EOF")
        assertEquals(result, tokens(lexerForCode(code)))

    }
    @Test
    fun parseStruct2(){
        val code = "struct MainView: View {\n" +
                "var name: String\n" +
                "    var body: some View {\n" +
                "        Text(\"Ciao\")\n" +
                "        Text(\"Ciao\")\n" +
                "    }\n" +
                "}"

        val result = listOf("STRUCT", "ID", "COLON", "ID",
            "LCURL", "NL","VAR", "ID", "COLON", "STRING","NL", "VAR", "ID", "COLON","SOME", "ID", "LCURL",
            "NL", "TEXT_WIDGET", "LPAREN", "QUOTE_OPEN", "LineStrText", "QUOTE_CLOSE", "RPAREN",
            "NL", "TEXT_WIDGET", "LPAREN", "QUOTE_OPEN", "LineStrText", "QUOTE_CLOSE", "RPAREN", "NL", "RCURL", "NL", "RCURL", "EOF")
        assertEquals(result, tokens(lexerForCode(code)))

    }



    @Test
    fun parseVStack(){
        val code = "VStack(alignment: VerticalAlignment.leading, spacing: 10)"
        val result = listOf(
            "VSTACK_WIDGET",
            "LPAREN",
            "ALIGNMENT_PARAM",
            "COLON",
            "VERTICAL_ALIGNMENT",
            "DOT",
            "LEADING",
            "COMMA",
            "SPACING_PARAM",
            "COLON",
            "INT_LIT",
            "RPAREN",
            "EOF"
        )
        assertEquals(result, tokens(lexerForCode(code)))
    }

    @Test
    fun parseSpacer(){
        val code = "Spacer()"
        val result = listOf("SPACER_WIDGET", "LPAREN", "RPAREN", "EOF")
        assertEquals(result, tokens(lexerForCode(code)))
      }

    @Test
    fun parseDivider(){
        val code = "Divider()"
        val result = listOf("DIVIDER_WIDGET", "LPAREN", "RPAREN", "EOF")
        assertEquals(result, tokens(lexerForCode(code)))
    }

    @Test
    fun parseDividerWithOverlay(){
        val code = "Divider().overlay(Color.blue)"
        val result = listOf("DIVIDER_WIDGET", "LPAREN", "RPAREN","DOT", "OVERLAY", "LPAREN", "COLOR", "DOT", "COLOR_BLUE", "RPAREN", "EOF")
        assertEquals(result, tokens(lexerForCode(code)))
    }

    @Test
    fun parseSpacerWithFrame(){
        val code = "Spacer().frame(width: 54.0, height: 54.0)"
        val result = listOf("SPACER_WIDGET","LPAREN","RPAREN", "DOT", "FRAME", "LPAREN", "WIDTH", "COLON", "DOUBLE_LIT", "COMMA", "HEIGHT", "COLON", "DOUBLE_LIT", "RPAREN", "EOF")
        assertEquals(result, tokens(lexerForCode(code)))
    }

    @Test
    fun parseCGFloat(){
        val code = "let margin = CGFloat(8)"
        val result = listOf(
            "LET",
            "ID",
            "ASSIGN",
            "CG_FLOAT",
            "LPAREN",
            "INT_LIT",
            "RPAREN",
            "EOF"
        )
        assertEquals(result, tokens(lexerForCode(code)))

    }


    @Test
    fun parseScrollView(){
        val code = "ScrollView(.horizontal){}"
        val result = listOf("SCROLL_VIEW", "LPAREN", "DOT", "ID", "RPAREN", "LCURL", "RCURL", "EOF")
        assertEquals(result, tokens(lexerForCode(code)))
    }
}