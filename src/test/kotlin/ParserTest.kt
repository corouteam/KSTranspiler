package it.poliba.KSTranspiler
import it.poliba.KSTranspiler.facade.KotlinAntlrParserFacade
import it.poliba.KSTranspiler.facade.KotlinAntlrParserFacadeScript
import it.poliba.KSTranspiler.facade.KotlinParserFacade
import it.poliba.KSTranspiler.facade.KotlinParserFacadeScript
import org.antlr.v4.runtime.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import toParseTree

class KotlinParserTest {

    private fun parseResource(
        resourceName: String,
    ): ParserRuleContext {
        val parseResult = KotlinAntlrParserFacade
            .parse(this.javaClass.getResourceAsStream("/${resourceName}.txt"))

        if (parseResult.isCorrect()) {
            return parseResult.root ?: throw Exception("ParserRuleContext was null")
        } else {
            throw Exception(parseResult.errors.first().message)
        }
    }

    private fun parseResourceScript(
        resourceName: String,
    ): ParserRuleContext {
        val parseResult = KotlinAntlrParserFacadeScript
            .parse(this.javaClass.getResourceAsStream("/${resourceName}.txt"))

        if (parseResult.isCorrect()) {
            return parseResult.root ?: throw Exception("ParserRuleContext was null")
        } else {
            throw Exception(parseResult.errors.first().message)
        }
    }

    @Test
    fun parseAdditionAssignment() {
        assertEquals(
            """KotlinScript
  Line
    AssignmentStatement
      Assignment
        T[a]
        T[=]
        BinaryOperation
          IntLiteral
            T[1]
          T[+]
          IntLiteral
            T[2]
    T[<EOF>]
""",
            toParseTree(parseResourceScript("addition_assignment")).multiLineString())
    }

    @Test
    fun parsePropertyDeclVarInt() {
        assertEquals(
            """KotlinFile
  Declaration
    PropertyDeclaration
      VarDeclaration
        T[var]
        T[a]
      T[=]
      IntLiteral
        T[1]
  T[<EOF>]
""",
            toParseTree(parseResource("property_declaration_var_int")).multiLineString())
    }

    @Test
    fun parsePropertyDeclVarStrinb() {
        assertEquals(
            """KotlinFile
  Declaration
    PropertyDeclaration
      VarDeclaration
        T[var]
        T[a]
      T[=]
      StringLiteralExpression
        StringLiteral
          LineStringLiteral
            T["]
            LineStringContent
              T[ciao]
            T["]
  T[<EOF>]
""",
            toParseTree(parseResource("property_declaration_var_string")).multiLineString())
    }

    @Test
    fun parsePropertyDeclVarBool() {
        assertEquals(
            """KotlinFile
  Declaration
    PropertyDeclaration
      VarDeclaration
        T[var]
        T[a]
      T[=]
      BoolLiteral
        T[true]
  T[<EOF>]
""",
            toParseTree(parseResource("property_declaration_var_bool")).multiLineString())
    }
    @Test
    fun parsePropertyDeclVarIntWithExplicitType() {
        assertEquals(
            """KotlinFile
  Declaration
    PropertyDeclaration
      VarDeclaration
        T[var]
        T[a]
        T[:]
        Integer
          T[Int]
      T[=]
      IntLiteral
        T[1]
  T[<EOF>]
""",
            toParseTree(parseResource("property_declaration_var_int_expl_type")).multiLineString())
    }

    @Test
    fun parseVarDeclInt() {
        assertEquals(
            """KotlinFile
  Declaration
    PropertyDeclaration
      VarDeclaration
        T[var]
        T[a]
        T[:]
        Integer
          T[Int]
  T[<EOF>]
""",
            toParseTree(parseResource("var_declaration_int")).multiLineString())
    }


    @Test
    fun parseValDeclInt() {
        assertEquals(
            """KotlinFile
  Declaration
    PropertyDeclaration
      ValDeclaration
        T[val]
        T[a]
        T[:]
        Integer
          T[Int]
  T[<EOF>]
""",
            toParseTree(parseResource("val_declaration_int")).multiLineString())
    }

