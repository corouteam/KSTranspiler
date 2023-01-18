package it.poliba.KSTranspiler

import it.poliba.KSTranspiler.facade.KotlinParserFacade
import it.poliba.KSTranspiler.facade.KotlinParserFacadeScript
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ValidationTest {

    @Test
    fun `array matches type`() {
        var code = "val list = listOf<Int>(1, 2, \"a\")"
        val parseResult = KotlinParserFacade.parse(code)

        assert(parseResult.errors.isNotEmpty())
        assertEquals(parseResult.errors.first().message, """
            List can't contain different types.
            Found it.poliba.KSTranspiler.StringType in a list of it.poliba.KSTranspiler.IntType
        """.trimIndent())
    }

    @Test
    fun `variable not duplicate in function`() {
        var code = """
            val a = 1
            
            fun test() {
                val b = 2
                val b = 3
            }
        """.trimIndent()
        val parseResult = KotlinParserFacade.parse(code)

        assert(parseResult.errors.isNotEmpty())
        assertEquals(parseResult.errors.first().message,"A variable named 'b' has been already declared")
    }

    @Test
    fun `variable in different scope can have same name`() {
        var code = """
            val a = 1
            
            fun test() {
                val a = 3
                val b = 2
            }
        """.trimIndent()
        val parseResult = KotlinParserFacade.parse(code)

        assert(parseResult.errors.isEmpty())
    }

    @Test
    fun `global variable not duplicate`() {
        var code = """
            val a = 1
            val a = 2
        """.trimIndent()
        val parseResult = KotlinParserFacadeScript.parse(code)

        assert(parseResult.errors.isNotEmpty())
        assertEquals("A variable named 'a' has been already declared", parseResult.errors.first().message)
    }
}