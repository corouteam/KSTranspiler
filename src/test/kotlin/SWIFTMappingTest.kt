package it.poliba.KSTranspiler

import com.google.gson.Gson
import it.poliba.KSTranspiler.KotlinParser.BoolLiteralContext
import it.poliba.KSTranspiler.parsing.KotlinParserFacade
import it.poliba.KSTranspiler.parsing.KotlinParserFacadeScript
import it.poliba.KSTranspiler.parsing.SwiftParserFacade
import it.poliba.KSTranspiler.parsing.SwiftParserFacadeScript
import it.poliba.KSranspiler.*
import org.antlr.v4.runtime.ParserRuleContext
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.awt.Color

class SWIFTMappingTest {
    private fun mapResource(
        code: String,
    ): KotlinFile {
        val result = SwiftParserFacade.parse(code)

        if (result.isCorrect()) {
            return result.root?.toAst() ?: throw Exception("ParserRuleContext was null")
        } else {
            throw result.errors.first()
        }
    }

    private fun mapResourceScript(
        code: String,
    ): KotlinScript {
        val result = SwiftParserFacadeScript.parse(code)

        if (result.isCorrect()) {
            return result.root?.toAst() ?: throw Exception("ParserRuleContext was null")
        } else {
            throw result.errors.first()
        }
    }

    @Test
    fun mapSimpleVarAssignmentString(){
        val code = "var a = \"hello\""
        val ast = KotlinParserFacade.parse(code).root!!.toAst()
        val expectedAst = KotlinFile(listOf(
            PropertyDeclaration("a", StringType(),StringLit("hello"), mutable = true)
        ))
        assertEquals(expectedAst, ast)
    }

    @Test
    fun mapSimpleVarAssignment(){
        val code = "var a = 3"
        val ast = KotlinParserFacade.parse(code).root!!.toAst()
        val expectedAst = KotlinFile(listOf(
            PropertyDeclaration("a", IntType(),IntLit("3"), mutable = true)
        ))
        assertEquals(expectedAst, ast)
    }

    @Test
    fun mapSimpleValAssignment(){
        val code = "val a = 3"
        val ast = KotlinParserFacade.parse(code).root!!.toAst()
        val expectedAst = KotlinFile(listOf(
            PropertyDeclaration("a", IntType(), IntLit("3"), mutable = false)
        ))
        assertEquals(expectedAst, ast)
    }

    @Test
    fun mapSimpleValAssignmentExplicitType(){
        val code = "val a: Int = 3"
        val ast = KotlinParserFacade.parse(code).root!!.toAst()
        val expectedAst = KotlinFile(listOf(
            PropertyDeclaration("a", IntType(), IntLit("3"), mutable = false)
        ))
        assertEquals(expectedAst, ast)
    }

    @Test
    fun funSimple(){
        val code = "fun test(x: Int, y: Int)\t{\tprint(\"ciao\")}"
        val ast = KotlinParserFacade.parse(code).root!!.toAst()
        val expectedAst = KotlinFile(listOf(
           FunctionDeclaration("test", listOf(FunctionParameter("x", IntType()), FunctionParameter("y", IntType())), null, Block(
               listOf(Print(StringLit("ciao")))
           ))
        ))
        assertEquals(Gson().toJson(expectedAst), Gson().toJson(ast))
    }

    @Test
    fun funExpression(){
        val code = "fun test(x: Int, y: Int) = 3"
        val ast = KotlinParserFacade.parse(code).root!!.toAst()
        val expectedAst = KotlinFile(listOf(
            FunctionDeclaration("test", listOf(FunctionParameter("x", IntType()), FunctionParameter("y", IntType())), IntType(), Block(
                listOf(ReturnExpression(IntLit("3")))
            ))
        ))
        assertEquals(Gson().toJson(expectedAst), Gson().toJson(ast))
    }

    @Test
    fun funExpressionReturn(){
        val code = "fun test(x: Int, y: Int): Int { return 3 }"
        val ast = KotlinParserFacade.parse(code).root!!.toAst()
        val expectedAst = KotlinFile(listOf(
            FunctionDeclaration("test", listOf(FunctionParameter("x", IntType()), FunctionParameter("y", IntType())), IntType(), Block(
                listOf(ReturnExpression(IntLit("3")))
            ))
        ))
        assertEquals(Gson().toJson(expectedAst), Gson().toJson(ast))
    }


