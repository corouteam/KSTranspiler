package it.poliba.KSTranspiler

import com.google.gson.Gson
import it.poliba.KSTranspiler.facade.KotlinAntlrParserFacade
import it.poliba.KSTranspiler.facade.KotlinAntlrParserFacadeScript
import it.poliba.KSTranspiler.facade.KotlinParserFacade
import it.poliba.KSTranspiler.facade.KotlinParserFacadeScript
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MappingTest {

    @Test
    fun mapSimpleVarAssignmentString(){
        val code = "var a = \"hello\""
        val ast = KotlinAntlrParserFacade.parse(code).root?.toAst()
        val expectedAst = AstFile(listOf(
            PropertyDeclaration("a", StringType(), StringLit("hello"), mutable = true)
        ))
        assertEquals(expectedAst, ast)
    }

    @Test
    fun mapSimpleVarAssignment(){
        val code = "var a = 3"
        val ast = KotlinAntlrParserFacade.parse(code).root?.toAst()
        val expectedAst = AstFile(listOf(
            PropertyDeclaration("a", IntType(), IntLit("3"), mutable = true)
        ))
        assertEquals(expectedAst, ast)
    }

    @Test
    fun mapSimpleValAssignment(){
        val code = "val a = 3"
        val ast = KotlinAntlrParserFacade.parse(code).root?.toAst()
        val expectedAst = AstFile(listOf(
            PropertyDeclaration("a", IntType(), IntLit("3"), mutable = false)
        ))
        assertEquals(expectedAst, ast)
    }

    @Test
    fun mapSimpleValAssignmentExplicitType(){
        val code = "val a: Int = 3"
        val ast = KotlinAntlrParserFacade.parse(code).root?.toAst()
        val expectedAst = AstFile(listOf(
            PropertyDeclaration("a", IntType(), IntLit("3"), mutable = false)
        ))
        assertEquals(expectedAst, ast)
    }

    @Test
    fun funSimple(){
        val code = "fun test(x: Int, y: Int)\t{\tprint(\"ciao\")}"
        val ast = KotlinAntlrParserFacade.parse(code).root?.toAst()
        val expectedAst = AstFile(listOf(
           FunctionDeclaration("test", listOf(FunctionParameter("x", IntType()), FunctionParameter("y", IntType())), null, Block(
               listOf(Print(StringLit("ciao")))
           )
           )
        ))
        assertEquals(Gson().toJson(expectedAst), Gson().toJson(ast))
    }

    @Test
    fun funExpression(){
        val code = "fun test(x: Int, y: Int) = 3"
        val ast = KotlinAntlrParserFacade.parse(code).root?.toAst()
        val expectedAst = AstFile(listOf(
            FunctionDeclaration("test", listOf(FunctionParameter("x", IntType()), FunctionParameter("y", IntType())), IntType(), Block(
                listOf(ReturnExpression(IntLit("3")))
            )
            )
        ))
        assertEquals(Gson().toJson(expectedAst), Gson().toJson(ast))
    }

    @Test
    fun funExpressionReturn(){
        val code = "fun test(x: Int, y: Int): Int { return 3 }"
        val ast = KotlinAntlrParserFacade.parse(code).root?.toAst()
        val expectedAst = AstFile(listOf(
            FunctionDeclaration("test", listOf(FunctionParameter("x", IntType()), FunctionParameter("y", IntType())), IntType(), Block(
                listOf(ReturnExpression(IntLit("3")))
            )
            )
        ))
        assertEquals(Gson().toJson(expectedAst), Gson().toJson(ast))
    }

    @Test
    fun funExpressionReturnMultiline(){
        val code = """fun test(x: Int, y: Int): Int { 
            print("Ciao")
            return 3
         }""".trimMargin()
        val ast = KotlinAntlrParserFacade.parse(code).root?.toAst()
        val expectedAst = AstFile(listOf(
            FunctionDeclaration("test", listOf(FunctionParameter("x", IntType()), FunctionParameter("y", IntType())), IntType(), Block(
                listOf(
                    Print(StringLit("Ciao")),
                    ReturnExpression(IntLit("3")))
            )
            )
        ))
        assertEquals(Gson().toJson(expectedAst), Gson().toJson(ast))
    }

    @Test
    fun functionCall(){
        val code = "test(\"hello\", \"world\", 42, a)"
        val ast = KotlinAntlrParserFacadeScript.parse(code).root?.toAst()
        val expectedAst = AstScript(listOf(
            FunctionCall("test", listOf(
                StringLit("hello"),
                StringLit("world"),
                IntLit("42"),
                VarReference("a", type = StringType())
            ))
        ))
        assertEquals(Gson().toJson(expectedAst), Gson().toJson(ast))
    }


    @Test
    fun mapPrint() {
        val code = "print('a')"
        val ast = KotlinAntlrParserFacadeScript.parse(code).root?.toAst()
        val expectedAst = AstScript(listOf(
            Print(VarReference("a", StringType() ))
        ))
        assertEquals(Gson().toJson(expectedAst), Gson().toJson(ast))
    }

    @Test
    fun mapAssignment() {
        val code = "a = 5"
        val ast = KotlinAntlrParserFacadeScript.parse(code).root?.toAst()
        val expectedAst = AstScript(listOf(
            Assignment("a", IntLit("5"))
        ))
        assertEquals(expectedAst, ast)
    }

    @Test
    fun mapIf() {
        val code = "if(true) print(\"Hello world\")"
        val ast = KotlinAntlrParserFacadeScript.parse(code).root?.toAst()
        val expectedAst = AstScript(listOf(
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
        val ast = KotlinAntlrParserFacadeScript.parse(code).root?.toAst()
        val expectedAst = AstScript(listOf(
            IfExpression(
                BoolLit("true"), Block(listOf(Print(StringLit("Hello world")))), elseBranch = Block(listOf(
                    Print(StringLit("Bye world"))
                ))
            )
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
        val ast = KotlinAntlrParserFacadeScript.parse(code).root?.toAst()
        val expectedAst = AstScript(listOf(
            IfExpression(
                BoolLit("true"), Block(listOf(Print(StringLit("Hello world")))), elseBranch = IfExpression(
                    BoolLit("false"), Block(listOf(Print(StringLit("Bye world")))), null)
            )
        ))
        assertEquals(expectedAst, ast)
    }
    @Test
    fun mapIfElseNonBlock() {
        val code = "if(true)" +
                "print(\"Hello world\")" +
                "else " +
                "print(\"Bye world\")" +
                ""
        val ast = KotlinAntlrParserFacadeScript.parse(code).root?.toAst()
        val expectedAst = AstScript(listOf(
            IfExpression(BoolLit("true"), Print(StringLit("Hello world")), elseBranch = Print(StringLit("Bye world")))
        ))
        assertEquals(expectedAst, ast)
    }

    @Test
    fun mapRangeExpression() {
        val code = "val a = 1..42"
        val ast = KotlinAntlrParserFacade.parse(code).root?.toAst()
        val expectedAst = AstFile(listOf(
            PropertyDeclaration(varName="a", type= RangeType(type= IntType()), value= RangeExpression(leftExpression= IntLit(value="1"), rightExpression= IntLit(value="42", position=null), type= RangeType(type= IntType())), mutable=false)
        )
        )

        assertEquals(expectedAst, ast)
    }

    @Test
    fun mapListOfExpression() {
        val code = "listOf<Int>(1, 2, 3)"
        val ast = KotlinAntlrParserFacadeScript.parse(code).root?.toAst()
        val expectedAst = AstScript(listOf(
            ListExpression(
                itemsType= IntType(position=null),
                items=listOf(IntLit("1"), IntLit("2"), IntLit("3")))
        ))

        assertEquals(expectedAst, ast)
    }



    @Test
    fun mapTextComposable(){
        val code = "Text(\"Hello world\")"
        val ast = KotlinAntlrParserFacadeScript.parse(code).root?.toAst()
        val expectedAst = AstScript(listOf(
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
            val ast = KotlinAntlrParserFacadeScript.parse(code).root?.toAst()
        }catch (e: Exception){
            res = e
        }
        assertEquals(res.toString(), expected.toString())
    }

    @Test
    fun mapTextComposableWithParams(){
        val code = "Text(\"Hello world\", color = Color.Blue, fontWeight = FontWeight.Bold)"
        val ast = KotlinAntlrParserFacadeScript.parse(code).root?.toAst()
        val expectedAst = AstScript(listOf(
            TextComposableCall(StringLit("Hello world"), ColorBlue(), FontWeightBold())
        ))
        assertEquals(Gson().toJson(expectedAst), Gson().toJson(ast))
        //val expectedAst = KotlinScript
    }

    @Test
    fun mapTextComposableRef(){
        val code = "Text(greet)"
        val ast = KotlinAntlrParserFacadeScript.parse(code).root?.toAst()

        val expectedAst = AstScript(listOf(
            TextComposableCall(VarReference("greet", type = StringType()), null, null)
        ))
        assertEquals(Gson().toJson(expectedAst), Gson().toJson(ast))
        //val expectedAst = KotlinScript
    }
    @Test
    fun convertComposableFunction(){
        val code = "@Composable\nfun test(x: Int, y: Int) { Text(\"Hello\") }"
        val ast = KotlinAntlrParserFacade.parse(code).root?.toAst()
        val body = listOf(
            TextComposableCall(StringLit("Hello"), null, null)
        )
        val expectedAst = AstFile(listOf(
            WidgetDeclaration(
                "test",
                listOf(FunctionParameter("x", IntType()), FunctionParameter("y", IntType())),
                Block(body)
            )
        ))
        assertEquals(Gson().toJson(expectedAst), Gson().toJson(ast))
    }

    @Test
    fun convertComposableDivider(){
        val code = "Divider()"
        val ast = KotlinAntlrParserFacadeScript.parse(code).root?.toAst()

        val expectedAst = AstScript(listOf(
            DividerComposableCall()
        ))
        assertEquals(Gson().toJson(expectedAst), Gson().toJson(ast))
        //val expectedAst = KotlinScript
    }

    @Test
    fun mapSpacerComposableRef(){
        val code = "Spacer()"
        val ast = KotlinAntlrParserFacadeScript.parse(code).root?.toAst()

        val expectedAst = AstScript(listOf(
            SpacerComposableCall()
        ))
        assertEquals(Gson().toJson(expectedAst), Gson().toJson(ast))
        //val expectedAst = KotlinScript
    }

}