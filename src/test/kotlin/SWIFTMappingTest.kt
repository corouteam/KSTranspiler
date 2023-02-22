package it.poliba.KSTranspiler

import com.google.gson.Gson
import it.poliba.KSTranspiler.facade.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SWIFTMappingTest {
    @Test
    fun mapTextComposable(){
        val code = "Text(\"Hello world\")"
        val ast = SwiftAntlrParserFacadeScript.parse(code).root?.toAst()
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
            val ast = SwiftAntlrParserFacadeScript.parse(code).root?.toAst()
        }catch (e: Exception){
            res = e
        }
        assertEquals(res.toString(), expected.toString())
    }

    @Test
    fun mapTextComposableWithParams(){
        val code = "Text(\"Hello world\").foregroundColor(Color.blue).fontWeight(Font.Weight.bold)"
        val ast = SwiftAntlrParserFacadeScript.parse(code).root?.toAst()
        val expectedAst = AstScript(listOf(
            TextComposableCall(StringLit("Hello world"), ColorBlue(), FontWeightBold())
        ))
        assertEquals(Gson().toJson(expectedAst), Gson().toJson(ast))
    }

    @Test
    fun mapTextComposableRef(){
        val code = "Text(greet)"
        val ast = SwiftAntlrParserFacadeScript.parse(code).root?.toAst()
        val expectedAst = AstScript(listOf(
            TextComposableCall(VarReference("greet", type = StringType()), null, null)
        ))
        assertEquals(Gson().toJson(expectedAst), Gson().toJson(ast))
        //val expectedAst = KotlinScript
    }

    @Test
    fun mapWidget(){
        val code = "struct MainView: View {\n" +
                "var name: String\n" +
                "    var body: some View {\n" +
                "        Text(\"Ciao\")\n" +
                "        Text(\"Ciao\")\n" +
                "    }\n" +
                "}"
        val ast = SwiftAntlrParserFacade.parse(code).root?.toAst()
        val expectedAst = AstFile(listOf(
            WidgetDeclaration("MainView", listOf(FunctionParameter("name", StringType())), Block(listOf(TextComposableCall(StringLit("Ciao"), null, null), TextComposableCall(StringLit("Ciao"), null, null))
            ))))

        assertEquals(Gson().toJson(expectedAst), Gson().toJson(ast))

    }

    @Test
    fun mapSpacerComposableRef(){
        val code = "Spacer()"
        val ast = KotlinAntlrParserFacadeScript.parse(code).root?.toAst()

        val expectedAst = AstScript(listOf(
            SpacerComposableCall(null)
        ))
        assertEquals(Gson().toJson(expectedAst), Gson().toJson(ast))
        //val expectedAst = KotlinScript
    }

    @Test
    fun testDividerSwiftUI() {
        val code = "Divider()"
        val ast = SwiftAntlrParserFacadeScript.parse(code).root?.toAst()
        val expectedAst = AstScript(
            listOf(
                DividerComposableCall(null)
            )
        )
        assertEquals(Gson().toJson(expectedAst), Gson().toJson(ast))
    }

    @Test
    fun mapVStack(){
        val code = "VStack(alignment: HorizontalAlignment.leading, spacing: 10)"
        val ast = SwiftAntlrParserFacadeScript.parse(code).root?.toAst()
        val expectingSpacing = DpLit("10")

        val column = ast?.statement?.first() as? ColumnComposableCall
        val spacing = column?.spacing as? DpLit
        val alignment = column?.horizontalAlignment as? HorizontalAlignment

        assertEquals(expectingSpacing, spacing)
        assertEquals(StartAlignment, alignment)
    }
    @Test
    fun parseCGFloat(){
        val code = "let margin = CGFloat(8)"
        val ast = SwiftAntlrParserFacade.parse(code).root?.toAst()
        val expected = AstFile(declarations = listOf(
            PropertyDeclaration(
                "margin",
                DpType(),
                DpLit("8"),
                null,
                false
            )
        ))

        assertEquals(expected, ast)
    }

   @Test
    fun parseScrollView(){
        val code = "ScrollView(.vertical){}"
        val ast = SwiftAntlrParserFacadeScript.parse(code).root?.toAst()

       val column = ast?.statement?.first() as? ColumnComposableCall

       assertEquals(true, column?.scrollable)

   }

    @Test
    fun parseScrollHorizontalView(){
        val code = "ScrollView(.horizontal){}"
        val ast = SwiftAntlrParserFacadeScript.parse(code).root?.toAst()

        val column = ast?.statement?.first() as? RowComposableCall

        assertEquals(true, column?.scrollable)
    }

    @Test
    fun testSpacerWithFrameSwiftUI(){
        val code = "Spacer().frame(width: 54.0, height: 54.0)"
        val ast = SwiftAntlrParserFacadeScript.parse(code).root?.toAst()
        val expectedAst = AstScript(listOf(
            SpacerComposableCall(Frame(width = DoubleLit("54.0"), height = DoubleLit("54.0")))
        ))
        assertEquals(Gson().toJson(expectedAst), Gson().toJson(ast))

    }
    @Test
    fun parseHStack(){
        val code = """
            HStack(
                spacing: 10,
                alignment: VerticalAlignment.center
            ){
                
            }
        """.trimIndent()
        val ast = SwiftAntlrParserFacadeScript.parse(code).root?.toAst()
        val expectingSpacing = DpLit("10")

        val column = ast?.statement?.first() as? RowComposableCall
        val spacing = column?.spacing as? DpLit
        val alignment = column?.verticalAlignment as? VerticalAlignment

        assertEquals(expectingSpacing, spacing)
        assertEquals(CenterVerticallyAlignment, alignment)
    }
    @Test
    fun mapButtonComposableRef(){
        val code = "Button( action: {} ) { }"
        val ast = SwiftAntlrParserFacadeScript.parse(code).root?.toAst()
        val expectedAst = AstScript(listOf(
            ButtonComposableCall(action = Block(body = listOf()), body = Block(listOf()))
        ))
        assertEquals(Gson().toJson(expectedAst), Gson().toJson(ast))
        //val expectedAst = KotlinScript
    }

    @Test
    fun mapButtonComposable(){
        val code = """
            Button( action = {
                print("Ok")
            }){
                Text("Ciao") 
            }""".trimMargin()
        val ast = KotlinAntlrParserFacadeScript.parse(code).root?.toAst()
        val expectedAst = AstScript(listOf(
            ButtonComposableCall(action = Block(body = listOf(Print(StringLit("Ok")))), body = Block(body = listOf(
                TextComposableCall(StringLit("Ciao"), color = null, fontWeight = null)
            ))
            )))
        assertEquals(Gson().toJson(expectedAst), Gson().toJson(ast))
    }

}