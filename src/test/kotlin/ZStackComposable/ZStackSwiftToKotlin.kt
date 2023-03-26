package it.poliba.KSTranspiler.ZStackComposable

import it.poliba.KSTranspiler.facade.KotlinParserFacadeScript
import it.poliba.KSTranspiler.facade.SwiftParserFacadeScript
import it.poliba.KSTranspiler.generateCode
import it.poliba.KSTranspiler.generateKotlinCode
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


class ZStackSwiftToKotlin {
    @Test
    fun convertZIndex() {

        val code = """
ZStack{
	Text("Ciao")
	Divider()
	Spacer()
    VStack()
    HStack()
	Button(action: {

	}){

	}
}""".trimIndent()
        val result = """
Box(){
	Text("Ciao", modifier = Modifier.zIndex(0))
	Divider(modifier = Modifier.zIndex(1))
	Spacer(modifier = Modifier.zIndex(2))
	Column(
		modifier = Modifier.zIndex(3)
	){

	}
	Row(
		modifier = Modifier.zIndex(4)
	){

	}
	Button(onClick = {

	}, modifier = Modifier.zIndex(5)){

	} 
}
        """.trimIndent()

        val parseResult = SwiftParserFacadeScript.parse(code)
        assertEquals(result, parseResult.root!!.generateKotlinCode())
    }



}