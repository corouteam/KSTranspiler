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
        var code = "val a = 5.0 / 7.0"
        val parseResult = KotlinParserFacade.parse(code).root!!
        var ast = parseResult.toAst()
        val expected = "let a:Double = 5.0 / 7.0"
        assertEquals(expected, ast.generateCode())
    }

    @Test
    fun convertAssignmentPropertyDefinition(){

        var code = "a = 5"
        val parseResult = KotlinParserFacade.parse(code).root!!
        var ast = parseResult.toAst()
        val expected = "a = 5"
        assertEquals(expected, ast.generateCode())
    }

    @Test
    fun convertPrintPropertyDefinition(){
        var code = "print(\"aa\")"
        val parseResult = KotlinParserFacade.parse(code).root!!
        var ast = parseResult.toAst()
        val expected = "print(\"aa\")"
        assertEquals(expected, ast.generateCode())
    }
    @Test
    fun convertIf(){
        val code = "if(true){ print(\"Is true \") })}"
        val result = "if(true){\n\tprint(\"Is true \")\n}"
        val parseResult = KotlinParserFacade.parse(code).root!!
        var ast = parseResult.toAst()
        assertEquals(result, ast.generateCode())

    }

    @Test
    fun convertIfElse(){
        val code = "if(true){ print(\"Is true \") }else{print(\"Is false\")}"
        val result = "if(true){\n\tprint(\"Is true \")\n}else{\n\tprint(\"Is false\")\n}"
        val parseResult = KotlinParserFacade.parse(code).root!!
        var ast = parseResult.toAst()
        assertEquals(result, ast.generateCode())

    }

    @Test
    fun convertIfElseIf(){
        val code = "if(true){ print(\"Is true \") }else if(false){print(\"Is false\")}"
        val result = "if(true){\n\tprint(\"Is true \")\n}else if(false){\n\tprint(\"Is false\")\n}"
        val parseResult = KotlinParserFacade.parse(code).root!!
        var ast = parseResult.toAst()
        assertEquals(result, ast.generateCode())
    }

    @Test
    fun convertIfElseIfElse(){
        val code = "if(true){ print(\"Is true \") }else if(false){print(\"Is false\")}else{print(\"never\")}}"
        val result = "if(true){\n\tprint(\"Is true \")\n}else if(false){\n\tprint(\"Is false\")\n}else{\n\tprint(\"never\")\n}"
        val parseResult = KotlinParserFacade.parse(code).root!!
        var ast = parseResult.toAst()
        assertEquals(result, ast.generateCode())
    }

}