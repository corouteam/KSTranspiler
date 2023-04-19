package it.poliba.KSTranspiler.TextComposable

import it.poliba.KSTranspiler.facade.SwiftParserFacade
import it.poliba.KSTranspiler.facade.SwiftParserFacadeScript
import it.poliba.KSTranspiler.generateCode
import it.poliba.KSTranspiler.generateKotlinCode
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * Test for Column. This class aims to verify the correctness of
 * the conversion of Column Widget from SwiftUI to Kotlin.
 * It also verifies alignment and spacing since were introduced for this widget
 */
class RowSwiftToKotlin {
    @Test
    fun convertRow() {
        val code = """
         HStack(
         	alignment: VerticalAlignment.top,
         	spacing: CGFloat(10)){
   
         }
        """.trimIndent()

        val result =
    """
Row(
	verticalAlignment = Alignment.Top,
	horizontalArrangement = Arrangement.spacedBy(10.dp)
){

}""".trimIndent()
        val parseResult = SwiftParserFacadeScript.parse(code)
        assertEquals(result, parseResult.root!!.generateKotlinCode())
    }

    @Test
    fun convertColumnOnlyArrangement() {
       val code =
           """HStack(
         	spacing: CGFloat(10)){
   
         }
        """.trimIndent()

        val result =
            """
Row(
	horizontalArrangement = Arrangement.spacedBy(10.dp)
){

}""".trimIndent()

        val parseResult = SwiftParserFacadeScript.parse(code)
        assertEquals(result, parseResult.root!!.generateKotlinCode())
    }

    @Test
    fun convertColumnOnlyArrangementVariable() {

        val code = """
         var cSpacing:CGFloat = CGFloat(10)
         HStack(
         	spacing: cSpacing){
   
         }
        """.trimIndent()

        val result = """
            var cSpacing:Dp = 10.dp
            Row(
            	horizontalArrangement = Arrangement.spacedBy(cSpacing)
            ){

            }
            """.trimIndent()

        val parseResult = SwiftParserFacadeScript.parse(code)
        assertEquals(result, parseResult.root!!.generateKotlinCode())
    }

    @Test
    fun convertColumnOnlyVerticalAlignment() {
        val code = """
HStack(
	alignment: VerticalAlignment.top){

}""".trimIndent()
        val result = """
Row(
	verticalAlignment = Alignment.Top
){

}""".trimIndent()

        val parseResult = SwiftParserFacadeScript.parse(code)
        assertEquals(result, parseResult.root!!.generateKotlinCode())
    }

        @Test
        fun convertColumnOnlyVerticalAlignmentVariable() {

            val code = """
        var cAlignment = VerticalAlignment.top
        HStack(
            alignment: cAlignment){

        }""".trimIndent()

            val result = """
var cAlignment:Alignment.Vertical = Alignment.Top
Row(
	verticalAlignment = cAlignment
){

}""".trimIndent()
            val parseResult = SwiftParserFacadeScript.parse(code)
            assertEquals(result, parseResult.root!!.generateKotlinCode())
        }
    @Test
    fun parseAlignmentWithImplicitType() {

        val code = """
        var cAlignment = VerticalAlignment.top
        """.trimIndent()

        val result = """
var cAlignment:Alignment.Vertical = Alignment.Top
""".trimIndent()
        val parseResult = SwiftParserFacade.parse(code)
        assertEquals(result, parseResult.root!!.generateKotlinCode())
    }

    //TODO: @Lops, per far passare questo test occorre aggiungere il tipo al parser
    /*
    @Test
    fun parseHorizontalAlignmentWithExplicitType() {

        val code = """
        var cAlignment: HorizontalAlignment = HorizontalAlignment.leading
        """.trimIndent()

        val result = """
var cAlignment:Alignment.Horizontal = Alignment.Start
""".trimIndent()
        val parseResult = SwiftParserFacade.parse(code)
        assertEquals(result, parseResult.root!!.generateKotlinCode())
    }
    */

    @Test
    fun wrongAlignmentThrowsError() {
        val code =  " var cAlignment = VerticalAlignment.tops"

        val parseResult = SwiftParserFacade.parse(code)

        assertEquals("Unrecognized symbol tops", parseResult.errors.first().message)
    }

    @Test
    fun convertColumnOnlyAlignmentVariableFailsIfWrongType() {

        val code = """
            var cAlignment:String = "Ciao"
            HStack(
                alignment: cAlignment){

            }""".trimIndent()

        val parseResult = SwiftParserFacadeScript.parse(code)
        assertEquals("Row vertical alignment requires a VerticalAlignmentType", parseResult.errors.first().message)
    }

    @Test
    fun convertColumnOnlyArrangementVariableFailsIfWrongType() {
        val code = """
                 var cSpacing:String = "Ciao"
                 HStack(
                     spacing: cSpacing){

                 }
                """.trimIndent()

        val parseResult = SwiftParserFacadeScript.parse(code)

        assertEquals("Row spacing requires a dp literal", parseResult.errors.first().message)
    }

    @Test
    fun convertRowWithChildren() {

        val code = """
                 HStack(
                     alignment: VerticalAlignment.top,
                     spacing: CGFloat(10)){
                     Text("Ciao")
                     Text("Linea2")
                     Text("Linea3")
                 }
                """.trimIndent()
        val result = """
Row(
	verticalAlignment = Alignment.Top,
	horizontalArrangement = Arrangement.spacedBy(10.dp)
){
	Text("Ciao")
	Text("Linea2")
	Text("Linea3")
}""".trimIndent()
        val parseResult = SwiftParserFacadeScript.parse(code)
        assertEquals(result, parseResult.root!!.generateKotlinCode())
    }

    @Test
    fun convertHorizontalScrollView(){
        val code = """
ScrollView(.horizontal){
	HStack(
	alignment: VerticalAlignment.top,
	spacing: CGFloat(10)){

	}
}
    """.trimIndent()
        val result = """
 Row(
 	verticalAlignment = Alignment.Top,
 	horizontalArrangement = Arrangement.spacedBy(10.dp),
 	modifier = Modifier.horizontalScroll(rememberScrollState())
 ){

 }
            """.trimIndent()

        val parseResult = SwiftParserFacadeScript.parse(code)

        assertEquals(result, parseResult.root!!.generateKotlinCode())
    }

}