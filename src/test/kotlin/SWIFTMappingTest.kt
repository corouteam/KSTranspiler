package it.poliba.KSTranspiler

import com.google.gson.Gson
import it.poliba.KSTranspiler.facade.*
import it.poliba.KSTranspiler.facade.SwiftAntlrParserFacade
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.test.assertIs

class SWIFTMappingTest {
    @Test
    fun mapFunction(){
        val code = "func test() -> Int { return 3 }"
        val ast = SwiftAntlrParserFacade.parse(code).root?.toAst()
        val expectedAst = AstScript(listOf(
            FunctionDeclaration("test", listOf(), IntType(), Block(
                listOf(ReturnExpression(IntLit("3"))))
            )
        ))
        assertEquals(Gson().toJson(expectedAst), Gson().toJson(ast))
        //val expectedAst = KotlinScript
    }

    @Test
    fun mapTextComposable() {
        val code = "Text(\"Hello world\")"
        val ast = SwiftAntlrParserFacade.parse(code).root?.toAst()
        val expectedAst = AstScript(
            listOf(
                TextComposableCall(StringLit("Hello world"), null, null)
            )
        )
        assertEquals(Gson().toJson(expectedAst), Gson().toJson(ast))
        //val expectedAst = KotlinScript
    }

    @Test
    fun mapTextComposableThrowsIfNotString() {
        val code = "Text(2)"
        val expected = IllegalArgumentException("String expected in Text composable")
        var res: Exception? = null
        try {
            val ast = SwiftAntlrParserFacade.parse(code).root?.toAst()
        } catch (e: Exception) {
            res = e
        }
        assertEquals(res.toString(), expected.toString())
    }

    @Test
    fun mapTextComposableWithParams() {
        val code = "Text(\"Hello world\").foregroundColor(Color.blue).fontWeight(Font.Weight.bold)"
        val ast = SwiftAntlrParserFacade.parse(code).root?.toAst()
        val expectedAst = AstScript(
            listOf(
                TextComposableCall(StringLit("Hello world"), ColorBlue(), FontWeightBold())
            )
        )
        assertEquals(Gson().toJson(expectedAst), Gson().toJson(ast))
    }

    @Test
    fun mapTextComposableRef() {
        val code = "Text(greet)"
        val ast = SwiftAntlrParserFacade.parse(code).root?.toAst()
        val expectedAst = AstScript(
            listOf(
                TextComposableCall(VarReference("greet", type = StringType()), null, null)
            )
        )
        assertEquals(Gson().toJson(expectedAst), Gson().toJson(ast))
        //val expectedAst = KotlinScript
    }

    @Test
    fun mapIfWithLogical() {
        val code = "if(true && false || true && 1==1 && 2!=3 && 4<5 && 6>7 && 8<=9 && 10>=11) print(\"Hello world\")"
        val ast = KotlinAntlrParserFacadeScript.parse(code).root?.toAst()
        val expectedAst = AstScript(
            listOf(IfExpression(condition=GTEqualExpression(left=LTEqualExpression(left=GreaterThanExpression(left=LessThanExpression(left=AndExpression(left=AndExpression(left=AndExpression(left=OrExpression(left=AndExpression(left=BoolLit(value="true", position=null), right=BoolLit(value="false", position=null), position=null), right=BoolLit(value="true", position=null), position=null), right=EqualExpression(left=IntLit(value="1", position=null), right=IntLit(value="1", position=null), position=null), position=null), right=NotEqualExpression(left=IntLit(value="2", position=null), right=IntLit(value="3", position=null), position=null), position=null), right=IntLit(value="4", position=null), position=null), right=AndExpression(left=IntLit(value="5", position=null), right=IntLit(value="6", position=null), position=null), position=null), right=AndExpression(left=IntLit(value="7", position=null), right=IntLit(value="8", position=null), position=null), position=null), right=AndExpression(left=IntLit(value="9", position=null), right=IntLit(value="10", position=null), position=null), position=null), right=IntLit(value="11", position=null), position=null), body=Print(value=StringLit(value="Hello world", position=null), position=null), elseBranch=null, position=null)
            )
        )
        assertEquals(expectedAst, ast)
    }

    @Test
    fun mapImageComposable(){
        val code = "Image(\"nome-immagine-test\")"
        val ast = SwiftAntlrParserFacade.parse(code).root?.toAst()
        val expectedAst = AstScript(listOf(
            ImageComposableCall(StringLit("nome-immagine-test"), false, null)
        ))
        assertEquals(Gson().toJson(expectedAst), Gson().toJson(ast))
    }