    @Test
    fun mapPrint() {
        val code = "print('a')"
        val ast = KotlinParserFacadeScript.parse(code).root!!.toAst()
        val expectedAst = KotlinScript(listOf(
            Print(VarReference("a", StringType() ))
        ))
        assertEquals(expectedAst, ast)
    }

    @Test
    fun mapAssignment() {
        val code = "a = 5"
        val ast = KotlinParserFacadeScript.parse(code).root!!.toAst()
        val expectedAst = KotlinScript(listOf(
            Assignment("a", IntLit("5"))
        ))
        assertEquals(expectedAst, ast)
    }

    @Test
    fun mapIf() {
        val code = "if(true) print(\"Hello world\")"
        val ast = KotlinParserFacadeScript.parse(code).root!!
            .toAst()
        val expectedAst = KotlinScript(listOf(
            IfExpression(BoolLit("true"), Print(StringLit("Hello world")), elseBranch = null)
        ))
        assertEquals(expectedAst, ast)
    }

    @Test
    fun mapIfElse() {
        val code = "if(true){" +
                "print(\"Hello world\")" +
                "}else{" +
                "print(\"Bye world\")" +
                "}"
        val ast = KotlinParserFacadeScript.parse(code).root!!.toAst()
        val expectedAst = KotlinScript(listOf(
            IfExpression(BoolLit("true"), Block(listOf(Print(StringLit("Hello world")))), elseBranch = Block(listOf(Print(StringLit("Bye world")))))
        ))
        assertEquals(expectedAst, ast)
    }

    @Test
    fun mapIfElseIf() {
        val code = "if(true){" +
                "print(\"Hello world\")" +
                "}else if(false){" +
                "print(\"Bye world\")" +
                "}"
        val ast = KotlinParserFacadeScript.parse(code).root!!.toAst()
        val expectedAst = KotlinScript(listOf(
            IfExpression(BoolLit("true"), Block(listOf(Print(StringLit("Hello world")))), elseBranch = IfExpression(BoolLit("false"), Block(listOf(Print(StringLit("Bye world")))), null))))
        assertEquals(expectedAst, ast)
    }
    @Test
    fun mapIfElseNonBlock() {
        val code = "if(true)" +
                "print(\"Hello world\")" +
                "else " +
                "print(\"Bye world\")" +
                ""
        val ast = KotlinParserFacadeScript.parse(code).root!!.toAst()
        val expectedAst = KotlinScript(listOf(
            IfExpression(BoolLit("true"),Print(StringLit("Hello world")), elseBranch = Print(StringLit("Bye world")))))
        assertEquals(expectedAst, ast)
    }

    @Test
    fun mapTextComposable(){
        val code = "Text(\"Hello world\")"
        val ast = KotlinParserFacadeScript.parse(code).root!!.toAst()
        val expectedAst = KotlinScript(listOf(
            TextComposableCall(StringLit("Hello world"), null, null)
        ))
        assertEquals(Gson().toJson(expectedAst), Gson().toJson(ast))
        //val expectedAst = KotlinScript
    }

    @Test
    fun mapTextComposableThrowsIfNotString(){
        val code = "Text(2)"
        val expected = IllegalArgumentException("String expected in Text composable")
        var res: Exception? = null
        try{
            val ast = KotlinParserFacadeScript.parse(code).root!!.toAst()
        }catch (e: Exception){
            res = e
        }
        assertEquals(res.toString(), expected.toString())
    }

    @Test
    fun mapTextComposableWithParams(){
        val code = "Text(\"Hello world\").bold().foregroundColor(Color.blue)"
        val ast = mapResourceScript(code)
        val expectedAst = KotlinScript(listOf(
            TextComposableCall(StringLit("Hello world"), ColorBlue(), FontWeightBold())
        ))
        assertEquals(Gson().toJson(expectedAst), Gson().toJson(ast))
    }

    @Test
    fun mapTextComposableRef(){
        val code = "Text(greet)"
        val ast = SwiftParserFacadeScript.parse(code).root!!.toAst()
        val expectedAst = KotlinScript(listOf(
            TextComposableCall(VarReference("greet", type = StringType()), null, null)
        ))
        assertEquals(Gson().toJson(expectedAst), Gson().toJson(ast))
        //val expectedAst = KotlinScript
    }


}