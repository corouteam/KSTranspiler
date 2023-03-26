package it.poliba.KSTranspiler.ZStackComposable

import it.poliba.KSTranspiler.SwiftParser
import it.poliba.KSTranspiler.facade.KotlinParserFacade
import it.poliba.KSTranspiler.facade.KotlinParserFacadeScript
import it.poliba.KSTranspiler.facade.SwiftParserFacade
import it.poliba.KSTranspiler.facade.SwiftParserFacadeScript
import it.poliba.KSTranspiler.generateCode
import it.poliba.KSTranspiler.generateKotlinCode
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
/**
 * Test for Image. This class aims to verify the correctness of
 * the conversion of the Image Widget from Compose to SwiftUI.
 */
class ImageSwiftToKotlin {
    @Test
    fun mapOnlyImage(){

        val code = "Image(\"nome-immagine-test\")"
        val result = """
            Image(painter = painterResource(id = getResources().getIdentifier("nome-immagine-test", "drawable", context.getPackageName())))
            """.trimIndent()

        val parseResult = SwiftParserFacadeScript.parse(code)
        assertEquals(result, parseResult.root!!.generateKotlinCode())
    }

    @Test
    fun mapOnlyImageVariable(){

        val code = """
        var imageName:String = "immagine-test"
        Image(imageName)""".trimIndent()

        val result = """
        var imageName:String = "immagine-test"
        Image(painter = painterResource(id = getResources().getIdentifier(imageName, "drawable", context.getPackageName())))
        """.trimIndent()
        val parseResult = SwiftParserFacadeScript.parse(code)
        assertEquals(result, parseResult.root!!.generateKotlinCode())
    }
    @Test
    fun mapImageWithSuffixComplete(){
        val code = "Image(\"nome-immagine-test\")\n.resizable()\n.aspectRatio(contentMode: ContentMode.fill)"
        val result = """
            Image(
            painter = painterResource(id = getResources().getIdentifier("nome-immagine-test", "drawable", context.getPackageName())),
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxSize())
            """.trimIndent()
        val parseResult = SwiftParserFacadeScript.parse(code)
        assertEquals(result, parseResult.root!!.generateKotlinCode())
    }
    @Test
    fun mapImageContentScaleVariable(){

        val code = """
        var fillVariable:ContentMode = ContentMode.fill
        Image("nome-immagine-test")
        .resizable()
        .aspectRatio(contentMode: fillVariable)
        """.trimIndent()

        val result = """
            var fillVariable:ContentScale = ContentScale.FillWidth
            Image(
            painter = painterResource(id = getResources().getIdentifier("nome-immagine-test", "drawable", context.getPackageName())),
            contentScale = fillVariable,
            modifier = Modifier.fillMaxSize())
            """.trimIndent()
        val parseResult = SwiftParserFacadeScript.parse(code)
        assertEquals(result, parseResult.root!!.generateKotlinCode())
    }

    @Test
    fun mapContentScale(){
        val code = "var a = ContentMode.fill"
        val result = """  var a:ContentScale = ContentScale.FillWidth""".trimIndent()
        val parseResult = SwiftParserFacade.parse(code)
        assertEquals(result, parseResult.root!!.generateKotlinCode())
    }
}