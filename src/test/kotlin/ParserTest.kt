package it.poliba.KSTranspiler
import it.poliba.KSTranspiler.parsing.KotlinParserFacade
import org.antlr.v4.runtime.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import toParseTree
import it.poliba.KSTranspiler.tools.ErrorHandler.attachErrorHandler

class KotlinParserTest {
    private fun lexerForResource(resourceName: String) =
        it.poliba.KSTranspiler.KotlinLexer(ANTLRInputStream(this.javaClass.getResourceAsStream("/${resourceName}.txt")))
            .attachErrorHandler()

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

    @Test
    fun parseAdditionAssignment() {
        assertEquals(
            """KotlinFile
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
            toParseTree(parseResource("addition_assignment")).multiLineString())
    }

    @Test
    fun parsePropertyDeclVarInt() {
        assertEquals(
            """KotlinFile
  Line
    PropertyDeclarationStatement
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
  Line
    PropertyDeclarationStatement
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
  Line
    PropertyDeclarationStatement
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
  Line
    PropertyDeclarationStatement
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
  Line
    PropertyDeclarationStatement
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
  Line
    PropertyDeclarationStatement
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
            """KotlinFile
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
            toParseTree(parseResource("ifDeclaration")).multiLineString())
    }
}