    @Test
    fun parseIfDeclaration() {
        assertEquals(
            """KotlinScript
  Line
    ExpressionStatement
      IfExpression
        If
          T[if]
          T[(]
          BoolLiteral
            T[true]
          T[)]
          ControlStructureBody
            Block
              T[{]
              PropertyDeclarationStatement
                PropertyDeclaration
                  ValDeclaration
                    T[val]
                    T[a]
                  T[=]
                  IntLiteral
                    T[5]
              T[}]
    T[<EOF>]
""",
            toParseTree(parseResourceScript("ifDeclaration")).multiLineString())
    }
    @Test
    fun parseSimpleFun(){
        var expected = """KotlinFile
  Declaration
    FunctionDeclaration
      T[fun]
      T[test]
      FunctionValueParameters
        T[(]
        FunctionValueParameter
          Parameter
            T[x]
            T[:]
            Integer
              T[Int]
        T[,]
        FunctionValueParameter
          Parameter
            T[y]
            T[:]
            Integer
              T[Int]
        T[)]
      FunctionBody
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
                      T[ciao]
                    T["]
              T[)]
          Semis
            T[
]
          T[}]
  T[<EOF>]
"""
        val actual = toParseTree(parseResource("function_declaration")).multiLineString()
        assertEquals(expected, actual)
    }

    @Test
    fun parseExpressionFun(){
        var expected = """KotlinFile
  Declaration
    FunctionDeclaration
      T[fun]
      T[test]
      FunctionValueParameters
        T[(]
        FunctionValueParameter
          Parameter
            T[x]
            T[:]
            Integer
              T[Int]
        T[,]
        FunctionValueParameter
          Parameter
            T[y]
            T[:]
            Integer
              T[Int]
        T[)]
      T[:]
      Integer
        T[Int]
      FunctionBody
        Block
          T[{]
          T[
]
          ExpressionStatement
            ReturnExpression
              T[return]
              IntLiteral
                T[3]
          Semis
            T[
]
          T[}]
  T[<EOF>]
"""
        val actual = toParseTree(parseResource("function_declaration_return")).multiLineString()
        assertEquals(expected, actual)
    }

    @Test
    fun parseFunctionCall(){
        var expected = """
KotlinScript
  Line
    ExpressionStatement
      FunctionCall
        T[test]
        FunctionCallParameters
          T[(]
          StringLiteralExpression
            StringLiteral
              LineStringLiteral
                T["]
                LineStringContent
                  T[a]
                T["]
          T[,]
          IntLiteral
            T[42]
          T[)]
    T[<EOF>]

        """.trimIndent()
        val actual = toParseTree(parseResourceScript("function_call")).multiLineString()
        assertEquals(expected, actual)
    }

    @Test
    fun parseRangeExpression() {
        val expected = """KotlinFile
  Declaration
    PropertyDeclaration
      ValDeclaration
        T[val]
        T[a]
      T[=]
      RangeExpression
        IntLiteral
          T[1]
        T[..]
        IntLiteral
          T[42]
  T[<EOF>]
"""
        val actual = toParseTree(parseResource("range_expression")).multiLineString()
        assertEquals(expected, actual)
    }

    @Test
    fun parseListOfExpression(){
        val expected = """
KotlinScript
  Line
    ExpressionStatement
      ListExpression
        T[listOf]
        TypeArguments
          T[<]
          Integer
            T[Int]
          T[>]
        T[(]
        IntLiteral
          T[1]
        T[,]
        IntLiteral
          T[2]
        T[,]
        IntLiteral
          T[3]
        T[)]
    T[<EOF>]

        """.trimIndent()

        val actual = toParseTree(parseResourceScript("listof_expression")).multiLineString()
        assertEquals(expected, actual)
    }


    @Test
    fun parseTextComposable(){
        val expected = "KotlinScript\n" +
                "  Line\n" +
                "    ExpressionStatement\n" +
                "      ComposableCallExpression\n" +
                "        TextComposable\n" +
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
                "    T[<EOF>]\n"
        val actual = toParseTree(parseResourceScript("textComposable")).multiLineString()
        assertEquals(expected, actual)
    }

    @Test
    fun parseCustomColor(){
        val expected = "KotlinScript\n" +
                "  Line\n" +
                "    ExpressionStatement\n" +
                "      ColorLiteral\n" +
                "        CustomColor\n" +
                "          T[Color]\n" +
                "          T[(]\n" +
                "          T[0xFF27AE60]\n" +
                "          T[)]\n" +
                "    T[<EOF>]\n"
        val actual = toParseTree(parseResourceScript("customColor")).multiLineString()
        assertEquals(expected, actual)
    }

