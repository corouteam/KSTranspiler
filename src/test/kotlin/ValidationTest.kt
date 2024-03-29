package it.poliba.KSTranspiler

import it.poliba.KSTranspiler.facade.KotlinParserFacade
import it.poliba.KSTranspiler.facade.SwiftParserFacade
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
        val parseResult = KotlinParserFacade.parse(code)

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
    fun `variable not declared but in function argument doesn't throws error`() {
        var code = """
            fun test(message: String) {
                print(message)
            }
        """.trimIndent()
        val parseResult = KotlinParserFacade.parse(code)

        assert(parseResult.errors.isEmpty())
    }

    @Test
    fun `variable not declared in function argument throws error`() {
        var code = """
            fun test(message: String) {
                print(message2)
            }
        """.trimIndent()
        val parseResult = KotlinParserFacade.parse(code)

        assert(parseResult.errors.isNotEmpty())
        assertEquals("A variable named 'message2' is used but never declared", parseResult.errors.first().message)
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
    fun `variable not declared in script throws error`() {
        var code = """
            print(a)
        """.trimIndent()
        val parseResult = KotlinParserFacade.parse(code)

        assert(parseResult.errors.isNotEmpty())
        assertEquals("A variable named 'a' is used but never declared", parseResult.errors.first().message)
    }

    @Test
    fun `final variable re-assigned in script throws error`() {
        var code = """
            val a = 2
            a = 4
        """.trimIndent()
        val parseResult = KotlinParserFacade.parse(code)

        assert(parseResult.errors.isNotEmpty())
        assertEquals("Final variable a can not be reassigned.", parseResult.errors.first().message)
    }


    @Test
    fun `duplicate variables in scripts throws error`() {
        var code = """
            val a = 2
            var a = 3
        """.trimIndent()
        val parseResult = KotlinParserFacade.parse(code)

        assert(parseResult.errors.isNotEmpty())
        assertEquals("A variable named 'a' has been already declared", parseResult.errors.first().message)
    }

    @Test
    fun `if condition must be boolean`() {
        var code = """
            fun main(){
                if (42) {
                    print("hello")
                }
            }
        """.trimIndent()
        val parseResult = KotlinParserFacade.parse(code)

        assert(parseResult.errors.isNotEmpty())
        assertEquals("If condition must be a boolean expression.", parseResult.errors.first().message)
    }

    @Test
    fun `global val can not be reassigned`() {
        var code = """
            val a = 1
            
            fun main(){
                a = 2
            }
        """.trimIndent()
        val parseResult = KotlinParserFacade.parse(code)

        assert(parseResult.errors.isNotEmpty())
        assertEquals("""
            Final variable a can not be reassigned.
        """.trimIndent(), parseResult.errors.first().message)
    }

    @Test
    fun `val can not be reassigned`() {
        var code = """
            
            fun main(){
                val a = 1
                a = 2
            }
        """.trimIndent()
        val parseResult = KotlinParserFacade.parse(code)

        assert(parseResult.errors.isNotEmpty())
        assertEquals("""
            Final variable a can not be reassigned.
        """.trimIndent(), parseResult.errors.first().message)
    }

    @Test
    fun `global var type mismatch between different declaration and assignment is reported`() {
        var code = """
            var a = 1
            
            fun main() {
                a = "hello"
            }
        """.trimIndent()
        val parseResult = KotlinParserFacade.parse(code)

        assert(parseResult.errors.isNotEmpty())
        assertEquals("""
            Type mismatch (String assigned to a variable of type Int).
        """.trimIndent(), parseResult.errors.first().message)
    }

    @Test
    fun `var type mismatch between different declaration and assignment is reported`() {
        var code = """            
            fun main() {
                var a = 1
                a = "hello"
            }
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

    @Test
    fun `function return reference with different type is reported`() {
        var code = """
            val a: Boolean = true

            fun testReturnType(): String {

                print("hello")
                return a
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

    @Test
    fun `composable function always return a composable`() {
        var code = """
            @Composable
            fun testComposable(){
                print("hello without composable")
            }
        """.trimIndent()
        val parseResult = KotlinParserFacade.parse(code)

        assert(parseResult.errors.isNotEmpty())
        assertEquals("""
            Function testComposable is expected to declare a Composable
        """.trimIndent(), parseResult.errors.first().message)
    }

    @Test
    fun `SwiftUi funciton always returns a widget`(){
        var code = """
            struct test: View{
                var x: Int
                var y: Int
            }
        """.trimIndent()
        val parseResult = SwiftParserFacade.parse(code)

        assert(parseResult.errors.isNotEmpty())
        assertEquals("""
            Struct declaration is required to return a widget
        """.trimIndent(), parseResult.errors.first().message)
    }

    @Test
    fun `data class must have only property parameters`(){
        val code = """
        data class Person(
        firstName: String,
        val lastName: String
        ): Address, Jks {
            init {
                print("Hello")
            }
        }""".trimMargin()

        val parseResult = KotlinParserFacade.parse(code)

        assert(parseResult.errors.isNotEmpty())
        assertEquals("""
            Data class must have only property parameters
        """.trimIndent(), parseResult.errors.first().message)
    }
}