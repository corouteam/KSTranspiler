package it.poliba.KSTranspiler

import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CharStreams
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*


import it.poliba.KSTranspiler.SandyLexer

class LexerTest {
   fun lexerForCode(code: String) = it.poliba.KSTranspiler.SandyLexer(CharStreams.fromString(code))
    fun lexerForResource(resourceName: String) = it.poliba.KSTranspiler.SandyLexer(ANTLRInputStream(this.javaClass.getResourceAsStream("/${resourceName}.sandy")))
    fun tokens(lexer: SandyLexer): List<String> {
        val tokens = LinkedList<String>()
        do {
            val t = lexer.nextToken()
            when (t.type) {
                -1 -> tokens.add("EOF")
                else -> if (t.type != SandyLexer.WS) tokens.add(lexer.ruleNames[t.type - 1])
            }
        } while (t.type != -1)
        return tokens
    }

    @Test
    fun parseVarDeclarationAssignedAnIntegerLiteral() {
        assertEquals(listOf("VAR", "ID", "ASSIGN", "INTLIT", "EOF"),
            tokens(lexerForCode("var a = 1")))
    }

    @Test
    fun parseLetDeclarationAssignedAnIntegerLiteral() {
        assertEquals(listOf("LET", "ID", "ASSIGN", "INTLIT", "EOF"),
            tokens(lexerForCode("let a = 1")))
    }

    @Test
    fun parseVarDeclarationAssignedADecimalLiteral() {
        assertEquals(listOf("VAR", "ID", "ASSIGN", "DECLIT", "EOF"),
            tokens(lexerForCode("var a = 1.23")))
    }

    @Test
    fun parseVarDeclarationAssignedASum() {
        assertEquals(listOf("VAR", "ID", "ASSIGN", "INTLIT", "PLUS", "INTLIT", "EOF"),
            tokens(lexerForCode("var a = 1 + 2")))
    }

    @Test
    fun parseMathematicalExpression() {
        assertEquals(listOf("INTLIT", "PLUS", "ID", "ASTERISK", "INTLIT", "DIVISION", "INTLIT", "MINUS", "INTLIT", "EOF"),
            tokens(lexerForCode("1 + a * 3 / 4 - 5")))
    }

    @Test
    fun parseMathematicalExpressionWithParenthesis() {
        assertEquals(listOf("INTLIT", "PLUS", "LPAREN", "ID", "ASTERISK", "INTLIT", "RPAREN", "MINUS", "DECLIT", "EOF"),
            tokens(lexerForCode("1 + (a * 3) - 5.12")))
    }

    @Test
    fun test(){
        assertEquals(42, 42)
    }

}