    @Test
    fun parseBlueColor(){
        val expected = "KotlinScript\n" +
                "  Line\n" +
                "    ExpressionStatement\n" +
                "      ColorLiteral\n" +
                "        BlueColor\n" +
                "          T[Color]\n" +
                "          T[.]\n" +
                "          T[Blue]\n" +
                "    T[<EOF>]\n"
        val actual = toParseTree(parseResourceScript("blueColor")).multiLineString()
        assertEquals(expected, actual)
    }

    @Test
    fun parseCustomFontWeight(){
        val expected = "KotlinScript\n" +
                "  Line\n" +
                "    ExpressionStatement\n" +
                "      FontWeightLiteral\n" +
                "        CustomWeight\n" +
                "          T[FontWeight]\n" +
                "          T[(]\n" +
                "          T[200]\n" +
                "          T[)]\n" +
                "    T[<EOF>]\n"
        val actual = toParseTree(parseResourceScript("customFontWeight")).multiLineString()
        assertEquals(expected, actual)
    }

    @Test
    fun parseTextWithColorProperties(){
        val expected = "KotlinScript\n" +
                "  Line\n" +
                "    ExpressionStatement\n" +
                "      ComposableCallExpression\n" +
                "        TextComposable\n" +
                "          T[Text]\n" +
                "          T[(]\n" +
                "          StringLiteralExpression\n" +
                "            StringLiteral\n" +
                "              LineStringLiteral\n" +
                "                T[\"]\n" +
                "                LineStringContent\n" +
                "                  T[Music]\n" +
                "                T[\"]\n" +
                "          T[,]\n" +
                "          ColorParameter\n" +
                "            T[color]\n" +
                "            T[=]\n" +
                "            BlueColor\n" +
                "              T[Color]\n" +
                "              T[.]\n" +
                "              T[Blue]\n" +
                "          T[)]\n" +
                "    T[<EOF>]\n"
        val actual = toParseTree(parseResourceScript("textWithColor")).multiLineString()
        assertEquals(expected, actual)
    }

    @Test
    fun parseTextWithColorAndFontProperties(){
        val expected = "KotlinScript\n" +
                "  Line\n" +
                "    ExpressionStatement\n" +
                "      ComposableCallExpression\n" +
                "        TextComposable\n" +
                "          T[Text]\n" +
                "          T[(]\n" +
                "          StringLiteralExpression\n" +
                "            StringLiteral\n" +
                "              LineStringLiteral\n" +
                "                T[\"]\n" +
                "                LineStringContent\n" +
                "                  T[Music]\n" +
                "                T[\"]\n" +
                "          T[,]\n" +
                "          ColorParameter\n" +
                "            T[color]\n" +
                "            T[=]\n" +
                "            BlueColor\n" +
                "              T[Color]\n" +
                "              T[.]\n" +
                "              T[Blue]\n" +
                "          T[,]\n" +
                "          FontWeightParameter\n" +
                "            T[fontWeight]\n" +
                "            T[=]\n" +
                "            BoldFontWeight\n" +
                "              T[FontWeight]\n" +
                "              T[.]\n" +
                "              T[Bold]\n" +
                "          T[)]\n" +
                "    T[<EOF>]\n"
        val actual = toParseTree(parseResourceScript("textWithFontWeightAndColor")).multiLineString()
        assertEquals(expected, actual)
    }

    @Test
    fun parseImageComposebale(){
        val expected = "KotlinScript\n" +
                "  Line\n" +
                "    ExpressionStatement\n" +
                "      ComposableCallExpression\n" +
                "        ImageComposable\n" +
                "          T[Image]\n" +
                "          T[(]\n" +
                "          PainterParameter\n" +
                "            T[painter]\n" +
                "            T[=]\n" +
                "            PainterResource\n" +
                "              T[painterResource]\n" +
                "              T[(]\n" +
                "              PainterResourceParameter\n" +
                "                T[id]\n" +
                "                T[=]\n" +
                "                DrawableResource\n" +
                "                  T[getResources]\n" +
                "                  T[(]\n" +
                "                  T[)]\n" +
                "                  T[.]\n" +
                "                  T[getIdentifier]\n" +
                "                  T[(]\n" +
                "                  StringLiteralExpression\n" +
                "                    StringLiteral\n" +
                "                      LineStringLiteral\n" +
                "                        T[\"]\n" +
                "                        LineStringContent\n" +
                "                          T[nome-immagine-test]\n" +
                "                        T[\"]\n" +
                "                  T[,]\n" +
                "                  StringLiteralExpression\n" +
                "                    StringLiteral\n" +
                "                      LineStringLiteral\n" +
                "                        T[\"]\n" +
                "                        LineStringContent\n" +
                "                          T[drawable]\n" +
                "                        T[\"]\n" +
                "                  T[,]\n" +
                "                  T[context]\n" +
                "                  T[.]\n" +
                "                  T[getPackageName]\n" +
                "                  T[(]\n" +
                "                  T[)]\n" +
                "                  T[)]\n" +
                "              T[)]\n" +
                "          T[)]\n" +
                "    T[<EOF>]\n"
        val actual = toParseTree(parseResourceScript("imageComposable")).multiLineString()
        assertEquals(expected, actual)
    }

