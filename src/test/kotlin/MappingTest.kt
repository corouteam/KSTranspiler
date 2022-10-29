package it.poliba.KSTranspiler

import it.poliba.KSTranspiler.parsing.SandyParserFacade
import it.poliba.KSranspiler.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MappingTest {
    @Test
    fun mapSimpleFile() {
        val code = """var a = 1 + 2
                     |a = 7 * (2 / 3)""".trimMargin("|")
        val ast = SandyParserFacade.parse(code).root!!.toAst()
        val expectedAst = SandyFile(listOf(
            VarDeclaration("a", SumExpression(IntLit("1"), IntLit("2"))),
            Assignment("a", MultiplicationExpression(
                IntLit("7"),
                DivisionExpression(
                    IntLit("2"),
                    IntLit("3"))))))
        assertEquals(expectedAst, ast)
    }

    @Test
    fun mapCastInt() {
        val code = "a = 7 as Int"
        val ast = SandyParserFacade.parse(code).root!!.toAst()
        val expectedAst = SandyFile(listOf(Assignment("a", TypeConversion(IntLit("7"), IntType()))))
        assertEquals(expectedAst, ast)
    }
    @Test
    fun mapCastDecimal() {
        val code = "a = 7 as Decimal"
        val ast = SandyParserFacade.parse(code).root!!.toAst()
        val expectedAst = SandyFile(listOf(Assignment("a", TypeConversion(IntLit("7"), DecimalType()))))
        assertEquals(expectedAst, ast)
    }
    @Test
    fun mapPrint() {
        val code = "print(a)"
        val ast = SandyParserFacade.parse(code).root!!.toAst()
        val expectedAst = SandyFile(listOf(Print(VarReference("a"))))
        assertEquals(expectedAst, ast)
    }

    @Test
    fun mapSimpleVarAssignment(){
        val code = "var a = 3"
        val ast = SandyParserFacade.parse(code).root!!.toAst()
        val expectedAst = SandyFile(listOf(
            VarDeclaration("a", IntLit("3"))
        ))
        assertEquals(expectedAst, ast)
    }

    @Test
    fun mapSimpleLetAssignment(){
        val code = "let a = 3"
        val ast = SandyParserFacade.parse(code).root!!.toAst()
        val expectedAst = SandyFile(listOf(
            ReadOnlyVarDeclaration("a", IntLit("3"))
        ))
        assertEquals(expectedAst, ast)
    }
}