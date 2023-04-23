package it.poliba.KSTranspiler.TextComposable

import it.poliba.KSTranspiler.facade.SwiftParserFacade
import it.poliba.KSTranspiler.facade.SwiftParserFacadeScript
import it.poliba.KSTranspiler.generateCode
import it.poliba.KSTranspiler.generateKotlinCode
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/**
 * Test for Column. This class aims to verify the correctness of
 * the conversion of Column Widget from SwiftUI to Kotlin.
 * It also verifies alignment and spacing since were introduced for this widget
 */
class ColumnSwiftToKotlin {
    @Test
    fun convertColumn() {
        val code = """
         VStack(
         	alignment: HorizontalAlignment.leading,
         	spacing: CGFloat(10)){
   
         }
        """.trimIndent()

        val result =
    """
Column(
	horizontalAlignment = Alignment.Start,
	verticalArrangement = Arrangement.spacedBy(10.dp)
){

}""".trimIndent()
        val parseResult = SwiftParserFacadeScript.parse(code)
        assertEquals(result, parseResult.root!!.generateKotlinCode())
    }

    @Test
    fun convertColumnOnlyVerticalArrangement() {
       val code =
           """VStack(
         	spacing: CGFloat(10)){
   
         }
        """.trimIndent()

        val result =
            """
Column(
	verticalArrangement = Arrangement.spacedBy(10.dp)
){

}""".trimIndent()

        val parseResult = SwiftParserFacadeScript.parse(code)
        assertEquals(result, parseResult.root!!.generateKotlinCode())
    }

    @Test
    fun convertColumnOnlyVerticalArrangementVariable() {

        val code = """
         var cSpacing:CGFloat = CGFloat(10)
         VStack(
         	spacing: cSpacing){
   
         }
        """.trimIndent()

        val result = """
            var cSpacing:Dp = 10.dp
            Column(
            	verticalArrangement = Arrangement.spacedBy(cSpacing)
            ){

            }
            """.trimIndent()

        val parseResult = SwiftParserFacadeScript.parse(code)
        assertEquals(result, parseResult.root!!.generateKotlinCode())
    }

    @Test
    fun convertColumnOnlyHorizontalAlignment() {
        val code = """
VStack(
	alignment: HorizontalAlignment.leading){

}""".trimIndent()
        val result = """
Column(
	horizontalAlignment = Alignment.Start
){

}""".trimIndent()

        val parseResult = SwiftParserFacadeScript.parse(code)
        assertEquals(result, parseResult.root!!.generateKotlinCode())
    }

        @Test
        fun convertColumnOnlyHorizontalAlignmentVariable() {

            val code = """
        var cAlignment = HorizontalAlignment.leading
        VStack(
            alignment: cAlignment){

        }""".trimIndent()

            val result = """
var cAlignment:Alignment.Horizontal = Alignment.Start
Column(
	horizontalAlignment = cAlignment
){

}""".trimIndent()
            val parseResult = SwiftParserFacadeScript.parse(code)
            assertEquals(result, parseResult.root!!.generateKotlinCode())
        }
    @Test
    fun parseHorizontalAlignmentWithImplicitType() {

        val code = """
        var cAlignment = HorizontalAlignment.leading
        """.trimIndent()

        val result = """
var cAlignment:Alignment.Horizontal = Alignment.Start
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
        val code =  " var cAlignment = HorizontalAlignment.leadings"

        val parseResult = SwiftParserFacade.parse(code)

        assertEquals("Unrecognized symbol leadings", parseResult.errors.first().message)
    }

    @Test
    fun convertColumnOnlyHorizontalAlignmentVariableFailsIfWrongType() {

        val code = """
            var cAlignment:String = "Ciao"
            VStack(
                alignment: cAlignment){

            }""".trimIndent()

        val result = """
            var cAlignment = "Ciao"
            Column(
             horizontalAlignment = cAlignment)
            """.trimIndent()

        val parseResult = SwiftParserFacadeScript.parse(code)
        assertEquals("Column horizontal alignment requires a HorizontalAlignmentType", parseResult.errors.first().message)
    }

    @Test
    fun convertColumnOnlyHorizontalArrangementVariableFailsIfWrongType() {
        val code = """
                 var cSpacing:String = "Ciao"
                 VStack(
                     spacing: cSpacing){

                 }
                """.trimIndent()
        val result = """
var cSpacing:String = "Ciao"
Column(
	verticalArrangement = Arrangement.spacedBy(cSpacing)
){

}""".trimIndent()


        val parseResult = SwiftParserFacadeScript.parse(code)

        assertEquals("Column spacing requires a dp literal", parseResult.errors.first().message)
        assertEquals(result, parseResult.root!!.generateKotlinCode())
    }

    @Test
    fun convertColumnWithChildren() {

        val code = """
                 VStack(
                     alignment: HorizontalAlignment.leading,
                     spacing: CGFloat(10)){
                     Text("Ciao")
                     Text("Linea2")
                     Text("Linea3")
                 }
                """.trimIndent()
        val result = """
Column(
	horizontalAlignment = Alignment.Start,
	verticalArrangement = Arrangement.spacedBy(10.dp)
){
	Text("Ciao")
	Text("Linea2")
	Text("Linea3")
}""".trimIndent()
        val parseResult = SwiftParserFacadeScript.parse(code)
        assertEquals(result, parseResult.root!!.generateKotlinCode())
    }

    @Test
    fun convertVerticalScrollView(){

        val code = """
ScrollView(.vertical){
	VStack(
	alignment: HorizontalAlignment.leading,
	spacing: CGFloat(10)){

	}
}
    """.trimIndent()

    val result = """
Column(
	horizontalAlignment = Alignment.Start,
	verticalArrangement = Arrangement.spacedBy(10.dp),
	modifier = Modifier.verticalScroll(rememberScrollState())
){

}""".trimIndent()
        val parseResult = SwiftParserFacadeScript.parse(code)

        assertEquals(result, parseResult.root!!.generateKotlinCode())
    }
}