package it.poliba.KSTranspiler

import it.poliba.KSTranspiler.KotlinParser.BoolLiteralContext
import it.poliba.KSTranspiler.parsing.KotlinParserFacade
import it.poliba.KSranspiler.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MappingTest {

    @Test
    fun mapSimpleVarAssignmentString(){
        val code = "var a = \"hello\""
        val ast = KotlinParserFacade.parse(code).root!!.toAst()
        val expectedAst = KotlinFile(listOf(
            PropertyDeclaration("a", StringType(),StringLit("hello"), mutable = true)
        ))
        assertEquals(expectedAst, ast)
    }

    @Test
    fun mapSimpleVarAssignment(){
        val code = "var a = 3"
        val ast = KotlinParserFacade.parse(code).root!!.toAst()
        val expectedAst = KotlinFile(listOf(
            PropertyDeclaration("a", IntType(),IntLit("3"), mutable = true)
        ))
        assertEquals(expectedAst, ast)
    }

    @Test
    fun mapSimpleValAssignment(){
        val code = "val a = 3"
        val ast = KotlinParserFacade.parse(code).root!!.toAst()
        val expectedAst = KotlinFile(listOf(
            PropertyDeclaration("a", IntType(), IntLit("3"), mutable = false)
        ))
        assertEquals(expectedAst, ast)
    }

    @Test
    fun mapSimpleValAssignmentExplicitType(){
        val code = "val a: Int = 3"
        val ast = KotlinParserFacade.parse(code).root!!.toAst()
        val expectedAst = KotlinFile(listOf(
            PropertyDeclaration("a", IntType(), IntLit("3"), mutable = false)
        ))
        assertEquals(expectedAst, ast)
    }

    @Test
    fun mapPrint() {
        val code = "print('a')"
        val ast = KotlinParserFacade.parse(code).root!!.toAst()
        val expectedAst = KotlinFile(listOf(
            Print(VarReference("a", StringType() ))
        ))
        assertEquals(expectedAst, ast)
    }

    @Test
    fun mapAssignment() {
        val code = "a = 5"
        val ast = KotlinParserFacade.parse(code).root!!.toAst()
        val expectedAst = KotlinFile(listOf(
            Assignment("a", IntLit("5"))
        ))
        assertEquals(expectedAst, ast)
    }

    @Test
    fun mapIf() {
        val code = "if(true){" +
                "print(\"Hello world\")" +
                "}"
        val ast = KotlinParserFacade.parse(code).root!!.toAst()
        val expectedAst = KotlinFile(listOf(
            IfExpression(BoolLit("true"), Block(listOf(Print(StringLit("Hello world")))), elseBranch = null)
        ))
        assertEquals(expectedAst, ast)
    }

    @Test
    fun mapIfElse() {
        val code = "if(true){" +
                "print(\"Hello world\")" +
                "}else{" +
                "print(\"Bye world\")" +
                "}"
        val ast = KotlinParserFacade.parse(code).root!!.toAst()
        val expectedAst = KotlinFile(listOf(
            IfExpression(BoolLit("true"), Block(listOf(Print(StringLit("Hello world")))), elseBranch = Block(listOf(Print(StringLit("Bye world")))))
        ))
        assertEquals(expectedAst, ast)
    }

    @Test
    fun mapIfElseIf() {
        val code = "if(true){" +
                "print(\"Hello world\")" +
                "}else if(false){" +
                "print(\"Bye world\")" +
                "}"
        val ast = KotlinParserFacade.parse(code).root!!.toAst()
        val expectedAst = KotlinFile(listOf(
            IfExpression(BoolLit("true"), Block(listOf(Print(StringLit("Hello world")))), elseBranch = IfExpression(BoolLit("false"), Block(listOf(Print(StringLit("Bye world")))), null))))
        assertEquals(expectedAst, ast)
    }

    @Test
    fun mapIfElseNonBlock() {
        val code = "if(true)" +
                "print(\"Hello world\")" +
                "else " +
                "print(\"Bye world\")" +
                ""
        val ast = KotlinParserFacade.parse(code).root!!.toAst()
        val expectedAst = KotlinFile(listOf(
            IfExpression(BoolLit("true"),Print(StringLit("Hello world")), elseBranch = Print(StringLit("Bye world")))))
        assertEquals(expectedAst, ast)
    }
}