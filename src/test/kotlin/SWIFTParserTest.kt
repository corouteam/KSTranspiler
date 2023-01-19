package it.poliba.KSTranspiler
import it.poliba.KSTranspiler.facade.SwiftAntlrParserFacade
import it.poliba.KSTranspiler.facade.SwiftAntlrParserFacadeScript
import it.poliba.KSTranspiler.facade.SwiftParserFacade
import it.poliba.KSTranspiler.facade.SwiftParserFacadeScript
import org.antlr.v4.runtime.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import toParseTree

class SWIFTParserTest {

    private fun parseResource(
        resourceName: String,
    ): ParserRuleContext {
        val parseResult = SwiftAntlrParserFacade
            .parse(this.javaClass.getResourceAsStream("/${resourceName}.txt"))

        if (parseResult.isCorrect()) {
            return parseResult.root ?: throw Exception("ParserRuleContext was null")
        } else {
            throw parseResult.errors.first()
        }
    }

    private fun parseResourceScript(
        resourceName: String,
    ): ParserRuleContext {
        val parseResult = SwiftAntlrParserFacadeScript
            .parse(this.javaClass.getResourceAsStream("/${resourceName}.txt"))

        if (parseResult.isCorrect()) {
            return parseResult.root ?: throw Exception("ParserRuleContext was null")
        } else {
            throw parseResult.errors.first()
        }
    }


    @Test
    fun parseSwiftTextWithBold(){
        val expected = "SwiftScript\n" +
                "  Line\n" +
                "    ExpressionStatement\n" +
                "      WidgetCallExpression\n" +
                "        TextWidget\n" +
                "          T[Text]\n" +
                "          T[(]\n" +
                "          StringLiteralExpression\n" +
                "            StringLiteral\n" +
                "              LineStringLiteral\n" +
                "                T[\"]\n" +
                "                LineStringContent\n" +
                "                  T[Hello world]\n" +
                "                T[\"]\n" +
                "          T[)]\n" +
                "          T[.]\n" +
                "          BoldSuffix\n" +
                "            T[fontWeight]\n" +
                "            T[(]\n" +
                "            BoldFontWeight\n" +
                "              T[Font]\n" +
                "              T[.]\n" +
                "              T[Weight]\n" +
                "              T[.]\n" +
                "              T[bold]\n" +
                "            T[)]\n" +
                "    T[<EOF>]\n"
        val actual = toParseTree(parseResourceScript("swift/textWithBold")).multiLineString()
        assertEquals(expected, actual)
    }

    @Test
    fun parseSwiftTextWithColor(){
        val expected = "SwiftScript\n" +
                "  Line\n" +
                "    ExpressionStatement\n" +
                "      WidgetCallExpression\n" +
                "        TextWidget\n" +
                "          T[Text]\n" +
                "          T[(]\n" +
                "          StringLiteralExpression\n" +
                "            StringLiteral\n" +
                "              LineStringLiteral\n" +
                "                T[\"]\n" +
                "                LineStringContent\n" +
                "                  T[Music]\n" +
                "                T[\"]\n" +
                "          T[)]\n" +
                "          T[.]\n" +
                "          ForegroundColorSuffix\n" +
                "            T[foregroundColor]\n" +
                "            T[(]\n" +
                "            BlueColor\n" +
                "              T[Color]\n" +
                "              T[.]\n" +
                "              T[blue]\n" +
                "            T[)]\n" +
                "          T[.]\n" +
                "          BoldSuffix\n" +
                "            T[fontWeight]\n" +
                "            T[(]\n" +
                "            BoldFontWeight\n" +
                "              T[Font]\n" +
                "              T[.]\n" +
                "              T[Weight]\n" +
                "              T[.]\n" +
                "              T[bold]\n" +
                "            T[)]\n" +
                "    T[<EOF>]\n"
        val actual = toParseTree(parseResourceScript("swift/textWithColor")).multiLineString()
        assertEquals(expected, actual)
    }

    @Test
    fun parseSwiftImageWithResizableAndAspectRatio(){
        val expected = "SwiftScript\n" +
                "  Line\n" +
                "    ExpressionStatement\n" +
                "      WidgetCallExpression\n" +
                "        ImageWidget\n" +
                "          T[Image]\n" +
                "          T[(]\n" +
                "          StringLiteralExpression\n" +
                "            StringLiteral\n" +
                "              LineStringLiteral\n" +
                "                T[\"]\n" +
                "                LineStringContent\n" +
                "                  T[nome-immagine-test]\n" +
                "                T[\"]\n" +
                "          T[)]\n" +
                "          T[.]\n" +
                "          ResizableSuffix\n" +
                "            T[resizable]\n" +
                "            T[(]\n" +
                "            T[)]\n" +
                "          T[.]\n" +
                "          AspectRatioSuffix\n" +
                "            T[aspectRatio]\n" +
                "            T[(]\n" +
                "            ContentModeFit\n"+
                "              T[contentMode]\n" +
                "              T[:]\n" +
                "              T[ContentMode]\n" +
                "              T[.]\n" +
                "              T[fit]\n" +
                "            T[)]\n" +
                "    T[<EOF>]\n"
        val actual = toParseTree(parseResourceScript("swift/imageWithResizableAndScaledToFit")).multiLineString()
        assertEquals(expected, actual)
    }

}