    @Test
    fun mapImageComposableWithParams(){
        val code = "Image(\"nome-immagine-test\").resizable().aspectRatio(contentMode: ContentMode.fit)"
        val ast = SwiftAntlrParserFacade.parse(code).root?.toAst()
        val expectedAst = AstScript(listOf(
            ImageComposableCall(StringLit("nome-immagine-test"),true, ContentFit())
        ))
        assertEquals(Gson().toJson(expectedAst), Gson().toJson(ast))
    }
    @Test
    fun mapFor(){
        val code = "for i in 1...42 {\n" +
                "    print(\"Hello world\")\n" +
                "}"
        val ast = SwiftAntlrParserFacade.parse(code).root?.toAst()
        val expectedAst = AstScript(listOf(
            ForExpression(varName="i", range=RangeExpression(leftExpression=IntLit(value="1", position=null), rightExpression=IntLit(value="42", position=null), type=RangeType(type=IntType())),Block(
                listOf(
                    Print(StringLit("Hello world"))
                )
            ))
        ))
        assertEquals(expectedAst,ast)
    }

    @Test
    fun mapWidget() {
        val code = "struct MainView: View {\n" +
                "var name: String\n" +
                "    var body: some View {\n" +
                "        Text(\"Ciao\")\n" +
                "        Text(\"Ciao\")\n" +
                "    }\n" +
                "}"
        val ast = SwiftAntlrParserFacade.parse(code).root?.toAst()
        val expectedAst = AstScript(
            listOf(
                WidgetDeclaration(
                    "MainView", listOf(FunctionParameter("name", StringType())), Block(
                        listOf(
                            TextComposableCall(StringLit("Ciao"), null, null),
                            TextComposableCall(StringLit("Ciao"), null, null)
                        )
                    )
                )
            )
        )

        assertEquals(Gson().toJson(expectedAst), Gson().toJson(ast))

    }

    @Test
    fun mapSpacerComposableRef() {
        val code = "Spacer()"
        val ast = KotlinAntlrParserFacade.parse(code).root?.toAst()

        val expectedAst = AstScript(
            listOf(
                SpacerComposableCall(null)
            )
        )

        val spacer = (ast?.statement?.first() as SpacerComposableCall)

        assert(spacer.size == null)
    }

    @Test
    fun testDividerSwiftUI() {
        val code = "Divider()"
        val ast = SwiftAntlrParserFacade.parse(code).root?.toAst()
        val expectedAst = AstScript(
            listOf(
                DividerComposableCall(null, null)
            )
        )
        val divider = (ast?.statement?.first() as DividerComposableCall)
        assertEquals(null, divider.color)
        assert(divider.frame == null)

    }

    @Test
    fun testDividerWithOverlaySwiftUI() {
        val code = "Divider().overlay(Color.blue)"
        val ast = SwiftAntlrParserFacade.parse(code).root?.toAst()
        val expectedAst = AstScript(
            listOf(
                DividerComposableCall(null, ColorBlue())
            )
        )
        val divider = (ast?.statement?.first() as DividerComposableCall)
        assert(divider.color is ColorBlue)
        assert(divider.frame == null)
    }

    @Test
    fun testDividerWithOverlayAndThicknessSwiftUI() {
        val code = "Divider().frame(height: 4.0).overlay(Color.blue)"
        val ast = SwiftAntlrParserFacade.parse(code).root?.toAst()
        val expectedAst = AstScript(
            listOf(
                DividerComposableCall(Frame(width = null, height = DpLit("4.0")), ColorBlue())
            )
        )
        val divider = (ast?.statement?.first() as DividerComposableCall)
        assert(divider.color is ColorBlue)
        assert(divider.frame != null)
        assert(divider.frame?.height != null)
        assert(divider.frame?.height is DpLit)
    }

    @Test
    fun mapVStack() {
        val code = "VStack(alignment: HorizontalAlignment.leading, spacing: 10)"
        val ast = SwiftAntlrParserFacade.parse(code).root?.toAst()
        val expectingSpacing = DpLit("10")

        val column = ast?.statement?.first() as? ColumnComposableCall
        val spacing = column?.spacing as? DpLit
        val alignment = column?.horizontalAlignment as? HorizontalAlignment

        assertEquals(expectingSpacing, spacing)
        assertEquals(StartAlignment(), alignment)
    }

    @Test
    fun parseCGFloat() {
        val code = "let margin = CGFloat(8)"
        val ast = SwiftAntlrParserFacade.parse(code).root?.toAst()
        val expected = AstScript(
            listOf(
                PropertyDeclaration(
                    "margin",
                    DpType(),
                    DpLit("8"),
                    null,
                    false
                )
            )
        )

        assertEquals(expected, ast)
    }

    @Test
    fun parseScrollView() {
        val code = "ScrollView(.vertical){}"
        val ast = SwiftAntlrParserFacade.parse(code).root?.toAst()

        val column = ast?.statement?.first() as? ColumnComposableCall

        assertEquals(true, column?.scrollable)

    }

    @Test
    fun parseScrollHorizontalView() {
        val code = "ScrollView(.horizontal){}"
        val ast = SwiftAntlrParserFacade.parse(code).root?.toAst()

        val column = ast?.statement?.first() as? RowComposableCall

        assertEquals(true, column?.scrollable)
    }

