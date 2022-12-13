package it.poliba.KSTranspiler
import it.poliba.KSTranspiler.parsing.KotlinParserFacade
import it.poliba.KSTranspiler.parsing.KotlinParserFacadeScript
import org.antlr.v4.runtime.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import toParseTree
import it.poliba.KSTranspiler.tools.ErrorHandler.attachErrorHandler

class KotlinParserTest {

    private fun parseResource(
        resourceName: String,
    ): ParserRuleContext {
        val parseResult = KotlinParserFacade
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
        val parseResult = KotlinParserFacadeScript
            .parse(this.javaClass.getResourceAsStream("/${resourceName}.txt"))

        if (parseResult.isCorrect()) {
            return parseResult.root ?: throw Exception("ParserRuleContext was null")
        } else {
            throw parseResult.errors.first()
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


}