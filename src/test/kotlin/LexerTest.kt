package it.poliba.KSTranspiler

import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CharStreams
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*

class LexerTest {
   fun lexerForCode(code: String) = it.poliba.KSTranspiler.KotlinLexer(CharStreams.fromString(code))
    fun lexerForResource(resourceName: String) = it.poliba.KSTranspiler.KotlinLexer(ANTLRInputStream(this.javaClass.getResourceAsStream("/${resourceName}.Kotlin")))
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

}