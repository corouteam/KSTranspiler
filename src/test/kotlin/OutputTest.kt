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
}