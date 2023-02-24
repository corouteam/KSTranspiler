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
            Found String in a list of Int
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

    @Test
    fun `variable not declared throws error`() {
        var code = """
            fun test() {
                print(a)
            }
        """.trimIndent()
        val parseResult = KotlinParserFacade.parse(code)

        assert(parseResult.errors.isNotEmpty())
        assertEquals("A variable named 'a' is used but never declared", parseResult.errors.first().message)
    }

    @Test
    fun `variable declared in different scope does not throw error`() {
        var code = """
            val a = "Hello world!"
            
            fun test() {
                print(a)
            }
        """.trimIndent()
        val parseResult = KotlinParserFacade.parse(code)

        assert(parseResult.errors.isEmpty())
    }

    @Test
    fun `var type mismatch is reported`() {
        var code = """
            val a: Int = "a"
        """.trimIndent()
        val parseResult = KotlinParserFacade.parse(code)

        assert(parseResult.errors.isNotEmpty())
        assertEquals("""
            Type mismatch (String assigned to a variable of type Int).
        """.trimIndent(), parseResult.errors.first().message)
    }

    @Test
    fun `function return required is reported`() {
        var code = """
            fun testReturn(): String {
                print("hello")
            }
        """.trimIndent()
        val parseResult = KotlinParserFacade.parse(code)

        assert(parseResult.errors.isNotEmpty())
        assertEquals("""
            A return expression is required for the function testReturn.
        """.trimIndent(), parseResult.errors.first().message)
    }

    @Test
    fun `function return with different type is reported`() {
        var code = """
            fun testReturnType(): String {
                print("hello")
                return true
            }
        """.trimIndent()
        val parseResult = KotlinParserFacade.parse(code)

        assert(parseResult.errors.isNotEmpty())
        assertEquals("""
            The return type Boolean does not
            conform to the expected type String
            of the function testReturnType.
        """.trimIndent(), parseResult.errors.first().message)
    }
}