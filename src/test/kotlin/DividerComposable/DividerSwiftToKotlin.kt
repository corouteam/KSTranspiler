package it.poliba.KSTranspiler.DividerComposable

import it.poliba.KSTranspiler.facade.SwiftParserFacade
import it.poliba.KSTranspiler.facade.SwiftParserFacadeScript
import it.poliba.KSTranspiler.generateCode
import it.poliba.KSTranspiler.generateKotlinCode
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * Test for Divider. This class aims to verify the correctness of
 * the conversion of the Divider Widget from Compose to SwiftUI.
 */

class DividerSwiftToKotlin {
    @Test
    fun convertComplete() {
        val code = """
Divider().overlay(Color.blue).frame(height: CGFloat(8))
        """.trimIndent()

        val result = """
            Divider(color = Color.Blue, thickness = 8.dp)
            """.trimIndent()

        val parseResult = SwiftParserFacadeScript.parse(code)
        assertEquals(result, parseResult.root!!.generateKotlinCode())
    }

    @Test
    fun convertOnlyColor() {
        val code = """
Divider()
	.overlay(Color.blue)
        """.trimIndent()

        val result = """
            Divider(color = Color.Blue)
            """.trimIndent()


        val parseResult = SwiftParserFacadeScript.parse(code)
        assertEquals(result, parseResult.root!!.generateKotlinCode())
    }

    @Test
    fun convertOnlyColorVariable() {

        val code = """
var myColor:Color = Color.blue
Divider()
	.overlay(myColor)
        """.trimIndent()

        val result = """
            var myColor:Color = Color.Blue
            Divider(color = myColor)
            """.trimIndent()

        val parseResult = SwiftParserFacadeScript.parse(code)
        assertEquals(result, parseResult.root!!.generateKotlinCode())
    }

    @Test
    fun convertOnlyColorVariableFailsIfWrongType() {
        val code = """
var myColor:Color = "Color.blue"
Divider()
	.overlay(myColor)
            """.trimIndent()

        val parseResult = SwiftParserFacadeScript.parse(code)
        assertEquals("Type mismatch (String assigned to a variable of type Color).", parseResult.errors.first().message)
    }

    @Test
    fun convertOnlyThickness() {
        val code = """
Divider()
	.frame(height: CGFloat(8))
        """.trimIndent()
        val result = """
            Divider(thickness = 8.dp)
            """.trimIndent()


        val parseResult = SwiftParserFacadeScript.parse(code)
        assertEquals(result, parseResult.root!!.generateKotlinCode())
    }

    @Test
    fun convertOnlyThicknessVariable() {
        val code = """
var dividerThickness:CGFloat = CGFloat(8)
Divider()
	.frame(height: dividerThickness)
        """.trimIndent()

        val result = """
            var dividerThickness:Dp = 8.dp
            Divider(thickness = dividerThickness)
            """.trimIndent()


        val parseResult = SwiftParserFacadeScript.parse(code)
        assertEquals(result, parseResult.root!!.generateKotlinCode())
    }

}