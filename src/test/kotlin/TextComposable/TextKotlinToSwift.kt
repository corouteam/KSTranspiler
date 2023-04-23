package it.poliba.KSTranspiler.TextComposable

import it.poliba.KSTranspiler.facade.KotlinParserFacade
import it.poliba.KSTranspiler.generateCode
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * Test for TextComposableCall. This class aims to verify the correctness of
 * the conversion of Text Widget from Compose to SwiftUI.
 * It also verifies Colors and FontWeight since were introduced for this widget
 */
class TextKotlinToSwift {
    @Test
    fun convertTextWidgetComplete(){
        val code = "Text(\"Hello world\", color = Color.Blue, fontWeight = FontWeight.Bold)"
        val result = "Text(\"Hello world\")\n.foregroundColor(Color.blue)\n.fontWeight(Font.Weight.bold)"
        val parseResult = KotlinParserFacade.parse(code).root!!
        assertEquals(result, parseResult.generateCode())
    }
    @Test
    fun completeTextWidgetOnlyTextLiteral(){
        val code = "Text(\"Hello world\")"
        val result = "Text(\"Hello world\")"
        val parseResult = KotlinParserFacade.parse(code).root!!
        assertEquals(result, parseResult.generateCode())
    }
    @Test
    fun completeTextWidgetOnlyTextVariable(){
        val code = """
            val name = "Ciao"
            Text(name)
        """.trimIndent()
        val result = """
            let name:String = "Ciao"
            Text(name)
        """.trimIndent()
        val parseResult = KotlinParserFacade.parse(code).root!!
        assertEquals(result, parseResult.generateCode())
    }

    @Test
    fun completeTextWidgetWithColorVariable(){
        val code = """
            val myColor = Color.Blue
            Text("Hello", color = myColor)
        """.trimIndent()
        val result = """
            let myColor:Color = Color.blue
            Text("Hello")
            .foregroundColor(myColor)
        """.trimIndent()
        val parseResult = KotlinParserFacade.parse(code).root!!
        assertEquals(result, parseResult.generateCode())
    }

    @Test
    fun completeTextWidgetOnlyColorLiteral(){
        val code = """
            Text("Hello", color = Color.Blue)
        """.trimIndent()
        val result = """
            Text("Hello")
            .foregroundColor(Color.blue)
        """.trimIndent()
        val parseResult = KotlinParserFacade.parse(code).root!!
        assertEquals(result, parseResult.generateCode())
    }

    @Test
    fun completeTextWidgetWithFontWeight(){
        val code = """
            val myWeight = FontWeight.Bold
            Text("Hello", fontWeight = myWeight)
        """.trimIndent()
        val result = """
            let myWeight:Font.Weight = Font.Weight.bold
            Text("Hello")
            .fontWeight(myWeight)
        """.trimIndent()
        val parseResult = KotlinParserFacade.parse(code).root!!
        assertEquals(result, parseResult.generateCode())
    }

    @Test
    fun completeTextWidgetWithFontWeightLiteral(){
        val code = """
            Text("Hello world", fontWeight = FontWeight.Bold)
        """.trimIndent()
        val result = """
            Text("Hello world")
            .fontWeight(Font.Weight.bold)
        """.trimIndent()
        val parseResult = KotlinParserFacade.parse(code).root!!
        assertEquals(result, parseResult.generateCode())
    }

    @Test
    fun malformedWeightRaisesError(){
        val code = """
            Text("Hello world", fontWeight = FontWeight.B)
        """.trimIndent()

        val parseResult = KotlinParserFacade.parse(code)
        var error = parseResult.errors.first()
        var message = error.message
        assertEquals("Unrecognized symbol B", message)
        assertEquals(1, error.position?.start?.line)
        assertEquals(44, error.position?.start?.column)
    }

    //TODO: @Lops aggiungi qui i test sui colori e font weight, basta definire delle variabili con i singoli valori inseriti

}