    @Test
    fun parseImageComposebaleWithModifiers(){
        val expected = "KotlinScript\n" +
                "  Line\n" +
                "    ExpressionStatement\n" +
                "      ComposableCallExpression\n" +
                "        ImageComposable\n" +
                "          T[Image]\n" +
                "          T[(]\n" +
                "          PainterParameter\n" +
                "            T[painter]\n" +
                "            T[=]\n" +
                "            PainterResource\n" +
                "              T[painterResource]\n" +
                "              T[(]\n" +
                "              PainterResourceParameter\n" +
                "                T[id]\n" +
                "                T[=]\n" +
                "                DrawableResource\n" +
                "                  T[getResources]\n" +
                "                  T[(]\n" +
                "                  T[)]\n" +
                "                  T[.]\n" +
                "                  T[getIdentifier]\n" +
                "                  T[(]\n" +
                "                  StringLiteralExpression\n" +
                "                    StringLiteral\n" +
                "                      LineStringLiteral\n" +
                "                        T[\"]\n" +
                "                        LineStringContent\n" +
                "                          T[nome-immagine-test]\n" +
                "                        T[\"]\n" +
                "                  T[,]\n" +
                "                  StringLiteralExpression\n" +
                "                    StringLiteral\n" +
                "                      LineStringLiteral\n" +
                "                        T[\"]\n" +
                "                        LineStringContent\n" +
                "                          T[drawable]\n" +
                "                        T[\"]\n" +
                "                  T[,]\n" +
                "                  T[context]\n" +
                "                  T[.]\n" +
                "                  T[getPackageName]\n" +
                "                  T[(]\n" +
                "                  T[)]\n" +
                "                  T[)]\n" +
                "              T[)]\n" +
                "          T[,]\n" +
                "          Resizable\n" +
                "            T[modifier]\n" +
                "            T[=]\n" +
                "            T[Modifier]\n" +
                "            T[.]\n" +
                "            T[fillMaxSize]\n" +
                "            T[(]\n" +
                "            T[)]\n" +
                "          T[,]\n" +
                "          ContentScaleFillWidth\n" +
                "            T[contentScale]\n" +
                "            T[=]\n" +
                "            T[ContentScale]\n" +
                "            T[.]\n" +
                "            T[FillWidth]\n" +
                "          T[)]\n" +
                "    T[<EOF>]\n"
        val actual = toParseTree(parseResourceScript("imageComposableWithFillMaxSizeAndContentScale")).multiLineString()
        assertEquals(expected, actual)
    }

    @Test
    fun testAnnotation(){
        val expected = "KotlinFile\n" +
                "  Declaration\n" +
                "    FunctionDeclaration\n" +
                "      Annotation\n" +
                "        T[@]\n" +
                "        T[Composable]\n" +
                "      T[\n" +
                "]\n" +
                "      T[fun]\n" +
                "      T[test]\n" +
                "      FunctionValueParameters\n" +
                "        T[(]\n" +
                "        T[)]\n" +
                "      FunctionBody\n" +
                "        Block\n" +
                "          T[{]\n" +
                "          T[\n" +
                "]\n" +
                "          T[}]\n" +
                "  T[<EOF>]\n"
        val actual = toParseTree(parseResource("functionComposable")).multiLineString()
        assertEquals(expected, actual)
    }



