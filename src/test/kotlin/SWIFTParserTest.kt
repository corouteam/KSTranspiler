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
    fun parseSwiftForDeclaration(){
        assertEquals(
            """SwiftScript
  Line
    ExpressionStatement
      ForExpression
        For
          T[for]
          T[i]
          T[in]
          RangeExpression
            IntLiteral
              T[1]
            T[...]
            IntLiteral
              T[42]
          ControlStructureBody
            Block
              T[{]
              T[
]
              PrintStatement
                Print
                  T[print]
                  T[(]
                  StringLiteralExpression
                    StringLiteral
                      LineStringLiteral
                        T["]
                        LineStringContent
                          T[Hello world]
                        T["]
                  T[)]
              Semis
                T[
]
              T[}]
    T[<EOF>]
""",
            toParseTree(parseResourceScript("swift/forDeclaration")).multiLineString())
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
    fun parseSwiftTextWithRedColor(){
        val expected = """SwiftScript
  Line
    ExpressionStatement
      WidgetCallExpression
        TextWidget
          T[Text]
          T[(]
          StringLiteralExpression
            StringLiteral
              LineStringLiteral
                T["]
                LineStringContent
                  T[Music]
                T["]
          T[)]
          T[.]
          ForegroundColorSuffix
            T[foregroundColor]
            T[(]
            ColorLiteral
              RedColor
                T[Color]
                T[.]
                T[red]
            T[)]
          T[.]
          FontWeightSuffix
            T[fontWeight]
            T[(]
            FontWeightLiteral
              T[Font]
              T[.]
              T[Weight]
              T[.]
              Bold
                T[bold]
            T[)]
    T[<EOF>]

        """.trimIndent()
        val actual = toParseTree(parseResourceScript("swift/textWithRedColor")).multiLineString()
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
                "            T[contentMode]\n" +
                "            T[:]\n" +
                "            ContentModeExpression\n" +
                "              T[ContentMode]\n" +
                "              T[.]\n" +
                "              ContentModeFit\n" +
                "                T[fit]\n" +
                "            T[)]\n" +
                "    T[<EOF>]\n"
        val actual = toParseTree(parseResourceScript("swift/imageWithResizableAndScaledToFit")).multiLineString()
        assertEquals(expected, actual)
    }

    @Test
    fun parseStruct(){
        val expected = "SwiftFile\n" +
                "  Declaration\n" +
                "    ClassDeclaration\n" +
                "      T[struct]\n" +
                "      T[MainView]\n" +
                "      T[:]\n" +
                "      DelegationSpecifiers\n" +
                "        UserType\n" +
                "          T[View]\n" +
                "      ClassBody\n" +
                "        T[{]\n" +
                "        T[\n" +
                "]\n" +
                "        ClassMemberDeclaration\n" +
                "          Declaration\n" +
                "            PropertyDeclaration\n" +
                "              VarDeclaration\n" +
                "                T[var]\n" +
                "                T[body]\n" +
                "                T[:]\n" +
                "                T[some]\n" +
                "                UserType\n" +
                "                  T[View]\n" +
                "              ComputedPropertyDeclarationBody\n" +
                "                Block\n" +
                "                  T[{]\n" +
                "                  T[\n" +
                "]\n" +
                "                  ExpressionStatement\n" +
                "                    WidgetCallExpression\n" +
                "                      TextWidget\n" +
                "                        T[Text]\n" +
                "                        T[(]\n" +
                "                        StringLiteralExpression\n" +
                "                          StringLiteral\n" +
                "                            LineStringLiteral\n" +
                "                              T[\"]\n" +
                "                              LineStringContent\n" +
                "                                T[Ciao]\n" +
                "                              T[\"]\n" +
                "                        T[)]\n" +
                "                  Semis\n" +
                "                    T[\n" +
                "]\n" +
                "                  T[}]\n" +
                "        Semis\n" +
                "          T[\n" +
                "]\n" +
                "        T[}]\n" +
                "  T[<EOF>]\n"
        val actual = toParseTree(parseResource("swift/structDeclaration")).multiLineString()
        assertEquals(expected, actual)
    }

    @Test
    fun parseSwiftButton(){
        val expected = "SwiftScript\n" +
                "  Line\n" +
                "    ExpressionStatement\n" +
                "      WidgetCallExpression\n" +
                "        ButtonWidget\n" +
                "          T[Button]\n" +
                "          T[(]\n" +
                "          T[action]\n" +
                "          T[:]\n" +
                "          FunctionBody\n" +
                "            Block\n" +
                "              T[{]\n" +
                "              T[}]\n" +
                "          T[)]\n" +
                "          Block\n" +
                "            T[{]\n" +
                "            T[}]\n" +
                "    T[<EOF>]\n"
        val actual = toParseTree(parseResourceScript("swift/button")).multiLineString()
        assertEquals(expected, actual)
    }


    @Test
    fun parseSwiftDivider(){
        val expected = "SwiftScript\n" +
                "  Line\n" +
                "    ExpressionStatement\n" +
                "      WidgetCallExpression\n" +
                "        DividerWidget\n" +
                "          T[Divider]\n" +
                "          T[(]\n" +
                "          T[)]\n" +
                "    T[<EOF>]\n"
        val actual = toParseTree(parseResourceScript("swift/divider")).multiLineString()
        assertEquals(expected, actual)
    }

    @Test
    fun parseSwiftDividerWithOverlay(){
        val expected = "SwiftScript\n" +
                "  Line\n" +
                "    ExpressionStatement\n" +
                "      WidgetCallExpression\n" +
                "        DividerWidget\n" +
                "          T[Divider]\n" +
                "          T[(]\n" +
                "          T[)]\n" +
                "          T[.]\n" +
                "          OverlaySuffix\n" +
                "            T[overlay]\n" +
                "            T[(]\n" +
                "            BlueColor\n" +
                "              T[Color]\n" +
                "              T[.]\n" +
                "              T[blue]\n" +
                "            T[)]\n" +
                "    T[<EOF>]\n"
        val actual = toParseTree(parseResourceScript("swift/dividerWithOverlay")).multiLineString()
        assertEquals(expected, actual)
    }

    @Test
    fun parseSpacer(){
        val expected = "SwiftScript\n" +
                "  Line\n" +
                "    ExpressionStatement\n" +
                "      WidgetCallExpression\n" +
                "        SpacerWidget\n" +
                "          T[Spacer]\n" +
                "          T[(]\n" +
                "          T[)]\n" +
                "    T[<EOF>]\n"
        val actual = toParseTree(parseResourceScript("swift/spacer")).multiLineString()
        assertEquals(expected, actual)
    }

    @Test
    fun parseSpacerWithSize(){
        val expected = "SwiftScript\n" +
                "  Line\n" +
                "    ExpressionStatement\n" +
                "      WidgetCallExpression\n" +
                "        SpacerWidget\n" +
                "          T[Spacer]\n" +
                "          T[(]\n" +
                "          T[)]\n" +
                "          T[.]\n" +
                "          FrameSuffix\n" +
                "            T[frame]\n" +
                "            T[(]\n" +
                "            WidthParam\n" +
                "              T[width]\n" +
                "              T[:]\n" +
                "              DoubleLiteral\n" +
                "                T[54.0]\n" +
                "            T[,]\n" +
                "            HeightParam\n" +
                "              T[height]\n" +
                "              T[:]\n" +
                "              DoubleLiteral\n" +
                "                T[54.0]\n" +
                "            T[)]\n" +
                "    T[<EOF>]\n"
        val actual = toParseTree(parseResourceScript("swift/spacerWithFrame")).multiLineString()
        assertEquals(expected, actual)
    }

    @Test
    fun parseZStack(){
        val expected = "SwiftScript\n" +
                "  Line\n" +
                "    ExpressionStatement\n" +
                "      WidgetCallExpression\n" +
                "        ZStackWidget\n" +
                "          T[ZStack]\n" +
                "          Block\n" +
                "            T[{]\n" +
                "            T[}]\n" +
                "    T[<EOF>]\n"
        val actual = toParseTree(parseResourceScript("swift/zstack")).multiLineString()
        assertEquals(expected, actual)
    }

}
