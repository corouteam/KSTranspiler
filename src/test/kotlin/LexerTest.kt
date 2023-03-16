package it.poliba.KSTranspiler

import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CharStreams
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import it.poliba.KSTranspiler.tools.ErrorHandler
import it.poliba.KSTranspiler.tools.ErrorHandler.attachErrorHandler
import java.util.*
import kotlin.jvm.internal.Intrinsics.Kotlin

class LexerTest {
    private fun lexerForCode(code: String): KotlinLexer {
        val lexer = it.poliba.KSTranspiler.KotlinLexer(CharStreams.fromString(code))
            .attachErrorHandler()

        val errors = ErrorHandler.getLexicalAndSyntaticErrors()

        if (errors.isNotEmpty()) {
            throw errors.first
        }

        return lexer
    }


    private fun lexerForResource(resourceName: String) = it.poliba.KSTranspiler.KotlinLexer(
        ANTLRInputStream(this.javaClass.getResourceAsStream("/${resourceName}.Kotlin"))
    ).attachErrorHandler()

    fun tokens(lexer: KotlinLexer): List<String> {
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
    fun parseVarDeclarationAssignedAnIntegerLiteral() {
        assertEquals(
            listOf("VAR", "ID", "ASSIGN", "INT_LIT", "EOF"),
            tokens(lexerForCode("var a = 1"))
        )
    }

    @Test
    fun parseVarDeclarationAssignedAnStringLiteral() {
        assertEquals(
            listOf("VAR", "ID", "ASSIGN", "QUOTE_OPEN", "LineStrText", "QUOTE_CLOSE", "EOF"),
            tokens(lexerForCode("var a = \"test\""))
        )
    }

    @Test
    fun parseVarDeclarationIntType() {
        assertEquals(
            listOf("VAR", "ID", "COLON", "INT", "EOF"),
            tokens(lexerForCode("var a: Int"))
        )
    }

    @Test
    fun parseVaLDeclarationIntType() {
        assertEquals(
            listOf("VAL", "ID", "COLON", "INT", "EOF"),
            tokens(lexerForCode("val a: Int"))
        )
    }


    @Test
    fun parseValDeclarationAssignedAnIntegerLiteral() {
        assertEquals(
            listOf("VAL", "ID", "ASSIGN", "INT_LIT", "EOF"),
            tokens(lexerForCode("val a = 1"))
        )
    }

    @Test
    fun parseVarDeclarationAssignedADecimalLiteral() {
        assertEquals(
            listOf("VAR", "ID", "ASSIGN", "DOUBLE_LIT", "EOF"),
            tokens(lexerForCode("var a = 1.23"))
        )
    }

    @Test
    fun parseVarDeclarationAssignedASum() {
        assertEquals(
            listOf("VAR", "ID", "ASSIGN", "INT_LIT", "PLUS", "INT_LIT", "EOF"),
            tokens(lexerForCode("var a = 1 + 2"))
        )
    }

    @Test
    fun parseMathematicalExpression() {
        assertEquals(
            listOf("INT_LIT", "PLUS", "ID", "ASTERISK", "INT_LIT", "DIVISION", "INT_LIT", "MINUS", "INT_LIT", "EOF"),
            tokens(lexerForCode("1 + a * 3 / 4 - 5"))
        )
    }

    @Test
    fun parseMathematicalExpressionWithParenthesis() {
        assertEquals(
            listOf("INT_LIT", "PLUS", "LPAREN", "ID", "ASTERISK", "INT_LIT", "RPAREN", "MINUS", "DOUBLE_LIT", "EOF"),
            tokens(lexerForCode("1 + (a * 3) - 5.12"))
        )
    }

    @Test
    fun parsePrintDeclarationLiteral() {
        assertEquals(
            listOf("PRINT", "LPAREN", "QUOTE_OPEN", "LineStrText", "QUOTE_CLOSE", "RPAREN", "EOF"),
            tokens(lexerForCode("print(\"ciao\")"))
        )
    }

    @Test
    fun parseAssignmentDeclarationLiteral() {
        assertEquals(
            listOf("ID", "ASSIGN", "INT_LIT", "EOF"),
            tokens(lexerForCode("a = 5"))
        )
    }

    @Test
    fun parseIfStatement() {
        assertEquals(
            listOf(
                "IF",
                "LPAREN",
                "BOOL_LIT",
                "RPAREN",
                "LCURL",
                "PRINT",
                "LPAREN",
                "QUOTE_OPEN",
                "LineStrText",
                "QUOTE_CLOSE",
                "RPAREN",
                "RCURL",
                "EOF"
            ),
            tokens(
                lexerForCode(
                    "if(true){" +
                            "   print(\"Hello world\")" +
                            "}"
                )
            )
        )
    }

    @Test
    fun parseFunction() {
        val code = "fun test(x: Int, y: Int)\t{\tprint(\"ciao\")}"
        val result = listOf(
            "FUN",
            "ID",
            "LPAREN",
            "ID",
            "COLON",
            "INT",
            "COMMA",
            "ID",
            "COLON",
            "INT",
            "RPAREN",
            "LCURL",
            "PRINT",
            "LPAREN",
            "QUOTE_OPEN",
            "LineStrText",
            "QUOTE_CLOSE",
            "RPAREN",
            "RCURL",
            "EOF"
        )
        assertEquals(result, tokens(lexerForCode(code)))
    }

    @Test
    fun parseFunctionExpression() {
        val code = "fun test(x: Int, y: Int) = 3"
        val result = listOf(
            "FUN",
            "ID",
            "LPAREN",
            "ID",
            "COLON",
            "INT",
            "COMMA",
            "ID",
            "COLON",
            "INT",
            "RPAREN",
            "ASSIGN",
            "INT_LIT",
            "EOF"
        )
        assertEquals(result, tokens(lexerForCode(code)))
    }

    @Test
    fun parseFunctionExpressionWithReturn() {
        val code = "fun test(x: Int, y: Int): Int {return 5}"
        val result = listOf(
            "FUN",
            "ID",
            "LPAREN",
            "ID",
            "COLON",
            "INT",
            "COMMA",
            "ID",
            "COLON",
            "INT",
            "RPAREN",
            "COLON",
            "INT",
            "LCURL",
            "RETURN",
            "INT_LIT",
            "RCURL",
            "EOF"
        )
        assertEquals(result, tokens(lexerForCode(code)))
    }

    @Test
    fun parseFunctionCall() {
        val code = "test(\"test\", 42)"

        val result =
            listOf("ID", "LPAREN", "QUOTE_OPEN", "LineStrText", "QUOTE_CLOSE", "COMMA", "INT_LIT", "RPAREN", "EOF")
        assertEquals(result, tokens(lexerForCode(code)))
    }

    @Test
    fun parseRangeExpression() {
        val code = "1..42"
        val result = listOf("INT_LIT", "RANGE", "INT_LIT", "EOF")
        assertEquals(result, tokens(lexerForCode(code)))
    }

    @Test
    fun parseListOfExpression() {
        val code = "listOf<Int>(1, 2, 3)"
        val result = listOf(
            "LISTOF",
            "LANGLE",
            "INT",
            "RANGLE",
            "LPAREN",
            "INT_LIT",
            "COMMA",
            "INT_LIT",
            "COMMA",
            "INT_LIT",
            "RPAREN",
            "EOF"
        )
        assertEquals(result, tokens(lexerForCode(code)))
    }
    @Test
    fun parseArrayOfExpression(){
        val code = "arrayOf<Int>(1, 2, 3)"
        val result = listOf("ARRAYOF", "LANGLE", "INT", "RANGLE", "LPAREN", "INT_LIT", "COMMA", "INT_LIT", "COMMA", "INT_LIT", "RPAREN", "EOF")
        assertEquals(result, tokens(lexerForCode(code)))
    }

    @Test
    fun parseTextComposable() {
        val code = "Text(\"Hello world\")"
        val result = listOf("TEXT_COMPOSE", "LPAREN", "QUOTE_OPEN", "LineStrText", "QUOTE_CLOSE", "RPAREN", "EOF")
        assertEquals(result, tokens(lexerForCode(code)))
    }

    @Test
    fun parseTextComposableWithVariable() {
        val code = "Text(greet)"
        val result = listOf("TEXT_COMPOSE", "LPAREN", "ID", "RPAREN", "EOF")
        assertEquals(result, tokens(lexerForCode(code)))
    }

    @Test
    fun parseCustomColor() {
        val code = "Color(0xFF27AE60)"
        val result = listOf("COLOR", "LPAREN", "COLOR_LITERAL", "RPAREN", "EOF")
        assertEquals(result, tokens(lexerForCode(code)))
    }

    @Test
    fun parseBlueColor() {
        val code = "Color.Blue"
        val result = listOf("COLOR", "DOT", "COLOR_BLUE", "EOF")
        assertEquals(result, tokens(lexerForCode(code)))
    }

    @Test
    fun parseCustomFontWeight() {
        val code = "FontWeight(200)"
        val result = listOf("FONT_WEIGHT", "LPAREN", "INT_LIT", "RPAREN", "EOF")
        assertEquals(result, tokens(lexerForCode(code)))
    }

    @Test
    fun parseBoldFontWeight() {
        val code = "FontWeight.Bold"
        val result = listOf("FONT_WEIGHT", "DOT", "FONT_WEIGHT_BOLD", "EOF")
        assertEquals(result, tokens(lexerForCode(code)))
    }

    @Test
    fun parseTextWithParameter() {
        val code = "Text( \"Music\", color = Color.Blue)"
        val result = listOf(
            "TEXT_COMPOSE", "LPAREN", "QUOTE_OPEN", "LineStrText",
            "QUOTE_CLOSE",
            "COMMA",
            "COLOR_PARAM",
            "ASSIGN",
            "COLOR", "DOT", "COLOR_BLUE",
            "RPAREN", "EOF"
        )
        assertEquals(result, tokens(lexerForCode(code)))
    }

    @Test
    fun parseColumn() {
        val code = "Column(){ Text(\"a\")}"
        val result = listOf(
            "COLUMN_COMPOSE",
            "LPAREN",
            "RPAREN",
            "LCURL",
            "TEXT_COMPOSE",
            "LPAREN",
            "QUOTE_OPEN",
            "LineStrText",
            "QUOTE_CLOSE",
            "RPAREN",
            "RCURL",
            "EOF"
        )
        assertEquals(result, tokens(lexerForCode(code)))
    }

    @Test
    fun ParseColumnWithParameters() {
        val code = """
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.Start
            )
        """.trimIndent()
        val result = listOf(
            "COLUMN_COMPOSE",
            "LPAREN",
            "NL",
            "VERTICAL_ARRANGEMENT_PARAM",
            "ASSIGN",
            "ARRANGEMENT",
            "DOT",
            "SPACED_BY",
            "LPAREN",
            "INT_LIT",
            "DOT",
            "DP_SUFFIX",
            "RPAREN",
            "COMMA",
            "NL",
            "HORIZONTAL_ALIGNMENT_PARAM",
            "ASSIGN",
            "ALIGNMENT",
            "DOT",
            "START",
            "NL",
            "RPAREN",
            "EOF"
        )
        assertEquals(result, tokens(lexerForCode(code)))

    }

    @Test
    fun scrollModifier() {
        val code = "val mod = Modifier.verticalScroll(rememberScrollState())"
        val result = listOf(
            "VAL", "ID", "ASSIGN", "MODIFIER", "DOT", "VERTICAL_SCROLL_SUFFIX",
            "LPAREN", "REMEMBER_SCROLL", "LPAREN", "RPAREN", "RPAREN", "EOF"
        )
        assertEquals(result, tokens(lexerForCode(code)))

    }

    @Test
    fun ParseRowWithParameters() {
        val code = """
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Top
            )
        """.trimIndent()
        val result = listOf(
            "ROW_COMPOSE", "LPAREN", "NL", "MODIFIER_PARAM", "ASSIGN", "MODIFIER", "DOT",
            "HORIZONTAL_SCROLL_SUFFIX", "LPAREN", "REMEMBER_SCROLL", "LPAREN", "RPAREN",
            "RPAREN", "COMMA", "NL", "HORIZONTAL_ARRANGEMENT_PARAM", "ASSIGN", "ARRANGEMENT",
            "DOT", "SPACED_BY", "LPAREN", "INT_LIT", "DOT", "DP_SUFFIX", "RPAREN", "COMMA", "NL",
            "VERTICAL_ALIGNMENT_PARAM", "ASSIGN", "ALIGNMENT", "DOT", "TOP", "NL", "RPAREN", "EOF"
        )
        assertEquals(result, tokens(lexerForCode(code)))

    }


    @Test
    fun parseSpacer() {
        val code = "Spacer()"
        val result = listOf("SPACER_COMPOSE", "LPAREN", "RPAREN", "EOF")
        assertEquals(result, tokens(lexerForCode(code)))
    }

    @Test
    fun parseDivider() {
        val code = "Divider()"
        val result = listOf("DIVIDER_COMPOSE", "LPAREN", "RPAREN", "EOF")
        assertEquals(result, tokens(lexerForCode(code)))
    }

    @Test
    fun parseDividerWithThickness() {
        val code = "Divider(thickness = 8.dp)"
        val result =
            listOf("DIVIDER_COMPOSE", "LPAREN", "THICKNESS", "ASSIGN", "INT_LIT", "DOT", "DP_SUFFIX", "RPAREN", "EOF")
        assertEquals(result, tokens(lexerForCode(code)))
    }


    @Test
    fun parseSpacerWithSize() {
        val code = "Spacer().size(width: 54.0, height: 54.0)"
        val result = listOf(
            "SPACER_COMPOSE",
            "LPAREN",
            "RPAREN",
            "DOT",
            "SIZE",
            "LPAREN",
            "WIDTH",
            "COLON",
            "DOUBLE_LIT",
            "COMMA",
            "HEIGHT",
            "COLON",
            "DOUBLE_LIT",
            "RPAREN",
            "EOF"
        )
        assertEquals(result, tokens(lexerForCode(code)))
    }

    @Test

    fun parseBox(){
        val code = "Box( Modifier.zIndex(1.0) ) {}"
        val result = listOf("BOX","LPAREN","MODIFIER", "DOT", "ZINDEX", "LPAREN", "DOUBLE_LIT", "RPAREN", "RPAREN", "LCURL","RCURL", "EOF")
        assertEquals(result, tokens(lexerForCode(code)))
    }

    fun parseButtonComposable() {
        val code = "Button( onClick = {} ) { }"
        val result = listOf(
            "BUTTON_COMPOSABLE", "LPAREN", "ID", "ASSIGN",
            "LCURL", "RCURL", "RPAREN", "LCURL", "RCURL", "EOF"
        )
        assertEquals(result, tokens(lexerForCode(code)))
    }

    @Test
    fun parseClass() {
        val code = """
            class Person(
    val firstName: String,
    val lastName: String,
    var age: Int
): Parent {}
        """.trimIndent()
        val result = listOf(
            "CLASS", "ID", "LPAREN", "NL", "VAL", "ID", "COLON",
            "STRING", "COMMA", "NL", "VAL", "ID", "COLON",
            "STRING", "COMMA", "NL", "VAR", "ID", "COLON", "INT",
            "NL", "RPAREN", "COLON", "ID", "LCURL", "RCURL", "EOF"
        )
        assertEquals(result, tokens(lexerForCode(code)))
    }

    @Test
    fun parseImageComposable(){
        val code = "Image(painter = painterResource(id = R.drawable.dog))"
        val result = listOf("IMAGE_COMPOSE",
            "LPAREN",
            "PAINTER_PARAM",
            "ASSIGN",
            "PAINTER_RESOURCE",
            "LPAREN",
            "PAINTER_RESOURCE_PARAM",
            "ASSIGN",
            "ID",
            "DOT",
            "ID",
            "DOT",
            "ID",
            "RPAREN",
            "RPAREN",
            "EOF")
        assertEquals(result, tokens(lexerForCode(code)))
    }

    @Test
    fun parseCustomAspectRatio(){
        val code = "AspectRatio(16f)"
        val result = listOf("ASPECT_RATIO", "LPAREN", "FLOAT_LIT", "RPAREN", "EOF")
        assertEquals(result, tokens(lexerForCode(code)))
    }

    @Test
    fun parseImage(){
        val code = "Image(painter = painterResource(id = getResources().getIdentifier(\"nome-immagine-test\", \"drawable\", context.getPackageName())))"
        val result = listOf("IMAGE_COMPOSE",
                "LPAREN",
                "PAINTER_PARAM", "ASSIGN", "PAINTER_RESOURCE", "LPAREN", "PAINTER_RESOURCE_PARAM", "ASSIGN", "GET_RESOURCE", "LPAREN", "RPAREN", "DOT", "GET_IDENTIFIER", "LPAREN", "QUOTE_OPEN", "LineStrText", "QUOTE_CLOSE", "COMMA", "QUOTE_OPEN", "LineStrText", "QUOTE_CLOSE", "COMMA", "CONTEXT", "DOT", "GET_PACKAGENAME", "LPAREN", "RPAREN", "RPAREN", "RPAREN",
                "RPAREN",
                "EOF")
        assertEquals(result, tokens(lexerForCode(code)))
    }

    @Test
    fun parseImageWithModifierAndScale(){
        val code = "Image(" +
                "painter = painterResource(id = getResources().getIdentifier(\"nome-immagine-test\", \"drawable\", context.getPackageName()))," +
                "modifier = Modifier.fillMaxSize()," +
                "contentScale = ContentScale.FillWidth" +
                ")"
        val result = listOf("IMAGE_COMPOSE",
            "LPAREN",
            "PAINTER_PARAM", "ASSIGN", "PAINTER_RESOURCE", "LPAREN", "PAINTER_RESOURCE_PARAM", "ASSIGN", "GET_RESOURCE", "LPAREN", "RPAREN", "DOT", "GET_IDENTIFIER", "LPAREN", "QUOTE_OPEN", "LineStrText", "QUOTE_CLOSE", "COMMA", "QUOTE_OPEN", "LineStrText", "QUOTE_CLOSE", "COMMA", "CONTEXT", "DOT", "GET_PACKAGENAME", "LPAREN", "RPAREN", "RPAREN", "RPAREN","COMMA",
            "MODIFIER_PARAM", "ASSIGN", "MODIFIER", "DOT", "RESIZABLE", "LPAREN", "RPAREN","COMMA",
            "CONTENTSCALE_PARAM", "ASSIGN", "CONTENTSCALE", "DOT", "FILLWIDTH",
            "RPAREN",
            "EOF")
        assertEquals(result, tokens(lexerForCode(code)))
    }
}