    @Test
    fun parseKotlinButton(){
        val expected = "KotlinScript\n" +
                "  Line\n" +
                "    ExpressionStatement\n" +
                "      ComposableCallExpression\n" +
                "        IconButtonComposable\n" +
                "          T[Button]\n" +
                "          T[(]\n" +
                "          T[onClick]\n" +
                "          T[=]\n" +
                "          FunctionBody\n" +
                "            Block\n" +
                "              T[{]\n" +
                "              T[}]\n" +
                "          T[)]\n" +
                "          Block\n" +
                "            T[{]\n" +
                "            T[}]\n" +
                "    T[<EOF>]\n"
        val actual = toParseTree(parseResourceScript("button")).multiLineString()
        assertEquals(expected, actual)
    }

    @Test
    fun parseComposableDivider(){
        val expected = "KotlinScript\n" +
                "  Line\n" +
                "    ExpressionStatement\n" +
                "      ComposableCallExpression\n" +
                "        DividerComposable\n" +
                "          T[Divider]\n" +
                "          T[(]\n" +
                "          T[)]\n" +
                "    T[<EOF>]\n"
        val actual = toParseTree(parseResourceScript("divider")).multiLineString()
        assertEquals(expected, actual)
    }

    @Test
    fun parseComposableDividerWithThicknes(){
        val expected = "KotlinScript\n" +
                "  Line\n" +
                "    ExpressionStatement\n" +
                "      ComposableCallExpression\n" +
                "        DividerComposable\n" +
                "          T[Divider]\n" +
                "          T[(]\n" +
                "          DividerTicknessParamater\n" +
                "            T[thickness]\n" +
                "            T[=]\n" +
                "            DpLiteral\n" +
                "              T[8]\n" +
                "              T[.]\n" +
                "              T[dp]\n" +
                "          T[)]\n" +
                "    T[<EOF>]\n"
        val actual = toParseTree(parseResourceScript("dividerWithThickness")).multiLineString()
        assertEquals(expected, actual)
    }

    @Test
    fun parseSpacer(){
        val expected = "KotlinScript\n" +
                "  Line\n" +
                "    ExpressionStatement\n" +
                "      ComposableCallExpression\n" +
                "        SpacerComposable\n" +
                "          T[Spacer]\n" +
                "          T[(]\n" +
                "          T[)]\n" +
                "    T[<EOF>]\n"
        val actual = toParseTree(parseResourceScript("spacer")).multiLineString()
        assertEquals(expected, actual)
    }

    @Test
    fun parseSpacerWithSize(){
        val expected = "KotlinScript\n" +
                "  Line\n" +
                "    ExpressionStatement\n" +
                "      ComposableCallExpression\n" +
                "        SpacerComposable\n" +
                "          T[Spacer]\n" +
                "          T[(]\n" +
                "          ModifierParameter\n" +
                "            T[modifier]\n" +
                "            T[=]\n" +
                "            Modifier\n" +
                "              T[Modifier]\n" +
                "              T[.]\n" +
                "              HeightSuffix\n" +
                "                T[height]\n" +
                "                T[(]\n" +
                "                DpLiteral\n" +
                "                  T[16]\n" +
                "                  T[.]\n" +
                "                  T[dp]\n" +
                "                T[)]\n" +
                "              T[.]\n" +
                "              WidthSuffix\n" +
                "                T[width]\n" +
                "                T[(]\n" +
                "                DpLiteral\n" +
                "                  T[8]\n" +
                "                  T[.]\n" +
                "                  T[dp]\n" +
                "                T[)]\n" +
                "          T[)]\n" +
                "    T[<EOF>]\n"
        val actual = toParseTree(parseResourceScript("spacerWithSize")).multiLineString()
        assertEquals(expected, actual)
    }

    @Test
    fun parseBoxWithModifierZIndex(){
        val expected = "KotlinScript\n" +
                "  Line\n" +
                "    ExpressionStatement\n" +
                "      ComposableCallExpression\n" +
                "        BoxComposable\n" +
                "          T[Box]\n" +
                "          T[(]\n" +
                "          ModifierParameter\n" +
                "            T[modifier]\n" +
                "            T[=]\n" +
                "            Modifier\n" +
                "              T[Modifier]\n" +
                "              T[.]\n" +
                "              ZIndexSuffix\n" +
                "                T[zIndex]\n" +
                "                T[(]\n" +
                "                IntLiteral\n" +
                "                  T[1]\n" +
                "                T[)]\n" +
                "          T[)]\n" +
                "    T[<EOF>]\n"
        val actual = toParseTree(parseResourceScript("box")).multiLineString()
        assertEquals(expected, actual)
    }
}