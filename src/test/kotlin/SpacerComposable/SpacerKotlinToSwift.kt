package it.poliba.KSTranspiler.SpacerComposable

import it.poliba.KSTranspiler.facade.KotlinParserFacade
import it.poliba.KSTranspiler.generateCode
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * Test for Spacer. This class aims to verify the correctness of
 * the conversion of the spacer Widget from Compose to SwiftUI.
 */
class SpacerKotlinToSwift {
    @Test
    fun convertComplete() {
        val code = """
            Spacer(modifier = Modifier.width(54.dp).height(54.dp))
            """.trimIndent()
        val result = """
Spacer()
	.frame(width: CGFloat(54), height: CGFloat(54))
        """.trimIndent()

        val parseResult = KotlinParserFacade.parse(code)
        assertEquals(result, parseResult.root!!.generateCode())
    }

    @Test
    fun convertOnlyHeight() {
        val code = """
            Spacer(modifier = Modifier.height(54.dp))
            """.trimIndent()
        val result = """
Spacer()
	.frame(height: CGFloat(54))
        """.trimIndent()

        val parseResult = KotlinParserFacade.parse(code)
        assertEquals(result, parseResult.root!!.generateCode())
    }

    @Test
    fun convertOnlyWidth() {
        val code = """
            Spacer(modifier = Modifier.width(54.dp))
            """.trimIndent()
        val result = """
Spacer()
	.frame(width: CGFloat(54))
        """.trimIndent()

        val parseResult = KotlinParserFacade.parse(code)
        assertEquals(result, parseResult.root!!.generateCode())
    }

    @Test
    fun convertCompleteVariable() {
        val code = """
            var sHeight = 54.dp
            var sWidth = 55.dp
            Spacer(modifier = Modifier.width(sWidth).height(sHeight))
            """.trimIndent()
        val result = """
var sHeight:CGFloat = CGFloat(54)
var sWidth:CGFloat = CGFloat(55)
Spacer()
	.frame(width: sWidth, height: sHeight)
        """.trimIndent()

        val parseResult = KotlinParserFacade.parse(code)
        assertEquals(result, parseResult.root!!.generateCode())
    }

}