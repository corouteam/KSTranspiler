package it.poliba.KSTranspiler.ButtonComposable

import it.poliba.KSTranspiler.facade.KotlinParserFacadeScript
import it.poliba.KSTranspiler.facade.SwiftParserFacadeScript
import it.poliba.KSTranspiler.generateCode
import it.poliba.KSTranspiler.generateKotlinCode
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ButtonSwiftToKotlin {
    @Test
    fun mapButton(){

        val code = """
            Button(action: {
            	print("Ok")
                print("Done")

            }){
            	Text("Ciao")
            }
        """.trimIndent()

        val result = """
Button(onClick = {
	print("Ok")
	print("Done")
}){
	Text("Ciao")
} 
            """.trimIndent()
        val parseResult = SwiftParserFacadeScript.parse(code)
        assertEquals(result, parseResult.root!!.generateKotlinCode())
    }
}