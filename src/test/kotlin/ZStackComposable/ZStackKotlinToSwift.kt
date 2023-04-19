package it.poliba.KSTranspiler.ZStackComposable

import it.poliba.KSTranspiler.facade.KotlinParserFacade
import it.poliba.KSTranspiler.generateCode
import it.poliba.KSTranspiler.generateKotlinCode
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * Test for ZStack. This class aims to verify the correctness of
 * the conversion of the ZStack Widget from Compose to SwiftUI.
 */
class ZStackKotlinToSwift {
    @Test
    fun convertZIndex() {
        val code = """
Box() {
    Divider(modifier = Modifier.zIndex(2))
    Text("Ciao", modifier = Modifier.zIndex(1))
    Spacer(modifier = Modifier.zIndex(3))
    Button(action = {}, modifier = Modifier.zIndex(4)){}
}
        """.trimIndent()

        val result = """
ZStack{
	Text("Ciao")
	Divider()
	Spacer()
	Button(action: {

	}){

	}
}""".trimIndent()

        val parseResult = KotlinParserFacade.parse(code)
        assertEquals(result, parseResult.root!!.generateCode())
    }
}

