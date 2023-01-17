package it.poliba.KSTranspiler
import it.poliba.KSTranspiler.facade.KotlinAntlrParserFacade
import it.poliba.KSTranspiler.facade.KotlinAntlrParserFacadeScript
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
    fun parseArrayOfExpression() {
        assertEquals(
            """KotlinScript
  Line
    ExpressionStatement
      ArrayExpression
        T[arrayOf]
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
""",
            toParseTree(parseResourceScript("array_expression")).multiLineString())
    }
    @Test
    fun parseWhileLoop() {
        assertEquals(
            """KotlinScript
    Line
        WhileStatement
            T[while]
            T[(]
            BinaryOperation
            IntLiteral
                T[1]
            T[<]
            IntLiteral
                T[2]
            T[)]
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
                      T[Hello World!]
                    T["]
              T[)]
          T[
]
          T[}]
    T[<EOF>]
""",
            toParseTree(parseResourceScript("while_loop.txt")).multiLineString())
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
          T[
]
          T[}]
  T[<EOF>]
"""
        val actual = toParseTree(parseResource("function_declaration_return")).multiLineString()
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

}