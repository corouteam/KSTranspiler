package it.poliba.KSTranspiler
import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CommonTokenStream
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import toParseTree
import java.io.*
import java.util.*

class SandyParserTest {

    fun lexerForResource(resourceName: String) =
        it.poliba.KSTranspiler.SandyLexer(ANTLRInputStream(this.javaClass.getResourceAsStream("/${resourceName}.sandy")))
    fun parseResource(resourceName: String) : SandyParser.SandyFileContext = it.poliba.KSTranspiler.SandyParser(
        CommonTokenStream(lexerForResource(resourceName))
    ).sandyFile()

    @Test
    fun parseAdditionAssignment() {
        assertEquals(
            """SandyFile
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
            """SandyFile
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
            """SandyFile
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
            """SandyFile
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