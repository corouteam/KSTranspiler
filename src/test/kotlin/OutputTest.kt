package it.poliba.KSTranspiler

import it.poliba.KSTranspiler.parsing.KotlinParserFacade
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class OutputTest {

    @Test
    fun convertVarPropertyDefinition(){
        var code = "val a = 5"
        val parseResult = KotlinParserFacade.parse(code).root!!
        var ast = parseResult.toAst()
        val expected = "let a:Int = 5"
        assertEquals(expected, ast.generateCode())
    }

    @Test
    fun convertDoublePropertyDefinition(){
        var code = "val a = 5.9"
        val parseResult = KotlinParserFacade.parse(code).root!!
        var ast = parseResult.toAst()
        val expected = "let a:Double = 5.9"
        assertEquals(expected, ast.generateCode())
    }

    @Test
    fun convertSumPropertyDefinition(){
        var code = "val a = 5 + 7"
        val parseResult = KotlinParserFacade.parse(code).root!!
        var ast = parseResult.toAst()
        val expected = "let a:Int = 5 + 7"
        assertEquals(expected, ast.generateCode())
    }

    @Test
    fun convertMinusPropertyDefinition(){
        var code = "val a = 5 - 7"
        val parseResult = KotlinParserFacade.parse(code).root!!
        var ast = parseResult.toAst()
        val expected = "let a:Int = 5 - 7"
        assertEquals(expected, ast.generateCode())
    }

    @Test
    fun convertDivisionPropertyDefinition(){
        var code = "val a = 5 / 7"
        val parseResult = KotlinParserFacade.parse(code).root!!
        var ast = parseResult.toAst()
        val expected = "let a:Double = 5 / 7"
        assertEquals(expected, ast.generateCode())
    }

    @Test
    fun convertPrintPropertyDefinition(){
        var code = "print('test')"
        val parseResult = KotlinParserFacade.parse(code).root!!
        var ast = parseResult.toAst()
        val expected = "print('test')"
        assertEquals(expected, ast.generateCode())
    }
}