package it.poliba.KSTranspiler

import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CharStreams
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import it.poliba.KSTranspiler.tools.ErrorHandler
import it.poliba.KSTranspiler.tools.ErrorHandler.attachErrorHandler
import java.util.*

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
    private fun lexerForResource(resourceName: String) = it.poliba.KSTranspiler.KotlinLexer (
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
        assertEquals(listOf("VAR", "ID", "ASSIGN", "INT_LIT", "EOF"),
            tokens(lexerForCode("var a = 1")))
    }

    @Test
    fun parseVarDeclarationAssignedAnStringLiteral() {
        assertEquals(listOf("VAR", "ID", "ASSIGN", "QUOTE_OPEN","LineStrText", "QUOTE_CLOSE",  "EOF"),
            tokens(lexerForCode("var a = \"test\"")))
    }
    @Test
    fun parseVarDeclarationIntType() {
        assertEquals(listOf("VAR", "ID", "COLON", "INT", "EOF"),
            tokens(lexerForCode("var a: Int")))
    }

    @Test
    fun parseVaLDeclarationIntType() {
        assertEquals(listOf("VAL", "ID", "COLON", "INT", "EOF"),
            tokens(lexerForCode("val a: Int")))
    }


    @Test
    fun parseValDeclarationAssignedAnIntegerLiteral() {
        assertEquals(listOf("VAL", "ID", "ASSIGN", "INT_LIT", "EOF"),
            tokens(lexerForCode("val a = 1")))
    }

    @Test
    fun parseVarDeclarationAssignedADecimalLiteral() {
        assertEquals(listOf("VAR", "ID", "ASSIGN", "DOUBLE_LIT", "EOF"),
            tokens(lexerForCode("var a = 1.23")))
    }

    @Test
    fun parseVarDeclarationAssignedASum() {
        assertEquals(listOf("VAR", "ID", "ASSIGN", "INT_LIT", "PLUS", "INT_LIT", "EOF"),
            tokens(lexerForCode("var a = 1 + 2")))
    }

    @Test
    fun parseMathematicalExpression() {
        assertEquals(listOf("INT_LIT", "PLUS", "ID", "ASTERISK", "INT_LIT", "DIVISION", "INT_LIT", "MINUS", "INT_LIT", "EOF"),
            tokens(lexerForCode("1 + a * 3 / 4 - 5")))
    }

    @Test
    fun parseMathematicalExpressionWithParenthesis() {
        assertEquals(listOf("INT_LIT", "PLUS", "LPAREN", "ID", "ASTERISK", "INT_LIT", "RPAREN", "MINUS", "DOUBLE_LIT", "EOF"),
            tokens(lexerForCode("1 + (a * 3) - 5.12")))
    }

    @Test
    fun parsePrintDeclarationLiteral() {
        assertEquals(listOf("PRINT", "LPAREN", "QUOTE_OPEN", "LineStrText","QUOTE_CLOSE","RPAREN", "EOF"),
            tokens(lexerForCode("print(\"ciao\")")))
    }

    @Test
    fun parseAssignmentDeclarationLiteral() {
        assertEquals(listOf("ID", "ASSIGN", "INT_LIT", "EOF"),
            tokens(lexerForCode("a = 5")))
    }

    @Test
    fun parseIfStatement() {
        assertEquals(listOf("IF", "LPAREN", "BOOL_LIT", "RPAREN", "LCURL", "PRINT","LPAREN", "QUOTE_OPEN", "LineStrText", "QUOTE_CLOSE", "RPAREN", "RCURL","EOF"),
            tokens(lexerForCode("if(true){" +
                    "   print(\"Hello world\")" +
                    "}")))
    }

    @Test
    fun parseWhileStatement() {
        assertEquals(listOf("WHILE", "LPAREN", "BOOL_LIT", "RPAREN", "LCURL", "PRINT","LPAREN", "QUOTE_OPEN", "LineStrText", "QUOTE_CLOSE", "RPAREN", "RCURL","EOF"),
            tokens(lexerForCode("while(true){" +
                    "   print(\"Hello world\")" +
                    "}")))
    }

    @Test
    fun parseWhileBreakStatement() {
        assertEquals(listOf("WHILE", "LPAREN", "BOOL_LIT", "RPAREN", "LCURL", "BREAK", "RCURL","EOF"),
            tokens(lexerForCode("while(true){" +
                    "   break" +
                    "}")))
    }

    @Test
    fun parseWhileContinueStatement() {
        assertEquals(listOf("WHILE", "LPAREN", "BOOL_LIT", "RPAREN", "LCURL", "CONTINUE", "RCURL","EOF"),
            tokens(lexerForCode("while(true){" +
                    "   continue" +
                    "}")))
    }

    @Test
    fun parseWhileReturnStatement() {
        assertEquals(listOf("WHILE", "LPAREN", "BOOL_LIT", "RPAREN", "LCURL", "RETURN", "INT_LIT", "RCURL","EOF"),
            tokens(lexerForCode("while(true){" +
                    "   return 1" +
                    "}")))
    }

    @Test
    fun parseFunction(){
        val code = "fun test(x: Int, y: Int)\t{\tprint(\"ciao\")}"
        val result = listOf("FUN","ID", "LPAREN", "ID", "COLON", "INT", "COMMA", "ID", "COLON", "INT", "RPAREN","LCURL", "PRINT","LPAREN", "QUOTE_OPEN", "LineStrText", "QUOTE_CLOSE", "RPAREN", "RCURL","EOF")
        assertEquals(result, tokens(lexerForCode(code)))
    }

    @Test
    fun parseFunctionExpression(){
        val code = "fun test(x: Int, y: Int) = 3"
        val result = listOf("FUN","ID", "LPAREN", "ID", "COLON", "INT", "COMMA", "ID", "COLON", "INT","RPAREN", "ASSIGN", "INT_LIT","EOF")
        assertEquals(result, tokens(lexerForCode(code)))
    }

    @Test
    fun parseFunctionExpressionWithReturn(){
        val code = "fun test(x: Int, y: Int): Int {return 5}"
        val result = listOf("FUN","ID", "LPAREN", "ID", "COLON", "INT", "COMMA", "ID", "COLON", "INT","RPAREN","COLON","INT", "LCURL","RETURN","INT_LIT","RCURL","EOF")
        assertEquals(result, tokens(lexerForCode(code)))
    }

    @Test
    fun parseRangeExpression(){
        val code = "1..42"
        val result = listOf("INT_LIT", "RANGE", "INT_LIT", "EOF")
        assertEquals(result, tokens(lexerForCode(code)))
    }

    @Test
    fun parseListOfExpression(){
        val code = "listOf<Int>(1, 2, 3)"
        val result = listOf("LISTOF", "LANGLE", "INT", "RANGLE", "LPAREN", "INT_LIT", "COMMA", "INT_LIT", "COMMA", "INT_LIT", "RPAREN", "EOF")
        assertEquals(result, tokens(lexerForCode(code)))
    }

    @Test
    fun parseArrayOfExpression(){
        val code = "arrayOf<Int>(1, 2, 3)"
        val result = listOf("ARRAYOF", "LANGLE", "INT", "RANGLE", "LPAREN", "INT_LIT", "COMMA", "INT_LIT", "COMMA", "INT_LIT", "RPAREN", "EOF")
        assertEquals(result, tokens(lexerForCode(code)))
    }

    @Test
    fun parseTextComposable(){
        val code = "Text(\"Hello world\")"
        val result = listOf("TEXT_COMPOSE", "LPAREN", "QUOTE_OPEN", "LineStrText", "QUOTE_CLOSE", "RPAREN", "EOF")
        assertEquals(result, tokens(lexerForCode(code)))
    }
    @Test
    fun parseTextComposableWithVariable(){
        val code = "Text(greet)"
        val result = listOf("TEXT_COMPOSE", "LPAREN", "ID", "RPAREN", "EOF")
        assertEquals(result, tokens(lexerForCode(code)))
    }

    @Test
    fun parseCustomColor(){
        val code = "Color(0xFF27AE60)"
        val result = listOf("COLOR", "LPAREN", "COLOR_LITERAL", "RPAREN", "EOF")
        assertEquals(result, tokens(lexerForCode(code)))
    }

    @Test
    fun parseBlueColor(){
        val code = "Color.Blue"
        val result = listOf("COLOR", "DOT", "COLOR_BLUE", "EOF")
        assertEquals(result, tokens(lexerForCode(code)))
    }

    @Test
    fun parseCustomFontWeight(){
        val code = "FontWeight(200)"
        val result = listOf("FONT_WEIGHT", "LPAREN", "INT_LIT", "RPAREN", "EOF")
        assertEquals(result, tokens(lexerForCode(code)))
    }

    @Test
    fun parseBoldFontWeight(){
        val code = "FontWeight.Bold"
        val result = listOf("FONT_WEIGHT", "DOT", "FONT_WEIGHT_BOLD", "EOF")
        assertEquals(result, tokens(lexerForCode(code)))
    }

    @Test
    fun parseTextWithParameter(){
        val code = "Text( \"Music\", color = Color.Blue)"
        val result = listOf("TEXT_COMPOSE", "LPAREN", "QUOTE_OPEN", "LineStrText",
            "QUOTE_CLOSE",
            "COMMA",
            "COLOR_PARAM",
            "ASSIGN",
            "COLOR", "DOT", "COLOR_BLUE",
             "RPAREN", "EOF")
        assertEquals(result, tokens(lexerForCode(code)))
    }
}