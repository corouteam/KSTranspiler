package it.poliba.KSTranspiler.ZStackComposable

import it.poliba.KSTranspiler.facade.KotlinParserFacade
import it.poliba.KSTranspiler.facade.KotlinParserFacadeScript
import it.poliba.KSTranspiler.generateCode
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * Test for Image. This class aims to verify the correctness of
 * the conversion of the Image Widget from Compose to SwiftUI.
 */
class ImageKotlinToSwift {
    @Test
    fun mapOnlyImage(){
        val code = """
            Image(painter = painterResource(id = getResources().getIdentifier("nome-immagine-test", "drawable", context.getPackageName())))
            """.trimIndent()

        val result = "Image(\"nome-immagine-test\")"
        val parseResult = KotlinParserFacadeScript.parse(code)
        assertEquals(result, parseResult.root!!.generateCode())
    }

    @Test
    fun mapOnlyImageVariable(){
        val code = """
            var imageName = "immagine-test"
            Image(painter = painterResource(id = getResources().getIdentifier(imageName, "drawable", context.getPackageName())))
            """.trimIndent()

        val result = """
        var imageName:String = "immagine-test"
        Image(imageName)""".trimIndent()
        val parseResult = KotlinParserFacadeScript.parse(code)
        assertEquals(result, parseResult.root!!.generateCode())
    }
    @Test
    fun mapImageWithSuffixComplete(){
        val code = """
            Image(
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillWidth,
            painter = painterResource(id = getResources().getIdentifier("nome-immagine-test", "drawable", context.getPackageName())))
 
            """.trimIndent()

        val result = "Image(\"nome-immagine-test\")\n.resizable()\n.aspectRatio(contentMode: ContentMode.fill)"
        val parseResult = KotlinParserFacadeScript.parse(code)
        assertEquals(result, parseResult.root!!.generateCode())
    }
    @Test
    fun mapImageContentScaleVariable(){
        val code = """
            var fillVariable = ContentScale.FillWidth
            Image(
            modifier = Modifier.fillMaxSize(),
            contentScale = fillVariable,
            painter = painterResource(id = getResources().getIdentifier("nome-immagine-test", "drawable", context.getPackageName())))
 
            """.trimIndent()

        val result = """
        var fillVariable:ContentMode = ContentMode.fill
        Image("nome-immagine-test")
        .resizable()
        .aspectRatio(contentMode: fillVariable)
        """.trimIndent()
        val parseResult = KotlinParserFacadeScript.parse(code)
        assertEquals(result, parseResult.root!!.generateCode())
    }

    @Test
    fun mapContentScale(){
        val code = """  var a = ContentScale.FillWidth  """.trimIndent()
        val result = "var a:ContentMode = ContentMode.fill"
        val parseResult = KotlinParserFacade.parse(code)
        assertEquals(result, parseResult.root!!.generateCode())
    }
}

