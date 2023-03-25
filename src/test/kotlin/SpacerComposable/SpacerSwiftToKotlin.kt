import it.poliba.KSTranspiler.facade.KotlinParserFacadeScript
import it.poliba.KSTranspiler.facade.SwiftParserFacadeScript
import it.poliba.KSTranspiler.generateCode
import it.poliba.KSTranspiler.generateKotlinCode
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * Test for Spacer. This class aims to verify the correctness of
 * the conversion of the spacer Widget from SwiftUI to Compose.
 */
class SpacerSwiftToKotlin {
    @Test
    fun convertComplete() {
        val code = """
Spacer()
    .frame(width: CGFloat(54), height: CGFloat(54))
        """.trimIndent()

        val result = """
            Spacer(modifier = Modifier.width(54.dp).height(54.dp))
            """.trimIndent()

        val parseResult = SwiftParserFacadeScript.parse(code)
        assertEquals(result, parseResult.root!!.generateKotlinCode())
    }

    @Test
    fun convertOnlyHeight() {
        val code = """
Spacer()
	.frame(height: CGFloat(54))
        """.trimIndent()

        val result = """
            Spacer(modifier = Modifier.height(54.dp))
            """.trimIndent()

        val parseResult = SwiftParserFacadeScript.parse(code)
        assertEquals(result, parseResult.root!!.generateKotlinCode())
    }

    @Test
    fun convertOnlyWidth() {
        val code = """
Spacer()
	.frame(width: CGFloat(54))
        """.trimIndent()

        val result = """
            Spacer(modifier = Modifier.width(54.dp))
            """.trimIndent()


        val parseResult = SwiftParserFacadeScript.parse(code)
        assertEquals(result, parseResult.root!!.generateKotlinCode())
    }

    @Test
    fun convertCompleteVariable() {
        val code = """
var sHeight:CGFloat = CGFloat(54)
var sWidth:CGFloat = CGFloat(55)
Spacer()
	.frame(width: sWidth, height: sHeight)
        """.trimIndent()

        val result = """
            var sHeight:Dp = 54.dp
            var sWidth:Dp = 55.dp
            Spacer(modifier = Modifier.width(sWidth).height(sHeight))
            """.trimIndent()

        val parseResult = SwiftParserFacadeScript.parse(code)
        assertEquals(result, parseResult.root!!.generateKotlinCode())
    }

}