    @Test
    fun testSpacerWithFrameSwiftUI() {
        val code = "Spacer().frame(width: 54.0, height: 54.0)"
        val ast = SwiftAntlrParserFacade.parse(code).root?.toAst()

        val divider = (ast?.statement?.first() as SpacerComposableCall)

        assert(divider.size != null)
        assert(divider.size?.height is DpLit)
        assert(divider.size?.width is DpLit)
    }

    @Test
    fun parseHStack() {
        val code = """
            HStack(
                spacing: 10,
                alignment: VerticalAlignment.center
            ){
                
            }
        """.trimIndent()
        val ast = SwiftAntlrParserFacade.parse(code).root?.toAst()
        val expectingSpacing = DpLit("10")

        val column = ast?.statement?.first() as? RowComposableCall
        val spacing = column?.spacing as? DpLit
        val alignment = column?.verticalAlignment as? VerticalAlignment

        assertEquals(expectingSpacing, spacing)
        assertEquals(CenterVerticallyAlignment(), alignment)
    }

    @Test
    fun mapZStackRef(){
        val code = """
             ZStack {
                Text("Ciao")
                Text("Ciao2")
                Text("Ciao3")
            }
        """.trimIndent()
        val ast = SwiftAntlrParserFacade.parse(code).root?.toAst()

        val zstack = (ast?.statement?.first() as ZStackComposableCall)
        val block = zstack.body as Block
        val text = block.body.first() as TextComposableCall

        val text2 = block.body[1] as TextComposableCall
        val text3 = block.body[2] as TextComposableCall


        assertEquals(text.zIndex, IntLit("0"))
        assertEquals(text2.zIndex, IntLit("1"))
        assertEquals(text3.zIndex, IntLit("2"))


    }

  @Test
    fun mapButtonComposableRef() {
        val code = "Button( action: {} ) { }"
        val ast = SwiftAntlrParserFacade.parse(code).root?.toAst()
        val expectedAst = AstScript(
            listOf(
                ButtonComposableCall(action = Block(body = listOf()), body = Block(listOf()))
            )
        )
        assertEquals(Gson().toJson(expectedAst), Gson().toJson(ast))
        //val expectedAst = KotlinScript
    }

    @Test
    fun mapButtonComposable() {
        val code = """
            Button( action = {
                print("Ok")
            }){
                Text("Ciao") 
            }""".trimMargin()
        val ast = KotlinAntlrParserFacade.parse(code).root?.toAst()
        val expectedAst = AstScript(
            listOf(
                ButtonComposableCall(
                    action = Block(body = listOf(Print(StringLit("Ok")))), body = Block(
                        body = listOf(
                            TextComposableCall(StringLit("Ciao"), color = null, fontWeight = null)
                        )
                    )
                )
            )
        )
        assertEquals(Gson().toJson(expectedAst), Gson().toJson(ast))
    }

    @Test
    fun mapClassDeclaration() {
        val code = """
        class Person: Address{
             init(firstName: String, lastName: String){
               print("Hello world")
            }
        }""".trimMargin()
        val ast = SwiftAntlrParserFacade.parse(code).root?.toAst()



        val classDecl = ast?.statement?.first() as ClassDeclaration
        var decl = classDecl.baseClasses.get(0)
        var constructor = classDecl.body.first() as PrimaryConstructor
        assertEquals("Person", classDecl.name)
        assertIs<UserType>(decl)
        assertEquals(2, constructor.parameters.count())

    }

    @Test
    fun mapClassDeclarationComplex() {
        val code = """
        class Person: Address{
            var firstName: String
            var lastName: String
            
            init(firstName: String, lastName: String){
               print("Hello world")
               self.firstName = firstName
               self.lastName = lastName
            }
            
            func testFunction(){
                self.sayHello()
            }
            
            func sayHello(){
                print("Hello world")
            }
            
        }""".trimMargin()
        val ast = SwiftAntlrParserFacade.parse(code).root?.toAst()



        val classDecl = ast?.statement?.first() as ClassDeclaration
        var decl = classDecl.baseClasses.get(0)
        var constructor = classDecl.body.get(2) as PrimaryConstructor
        var constructorBlock = (constructor.body as Block)
        assertEquals("Person", classDecl.name)
        assertIs<UserType>(decl)
        assertEquals(2, constructor.parameters.count())
        assertEquals(3, constructorBlock.body.count())

    }

    @Test
    fun mapStructDeclarationComplex() {
        val code = """
        struct Person: Address{
            var firstName: String
            var lastName: String
            
            func testFunction(){
                self.sayHello()
            }
            
            func sayHello(){
                print("Hello world")
            }
            
        }""".trimMargin()
        val ast = SwiftAntlrParserFacade.parse(code).root?.toAst()



        val classDecl = ast?.statement?.first() as DataClassDeclaration
        var decl = classDecl.baseClasses.get(0)

        assertEquals("Person", classDecl.name)
        assertIs<UserType>(decl)
        assertEquals(2, classDecl.propertyList.count())

    }
}
