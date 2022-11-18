package it.poliba.KSTranspiler
import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CommonTokenStream
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import toParseTree
import java.io.*
import java.util.*

class KotlinParserTest {

    fun lexerForResource(resourceName: String) =
        it.poliba.KSTranspiler.KotlinLexer(ANTLRInputStream(this.javaClass.getResourceAsStream("/${resourceName}.txt")))
    fun parseResource(resourceName: String) : KotlinParser.KotlinFileContext = it.poliba.KSTranspiler.KotlinParser(
        CommonTokenStream(lexerForResource(resourceName))
    ).kotlinFile()

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
    /*
   @Test
   fun parsePrecedenceExpressions() {
        assertEquals(
            """KotlinFile
  Line
    VarDeclarationStatement
      VarDeclaration
        T[var]
        Assignment
          T[a]
          T[=]
          BinaryOperation
            BinaryOperation
              IntLiteral
                T[1]
              T[+]
              BinaryOperation
                IntLiteral
                  T[2]
                T[*]
                IntLiteral
                  T[3]
            T[-]
            IntLiteral
              T[4]
    T[<EOF>]
""",
            toParseTree(parseResource("precedence_expression")).multiLineString())
    }*/
}