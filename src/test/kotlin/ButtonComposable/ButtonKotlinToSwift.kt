package it.poliba.KSTranspiler.ButtonComposable

import it.poliba.KSTranspiler.facade.KotlinParserFacade
import it.poliba.KSTranspiler.generateCode
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ButtonKotlinToSwift {
    @Test
    fun mapButton(){
        val code = """
            Button( onClick = {
                print("Ok")
            }){
                Text("Ciao") 
            }
            """.trimIndent()

        val result = """
Button(action: {
	print("Ok")
}){
	Text("Ciao")
}
""".trimIndent()
        val parseResult = KotlinParserFacade.parse(code)
        assertEquals(result, parseResult.root!!.generateCode())
    }
}