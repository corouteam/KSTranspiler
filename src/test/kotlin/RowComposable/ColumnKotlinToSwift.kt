package it.poliba.KSTranspiler.TextComposable

import it.poliba.KSTranspiler.facade.KotlinParserFacade
import it.poliba.KSTranspiler.generateCode
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/**
 * Test for Column. This class aims to verify the correctness of
 * the conversion of Column Widget from Compose to SwiftUI.
 * It also verifies VerticalArrangement and HorizontalAlignment since were introduced for this widget
 */
class ColumnKotlinToSwift {
    @Test
    fun convertColumn() {
        val code = """
            Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
             horizontalAlignment = Alignment.Start)
            """.trimIndent()
        val result = """
         VStack(
         	alignment: HorizontalAlignment.leading,
         	spacing: CGFloat(10)){
   
         }
        """.trimIndent()

        val parseResult = KotlinParserFacade.parse(code)
        assertEquals(result, parseResult.root!!.generateCode())
    }

    @Test
    fun convertColumnOnlyVerticalArrangement() {
        val code = """
            Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
            )
            """.trimIndent()
        val result = """
         VStack(
         	spacing: CGFloat(10)){
   
         }
        """.trimIndent()

        val parseResult = KotlinParserFacade.parse(code)
        assertEquals(result, parseResult.root!!.generateCode())
    }

    @Test
    fun convertColumnOnlyVerticalArrangementVariable() {
        val code = """
            var cSpacing = 10.dp
            Column(
            verticalArrangement = Arrangement.spacedBy(cSpacing)
            )
            """.trimIndent()
        val result = """
         var cSpacing:CGFloat = CGFloat(10)
         VStack(
         	spacing: cSpacing){
   
         }
        """.trimIndent()

        val parseResult = KotlinParserFacade.parse(code)
        assertEquals(result, parseResult.root!!.generateCode())
    }

    @Test
    fun convertColumnOnlyHorizontalAlignment() {
        val code = """
            Column(
             horizontalAlignment = Alignment.Start)
            """.trimIndent()
        val result = """
VStack(
	alignment: HorizontalAlignment.leading){

}""".trimIndent()

        val parseResult = KotlinParserFacade.parse(code)
        assertEquals(result, parseResult.root!!.generateCode())
    }

    @Test
    fun convertColumnOnlyHorizontalAlignmentVariable() {
        val code = """
                var cAlignment = Alignment.Start
                Column(
                 horizontalAlignment = cAlignment)
                """.trimIndent()
        val result = """
    var cAlignment:HorizontalAlignment = HorizontalAlignment.leading
    VStack(
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
    fun wrongAlignmentThrowsError2() {
        val code =  "var cAlignment = Alignment.Starts"

        val parseResult = KotlinParserFacade.parse(code)

        assertEquals("Unrecognized symbol Starts", parseResult.errors.first().message)
    }
    @Test
    fun wrongArrangementThrowsError() {
        val code =  "Arrangement.spacedByf(cSpacing)"
        val parseResult = KotlinParserFacade.parse(code)

        assertEquals("Unrecognized symbol spacedByf", parseResult.errors.first().message)
    }

    /**
     * This is an actual limitation of the sistem, should be fixed once we add Class validation
     */
    @Test
    fun wrongArrangementThrowsError2() {
        val code =  "Arrangements.spacedByf(cSpacing)"
        val parseResult = KotlinParserFacade.parse(code)

        assertNull( parseResult.errors.firstOrNull())
    }


    @Test
    fun wrongArrangementValueThrowsError() {
        val code =  "Arrangements.spacedByf('Hello')"
        val parseResult = KotlinParserFacade.parse(code)

        assertEquals("token recognition error at: '''", parseResult.errors.first().message)
    }

    @Test
    fun convertColumnOnlyHorizontalAlignmentVariableFailsIfWrongType() {
        val code = """
                var cAlignment = "Ciao"
                Column(
                 horizontalAlignment = cAlignment)
                """.trimIndent()
        val result = """
    var cAlignment:String = "Ciao"
    VStack(
    	alignment: cAlignment){
    
    }""".trimIndent()

        val parseResult = KotlinParserFacade.parse(code)
        assertEquals("Column horizontal alignment requires a HorizontalAlignmentType", parseResult.errors.first().message)
    }

    @Test
    fun convertColumnOnlyHorizontalArrangementVariableFailsIfWrongType() {
        val code = """
            var cSpacing = "Ciao"
            Column(
            verticalArrangement = Arrangement.spacedBy(cSpacing)
            )
            """.trimIndent()
        val result = """
         var cSpacing:String = "Ciao"
         VStack(
         	spacing: cSpacing){
   
         }
        """.trimIndent()

        val parseResult = KotlinParserFacade.parse(code)

        assertEquals("Column spacing requires a dp literal", parseResult.errors.first().message)
        assertEquals(result, parseResult.root!!.generateCode())
    }

    @Test
    fun convertColumnWithChildren() {
        val code = """
            Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
             horizontalAlignment = Alignment.Start){
                Text("Ciao")
                Text("Linea2")
                Text("Linea3")
             }
            """.trimIndent()
        val result = """
         VStack(
         	alignment: HorizontalAlignment.leading,
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
    fun convertColumnScrollable(){
        val code = """
            Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.Start)
            """.trimIndent()
        val result = """
ScrollView(.vertical){
	VStack(
	alignment: HorizontalAlignment.leading,
	spacing: CGFloat(10)){

	}
}
    """.trimIndent()
        val parseResult = KotlinParserFacade.parse(code)

        assertEquals(result, parseResult.root!!.generateCode())
    }



}