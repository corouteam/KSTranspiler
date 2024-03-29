package it.poliba.KSTranspiler
import it.poliba.KSTranspiler.facade.KotlinAntlrParserFacade
import it.poliba.KSTranspiler.facade.KotlinParserFacade
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

    @Test
    fun parseAdditionAssignment() {
        assertEquals(
            """KotlinScript
  Line
    AssignmentStatement
      Assignment
        VarReference
          Identifier
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
            toParseTree(parseResource("addition_assignment")).multiLineString())
    }

    @Test
    fun parsePropertyDeclVarInt() {
        assertEquals(
            """KotlinScript
  Line
    PropertyDeclarationStatement
      PropertyDeclaration
        VarDeclaration
          T[var]
          Identifier
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
            """KotlinScript
  Line
    PropertyDeclarationStatement
      PropertyDeclaration
        VarDeclaration
          T[var]
          Identifier
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
            """KotlinScript
  Line
    PropertyDeclarationStatement
      PropertyDeclaration
        VarDeclaration
          T[var]
          Identifier
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
            """KotlinScript
  Line
    PropertyDeclarationStatement
      PropertyDeclaration
        VarDeclaration
          T[var]
          Identifier
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
            """KotlinScript
  Line
    PropertyDeclarationStatement
      PropertyDeclaration
        VarDeclaration
          T[var]
          Identifier
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
            """KotlinScript
  Line
    PropertyDeclarationStatement
      PropertyDeclaration
        ValDeclaration
          T[val]
          Identifier
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
                    Identifier
                      T[a]
                  T[=]
                  IntLiteral
                    T[5]
              T[}]
    T[<EOF>]
""",
            toParseTree(parseResource("ifDeclaration")).multiLineString())
    }

    @Test
    fun parseForDeclaration() {
        assertEquals(
            """KotlinScript
  Line
    ExpressionStatement
      ForExpression
        For
          T[for]
          T[(]
          Identifier
            T[i]
          T[in]
          RangeExpression
            IntLiteral
              T[1]
            T[..]
            IntLiteral
              T[10]
          T[)]
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
            toParseTree(parseResource("forDeclaration")).multiLineString())
    }
    @Test
    fun parseSimpleFun(){
        var expected = """KotlinScript
  Line
    Declaration
      FunctionDeclaration
        T[fun]
        Identifier
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
        var expected = """KotlinScript
  Line
    Declaration
      FunctionDeclaration
        T[fun]
        Identifier
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
        var expected = """KotlinScript
  Line
    ExpressionStatement
      ComplexExpression
        FunctionCallExpression
          Identifier
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
"""
        val actual = toParseTree(parseResource("function_call")).multiLineString()
        assertEquals(expected, actual)
    }

    @Test
    fun parseRangeExpression() {
        val expected = """KotlinScript
  Line
    PropertyDeclarationStatement
      PropertyDeclaration
        ValDeclaration
          T[val]
          Identifier
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

        val actual = toParseTree(parseResource("listof_expression")).multiLineString()
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
        val actual = toParseTree(parseResource("textComposable")).multiLineString()
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
        val actual = toParseTree(parseResource("customColor")).multiLineString()
        assertEquals(expected, actual)
    }
    @Test
    fun parseBlackColor(){
        val expected = "KotlinScript\n" +
                "  Line\n" +
                "    ExpressionStatement\n" +
                "      ColorLiteral\n" +
                "        BlackColor\n" +
                "          T[Color]\n" +
                "          T[.]\n" +
                "          T[Black]\n" +
                "    T[<EOF>]\n"
        val actual = toParseTree(parseResource("blackColor")).multiLineString()
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
        val actual = toParseTree(parseResource("blueColor")).multiLineString()
        assertEquals(expected, actual)
    }

    @Test
    fun parseCyanColor(){
        val expected = "KotlinScript\n" +
                "  Line\n" +
                "    ExpressionStatement\n" +
                "      ColorLiteral\n" +
                "        CyanColor\n" +
                "          T[Color]\n" +
                "          T[.]\n" +
                "          T[Cyan]\n" +
                "    T[<EOF>]\n"
        val actual = toParseTree(parseResource("cyanColor")).multiLineString()
        assertEquals(expected, actual)
    }

    @Test
    fun parseGrayColor(){
        val expected = "KotlinScript\n" +
                "  Line\n" +
                "    ExpressionStatement\n" +
                "      ColorLiteral\n" +
                "        GrayColor\n" +
                "          T[Color]\n" +
                "          T[.]\n" +
                "          T[Gray]\n" +
                "    T[<EOF>]\n"
        val actual = toParseTree(parseResource("grayColor")).multiLineString()
        assertEquals(expected, actual)
    }

    @Test
    fun parseGreenColor(){
        val expected = "KotlinScript\n" +
                "  Line\n" +
                "    ExpressionStatement\n" +
                "      ColorLiteral\n" +
                "        GreenColor\n" +
                "          T[Color]\n" +
                "          T[.]\n" +
                "          T[Green]\n" +
                "    T[<EOF>]\n"
        val actual = toParseTree(parseResource("greenColor")).multiLineString()
        assertEquals(expected, actual)
    }

    @Test
    fun parseRedColor(){
        val expected = "KotlinScript\n" +
                "  Line\n" +
                "    ExpressionStatement\n" +
                "      ColorLiteral\n" +
                "        RedColor\n" +
                "          T[Color]\n" +
                "          T[.]\n" +
                "          T[Red]\n" +
                "    T[<EOF>]\n"
        val actual = toParseTree(parseResource("redColor")).multiLineString()
        assertEquals(expected, actual)
    }

    @Test
    fun parseWhiteColor(){
        val expected = "KotlinScript\n" +
                "  Line\n" +
                "    ExpressionStatement\n" +
                "      ColorLiteral\n" +
                "        WhiteColor\n" +
                "          T[Color]\n" +
                "          T[.]\n" +
                "          T[White]\n" +
                "    T[<EOF>]\n"
        val actual = toParseTree(parseResource("whiteColor")).multiLineString()
        assertEquals(expected, actual)
    }

    @Test
    fun parseYellowColor(){
        val expected = "KotlinScript\n" +
                "  Line\n" +
                "    ExpressionStatement\n" +
                "      ColorLiteral\n" +
                "        YellowColor\n" +
                "          T[Color]\n" +
                "          T[.]\n" +
                "          T[Yellow]\n" +
                "    T[<EOF>]\n"
        val actual = toParseTree(parseResource("yellowColor")).multiLineString()
        assertEquals(expected, actual)
    }

    @Test
    fun parseCustomFontWeight(){
        val expected = """KotlinScript
  Line
    ExpressionStatement
      FontWeightLiteral
        CustomWeight
          T[FontWeight]
          T[(]
          IntLiteral
            T[200]
          T[)]
    T[<EOF>]

""".trimIndent()
        val actual = toParseTree(parseResource("customFontWeight")).multiLineString()
        assertEquals(expected, actual)
    }

    @Test
    fun parseTextWithColorProperties(){
        val expected = """KotlinScript
  Line
    ExpressionStatement
      ComposableCallExpression
        TextComposable
          T[Text]
          T[(]
          StringLiteralExpression
            StringLiteral
              LineStringLiteral
                T["]
                LineStringContent
                  T[Music]
                T["]
          T[,]
          ColorParameter
            T[color]
            T[=]
            ColorLiteral
              BlueColor
                T[Color]
                T[.]
                T[Blue]
          T[)]
    T[<EOF>]
"""
        val actual = toParseTree(parseResource("textWithColor")).multiLineString()
        assertEquals(expected, actual)
    }

    @Test
    fun parseTextWithColorAndFontProperties(){
        val expected = """KotlinScript
  Line
    ExpressionStatement
      ComposableCallExpression
        TextComposable
          T[Text]
          T[(]
          StringLiteralExpression
            StringLiteral
              LineStringLiteral
                T["]
                LineStringContent
                  T[Music]
                T["]
          T[,]
          ColorParameter
            T[color]
            T[=]
            ColorLiteral
              BlueColor
                T[Color]
                T[.]
                T[Blue]
          T[,]
          FontWeightParameter
            T[fontWeight]
            T[=]
            FontWeightLiteral
              BoldFontWeight
                T[FontWeight]
                T[.]
                T[Bold]
          T[)]
    T[<EOF>]

        """.trimIndent()
        val actual = toParseTree(parseResource("textWithFontWeightAndColor")).multiLineString()
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
                "            T[painterResource]\n" +
                "            T[(]\n" +
                "            T[id]\n" +
                "            T[=]\n" +
                "            Resource\n" +
                "              T[getResources]\n" +
                "              T[(]\n" +
                "              T[)]\n" +
                "              T[.]\n" +
                "              T[getIdentifier]\n" +
                "              T[(]\n" +
                "              StringLiteralExpression\n" +
                "                StringLiteral\n" +
                "                  LineStringLiteral\n" +
                "                    T[\"]\n" +
                "                    LineStringContent\n" +
                "                      T[nome-immagine-test]\n" +
                "                    T[\"]\n" +
                "              T[,]\n" +
                "              StringLiteralExpression\n" +
                "                StringLiteral\n" +
                "                  LineStringLiteral\n" +
                "                    T[\"]\n" +
                "                    LineStringContent\n" +
                "                      T[drawable]\n" +
                "                    T[\"]\n" +
                "              T[,]\n" +
                "              T[context]\n" +
                "              T[.]\n" +
                "              T[getPackageName]\n" +
                "              T[(]\n" +
                "              T[)]\n" +
                "              T[)]\n" +
                "            T[)]\n" +
                "          T[)]\n" +
                "    T[<EOF>]\n"
        val actual = toParseTree(parseResource("imageComposable")).multiLineString()
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
                "          T[\n" +
                "]\n" +
                "          PainterParameter\n" +
                "            T[painter]\n" +
                "            T[=]\n" +
                "            T[painterResource]\n" +
                "            T[(]\n" +
                "            T[id]\n" +
                "            T[=]\n" +
                "            Resource\n" +
                "              T[getResources]\n" +
                "              T[(]\n" +
                "              T[)]\n" +
                "              T[.]\n" +
                "              T[getIdentifier]\n" +
                "              T[(]\n" +
                "              StringLiteralExpression\n" +
                "                StringLiteral\n" +
                "                  LineStringLiteral\n" +
                "                    T[\"]\n" +
                "                    LineStringContent\n" +
                "                      T[nome-immagine-test]\n" +
                "                    T[\"]\n" +
                "              T[,]\n" +
                "              StringLiteralExpression\n" +
                "                StringLiteral\n" +
                "                  LineStringLiteral\n" +
                "                    T[\"]\n" +
                "                    LineStringContent\n" +
                "                      T[drawable]\n" +
                "                    T[\"]\n" +
                "              T[,]\n" +
                "              T[context]\n" +
                "              T[.]\n" +
                "              T[getPackageName]\n" +
                "              T[(]\n" +
                "              T[)]\n" +
                "              T[)]\n" +
                "            T[)]\n" +
                "          T[,]\n" +
                "          T[\n" +
                "]\n" +
                "          ModifierImageParameter\n" +
                "            ModifierParameter\n" +
                "              T[modifier]\n" +
                "              T[=]\n" +
                "              Modifier\n" +
                "                T[Modifier]\n" +
                "                T[.]\n" +
                "                ResizableSuffix\n" +
                "                  T[fillMaxSize]\n" +
                "                  T[(]\n" +
                "                  T[)]\n" +
                "          T[,]\n" +
                "          T[\n" +
                "]\n" +
                "          ContentScale\n" +
                "            T[contentScale]\n" +
                "            T[=]\n" +
                "            ContentScaleExpression\n" +
                "              T[ContentScale]\n" +
                "              T[.]\n" +
                "              ContentScaleFillWidth\n" +
                "                T[FillWidth]\n" +
                "          T[)]\n" +
                "    T[<EOF>]\n"
        val actual = toParseTree(parseResource("imageComposableWithFillMaxSizeAndContentScale")).multiLineString()
        assertEquals(expected, actual)
    }

    @Test
    fun testAnnotation(){
        val expected = """KotlinScript
  Line
    Declaration
      FunctionDeclaration
        Annotation
          T[@]
          Identifier
            T[Composable]
        T[
]
        T[fun]
        Identifier
          T[test]
        FunctionValueParameters
          T[(]
          T[)]
        FunctionBody
          Block
            T[{]
            T[
]
            T[}]
    T[<EOF>]

""".trimIndent()
        val actual = toParseTree(parseResource("functionComposable")).multiLineString()
        assertEquals(expected, actual)
    }



    @Test
    fun parseKotlinButton(){
        val expected = """KotlinScript
  Line
    ExpressionStatement
      ComposableCallExpression
        IconButtonComposable
          T[Button]
          T[(]
          Identifier
            T[onClick]
          T[=]
          FunctionBody
            Block
              T[{]
              T[}]
          T[)]
          Block
            T[{]
            T[}]
    T[<EOF>]

""".trimIndent()
        val actual = toParseTree(parseResource("button")).multiLineString()
        assertEquals(expected, actual)
    }

    @Test
    fun parseComposableDivider(){
        val expected = """KotlinScript
  Line
    ExpressionStatement
      ComplexExpression
        FunctionCallExpression
          Identifier
            T[DividerComposable]
          FunctionCallParameters
            T[(]
            T[)]
    T[<EOF>]

""".trimIndent()
        val actual = toParseTree(parseResource("divider")).multiLineString()
        assertEquals(expected, actual)
    }

    /*@Test
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
        val actual = toParseTree(parseResource("dividerWithThickness")).multiLineString()
        assertEquals(expected, actual)
    }*/

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
        val actual = toParseTree(parseResource("spacer")).multiLineString()
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
        val actual = toParseTree(parseResource("spacerWithSize")).multiLineString()
        assertEquals(expected, actual)
    }


    @Test
    fun parseColumn(){
        val expected = "KotlinScript\n" +
                "  Line\n" +
                "    ExpressionStatement\n" +
                "      ComposableCallExpression\n" +
                "        ColumnComposable\n" +
                "          T[Column]\n" +
                "          T[(]\n" +
                "          T[\n" +
                "]\n" +
                "          VerticalArrangementParameter\n" +
                "            T[verticalArrangement]\n" +
                "            T[=]\n" +
                "            ArrangementExpression\n" +
                "              Arrangement\n" +
                "                T[Arrangement]\n" +
                "                T[.]\n" +
                "                T[spacedBy]\n" +
                "                T[(]\n" +
                "                DpLiteral\n" +
                "                  T[8]\n" +
                "                  T[.]\n" +
                "                  T[dp]\n" +
                "                T[)]\n" +
                "          T[,]\n" +
                "          T[\n" +
                "]\n" +
                "          HorizontalAlignmentParameter\n" +
                "            T[horizontalAlignment]\n" +
                "            T[=]\n" +
                "            HorizhontalAlignmentExpression\n" +
                "              StartAlignment\n" +
                "                T[Alignment]\n" +
                "                T[.]\n" +
                "                T[Start]\n" +
                "          T[\n" +
                "]\n" +
                "          T[)]\n" +
                "    T[<EOF>]\n"
        val actual = toParseTree(parseResource("column")).multiLineString()
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
        val actual = toParseTree(parseResource("box")).multiLineString()
        assertEquals(expected, actual)
    }


}