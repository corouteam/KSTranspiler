package it.poliba.KSTranspiler.DividerComposable

import it.poliba.KSTranspiler.facade.KotlinParserFacade
import it.poliba.KSTranspiler.generateCode
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * Test for Divider. This class aims to verify the correctness of
 * the conversion of the Divider Widget from Compose to SwiftUI.
 */
class DividerKotlinToSwift {
    @Test
    fun convertComplete() {
        val code = """
            Divider(color = Color.Blue, thickness = 8.dp)
            """.trimIndent()
        val result = """
Divider()
	.overlay(Color.blue)
	 .frame(height: CGFloat(8))
        """.trimIndent()

        val parseResult = KotlinParserFacade.parse(code)
        assertEquals(result, parseResult.root!!.generateCode())
    }

    @Test
    fun convertOnlyColor() {
        val code = """
            Divider(color = Color.Blue)
            """.trimIndent()
        val result = """
Divider()
	.overlay(Color.blue)
        """.trimIndent()

        val parseResult = KotlinParserFacade.parse(code)
        assertEquals(result, parseResult.root!!.generateCode())
    }

    @Test
    fun convertOnlyColorVariable() {
        val code = """
            var myColor = Color.Blue
            Divider(color = myColor)
            """.trimIndent()
        val result = """
var myColor:Color = Color.blue
Divider()
	.overlay(myColor)
        """.trimIndent()

        val parseResult = KotlinParserFacade.parse(code)
        assertEquals(result, parseResult.root!!.generateCode())
    }

    @Test
    fun convertOnlyColorVariableFailsIfWrongType() {
        val code = """
            var myColor = "Ciao"
            Divider(color = myColor)
            """.trimIndent()

        val parseResult = KotlinParserFacade.parse(code)
        assertEquals("Expecting a color", parseResult.errors.first().message)
    }

    @Test
    fun convertOnlyThickness() {
        val code = """
            Divider(thickness = 8.dp)
            """.trimIndent()
        val result = """
Divider()
	.frame(height: CGFloat(8))
        """.trimIndent()

        val parseResult = KotlinParserFacade.parse(code)
        assertEquals(result, parseResult.root!!.generateCode())
    }

    @Test
    fun convertOnlyThicknessVariable() {
        val code = """
            var dividerThickness = 8.dp
            Divider(thickness = dividerThickness)
            """.trimIndent()
        val result = """
var dividerThickness:CGFloat = CGFloat(8)
Divider()
	.frame(height: dividerThickness)
        """.trimIndent()

        val parseResult = KotlinParserFacade.parse(code)
        assertEquals(result, parseResult.root!!.generateCode())
    }
}