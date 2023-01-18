package it.poliba.KSTranspiler

import it.poliba.KSTranspiler.facade.KotlinParserFacade
import it.poliba.KSTranspiler.facade.KotlinParserFacadeScript
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ValidationTest {

    @Test
    fun arrayMatchesType() {
        var code = "val list = listOf<Int>(1, 2, \"a\")"
        val parseResult = KotlinParserFacade.parse(code)

        assert(parseResult.errors.isNotEmpty())
        assertEquals(parseResult.errors.first().message, """
            List can't contain different types.
            Found it.poliba.KSTranspiler.StringType in a list of it.poliba.KSTranspiler.IntType
        """.trimIndent())
    }

    @Test
    fun variableDeclaredOneTime() {
        var code = """
            val a = 1
            
            fun test() {
                val b = 2
            }
        """.trimIndent()
        val parseResult = KotlinParserFacadeScript.parse(code)

        assert(parseResult.errors.isNotEmpty())
        assertEquals(parseResult.errors.first().message, """
            
            
        """.trimIndent())
    }
}