package it.poliba.KSTranspiler.TextComposable

import it.poliba.KSTranspiler.facade.KotlinParserFacade
import it.poliba.KSTranspiler.facade.SwiftParserFacadeScript
import it.poliba.KSTranspiler.generateCode
import it.poliba.KSTranspiler.generateKotlinCode
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * Test for TextComposableCall. This class aims to verify the correctness of
 * the conversion of Text Widget from SwiftUI to Compose.
 * It also verifies Colors and FontWeight since were introduced for this widget
 */
class TextSwiftToKotlin {
    @Test
    fun completeTextWidgetOnlyTextLiteral() {
        val code = "Text(\"Hello world\")\n.foregroundColor(Color.blue)\n.fontWeight(Font.Weight.bold)"
        val result = "Text(\"Hello world\", color = Color.Blue, fontWeight = FontWeight.Bold)"
        val parseResult = SwiftParserFacadeScript.parse(code).root!!
        assertEquals(result, parseResult.generateKotlinCode())
    }

    @Test
    fun completeTextWidgetOnlyText(){
        val code = "Text(\"Hello world\")"
        val result = "Text(\"Hello world\")"
        val parseResult = SwiftParserFacadeScript.parse(code).root!!
        assertEquals(result, parseResult.generateKotlinCode())
    }

    @Test
    fun completeTextWidgetOnlyTextVariable(){
        val code = """
            let name = "Ciao"
            Text(name)
        """.trimIndent()
        val result = """
            val name:String = "Ciao"
            Text(name)
        """.trimIndent()
        val parseResult = SwiftParserFacadeScript.parse(code).root!!
        assertEquals(result, parseResult.generateKotlinCode())
    }

    @Test
    fun completeTextWidgetWithColorVariable(){
        val result = """
            val myColor:Color = Color.Blue
            Text("Hello", color = myColor)
        """.trimIndent()
        val code = """
            let myColor = Color.blue
            Text("Hello")
            .foregroundColor(myColor)
        """.trimIndent()
        val parseResult = SwiftParserFacadeScript.parse(code).root!!
        assertEquals(result, parseResult.generateKotlinCode())
    }


    @Test
    fun completeTextWidgetOnlyColorLiteral(){
        val result = """
            Text("Hello", color = Color.Blue)
        """.trimIndent()
        val code = """
            Text("Hello")
            .foregroundColor(Color.blue)
        """.trimIndent()
        val parseResult = SwiftParserFacadeScript.parse(code).root!!
        assertEquals(result, parseResult.generateKotlinCode())
    }

    @Test
    fun completeTextWidgetWithFontWeight(){
        val result = """
            val myWeight:FontWeight = FontWeight.Bold
            Text("Hello", fontWeight = myWeight)
        """.trimIndent()
        val code = """
            let myWeight: Font.Weight = Font.Weight.bold
            Text("Hello")
            .fontWeight(myWeight)
        """.trimIndent()
        val parseResult = SwiftParserFacadeScript.parse(code).root!!
        assertEquals(result, parseResult.generateKotlinCode())
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

    //TODO: @Lops aggiungi qui i test sui colori e font weight, basta definire delle variabili con i singoli valori inseriti
}