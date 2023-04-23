package it.poliba.KSTranspiler.TextComposable

import it.poliba.KSTranspiler.facade.KotlinParserFacade
import it.poliba.KSTranspiler.generateCode
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/**
 * Test for Row. This class aims to verify the correctness of
 * the conversion of Column Widget from Compose to SwiftUI.
 * It also verifies HorizontalArrangement and VerticalAlignment since were introduced for this widget
 */
class RowKotlinToSwift {
    @Test
    fun convertRow() {
        val code = """
            Row(
             horizontalArrangement = Arrangement.spacedBy(10.dp),
             verticalAlignment = Alignment.Top)
            """.trimIndent()
        val result = """HStack(
	alignment: VerticalAlignment.top,
	spacing: CGFloat(10)){

}""".trimIndent()

        val parseResult = KotlinParserFacade.parse(code)
        assertEquals(result, parseResult.root!!.generateCode())
    }

    @Test
    fun convertRowOnlyArrangement() {
        val code = """
            Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
            )
            """.trimIndent()
        val result = """
         HStack(
         	spacing: CGFloat(10)){
   
         }
        """.trimIndent()

        val parseResult = KotlinParserFacade.parse(code)
        assertEquals(result, parseResult.root!!.generateCode())
    }

    @Test
    fun convertRowOnlyArrangementVariable() {
        val code = """
            var cSpacing = 10.dp
            Row(
            horizontalArrangement = Arrangement.spacedBy(cSpacing)
            )
            """.trimIndent()
        val result = """
         var cSpacing:CGFloat = CGFloat(10)
         HStack(
         	spacing: cSpacing){
   
         }
        """.trimIndent()

        val parseResult = KotlinParserFacade.parse(code)
        assertEquals(result, parseResult.root!!.generateCode())
    }

    @Test
    fun convertRowOnlyVerticalAlignment() {
        val code = """
            Row(
             verticalAlignment = Alignment.Top)
            """.trimIndent()
        val result = """
HStack(
	alignment: VerticalAlignment.top){

}""".trimIndent()

        val parseResult = KotlinParserFacade.parse(code)
        assertEquals(result, parseResult.root!!.generateCode())
    }

    @Test
    fun convertRowOnlyAlignmentVariable() {
        val code = """
                var cAlignment = Alignment.Top
                Row(
                 verticalAlignment = cAlignment)
                """.trimIndent()
        val result = """
    var cAlignment:VerticalAlignment = VerticalAlignment.top
    HStack(
    	alignment: cAlignment){
    
    }""".trimIndent()

        val parseResult = KotlinParserFacade.parse(code)
        assertEquals(result, parseResult.root!!.generateCode())
    }

    //TODO: @Lops, per far passare questo test occorre aggiungere il tipo al parser/*
    /*@Test
    fun wrongAlignmentThrowsError() {
        val code =  "var cAlignment: Alignment.Horizontal = Alignment.Start"
        val result = "var cAlignment:HorizontalAlignment = HorizontalAlignment.leading"

        val parseResult = KotlinParserFacade.parse(code)

        assertEquals(result, parseResult.root!!.generateCode())
    }*/

    @Test
    fun mapCGFloat() {
        val code =  "var dimens = 4.dp"
        val result = "var dimens:CGFloat = CGFloat(4)"
        val parseResult = KotlinParserFacade.parse(code)
        assertEquals(result, parseResult.root!!.generateCode())
    }


    @Test
    fun convertRowOnlyAlignmentVariableFailsIfWrongType() {
        val code = """
                var cAlignment = "Ciao"
                Row(
                 verticalAlignment = cAlignment)
                """.trimIndent()

        val parseResult = KotlinParserFacade.parse(code)
        assertEquals("Row vertical alignment requires a VerticalAlignmentType", parseResult.errors.first().message)
    }

    @Test
    fun convertRowOnlyArrangementVariableFailsIfWrongType() {
        val code = """
            var cSpacing = "Ciao"
            Row(
            horizontalArrangement = Arrangement.spacedBy(cSpacing)
            )
            """.trimIndent()

        val parseResult = KotlinParserFacade.parse(code)

        assertEquals("Row spacing requires a dp literal", parseResult.errors.first().message)
    }

    @Test
    fun convertColumnWithChildren() {
        val code = """
            Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.Top){
                Text("Ciao")
                Text("Linea2")
                Text("Linea3")
             }
            """.trimIndent()
        val result = """
         HStack(
         	alignment: VerticalAlignment.top,
         	spacing: CGFloat(10)){
         	Text("Ciao")
         	Text("Linea2")
         	Text("Linea3")
         }
        """.trimIndent()

        val parseResult = KotlinParserFacade.parse(code)
        assertEquals(result, parseResult.root!!.generateCode())
    }

    @Test
    fun convertRowScrollable(){
        val code = """
            Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.Top)
            """.trimIndent()
        val result = """
ScrollView(.horizontal){
	HStack(
	alignment: VerticalAlignment.top,
	spacing: CGFloat(10)){

	}
}
    """.trimIndent()
        val parseResult = KotlinParserFacade.parse(code)

        assertEquals(result, parseResult.root!!.generateCode())
    }

}