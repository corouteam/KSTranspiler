package it.poliba.KSTranspiler

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
}