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
    fun parseSimplestVarDecl() {
        assertEquals(
            """KotlinFile
  Line
    VarDeclarationStatement
      VarDeclaration
        T[var]
        Assignment
          T[a]
          T[=]
          IntLiteral
            T[1]
    T[<EOF>]
""",
            toParseTree(parseResource("simplest_var_decl")).multiLineString())
    }

    @Test
    fun parseSimplestLetDecl() {
        assertEquals(
            """KotlinFile
  Line
    LetDeclarationStatement
      LetDeclaration
        T[let]
        Assignment
          T[a]
          T[=]
          IntLiteral
            T[1]
    T[<EOF>]
""",
            toParseTree(parseResource("simplest_let_decl")).multiLineString())
    }

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
    }
}