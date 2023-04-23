package it.poliba.KSTranspiler

import com.google.gson.Gson
import it.poliba.KSTranspiler.SwiftParser.BlockContext
import it.poliba.KSTranspiler.facade.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.reflect.jvm.internal.impl.types.CustomTypeParameter
import kotlin.test.assertIs

class MappingTest {

    @Test
    fun mapSimpleVarAssignmentString() {
        val code = "var a = \"hello\""
        val ast = KotlinAntlrParserFacade.parse(code).root?.toAst()
        val expectedAst = AstScript(
            listOf(
                PropertyDeclaration("a", StringType(), StringLit("hello"), mutable = true)
            )
        )
        assertEquals(expectedAst, ast)
    }

    @Test
    fun mapSimpleVarAssignment() {
        val code = "var a = 3"
        val ast = KotlinAntlrParserFacade.parse(code).root?.toAst()
        val expectedAst = AstScript(
            listOf(
                PropertyDeclaration("a", IntType(), IntLit("3"), mutable = true)
            )
        )
        assertEquals(expectedAst, ast)
    }

    @Test
    fun mapSimpleValAssignment() {
        val code = "val a = 3"
        val ast = KotlinAntlrParserFacade.parse(code).root?.toAst()
        val expectedAst = AstScript(
            listOf(
                PropertyDeclaration("a", IntType(), IntLit("3"), mutable = false)
            )
        )
        assertEquals(expectedAst, ast)
    }

    @Test
    fun mapSimpleValAssignmentExplicitType() {
        val code = "val a: Int = 3"
        val ast = KotlinAntlrParserFacade.parse(code).root?.toAst()
        val expectedAst = AstScript(
            listOf(
                PropertyDeclaration("a", IntType(), IntLit("3"), mutable = false)
            )
        )
        assertEquals(expectedAst, ast)
    }

    @Test
    fun funSimple() {
        val code = "fun test(x: Int, y: Int)\t{\tprint(\"ciao\")}"
        val ast = KotlinAntlrParserFacade.parse(code).root?.toAst()
        val expectedAst = AstScript(
            listOf(
                FunctionDeclaration(
                    "test", listOf(FunctionParameter("x", IntType()), FunctionParameter("y", IntType())), null, Block(
                        listOf(Print(StringLit("ciao")))
                    )
                )
            )
        )
        assertEquals(Gson().toJson(expectedAst), Gson().toJson(ast))
    }

    @Test
    fun funExpression() {
        val code = "fun test(x: Int, y: Int) = 3"
        val ast = KotlinAntlrParserFacade.parse(code).root?.toAst()
        val expectedAst = AstScript(
            listOf(
                FunctionDeclaration(
                    "test",
                    listOf(FunctionParameter("x", IntType()), FunctionParameter("y", IntType())),
                    IntType(),
                    Block(
                        listOf(ReturnExpression(IntLit("3")))
                    )
                )
            )
        )
        assertEquals(Gson().toJson(expectedAst), Gson().toJson(ast))
    }

    @Test
    fun funExpressionReturn() {
        val code = "fun test(x: Int, y: Int): Int { return 3 }"
        val ast = KotlinAntlrParserFacade.parse(code).root?.toAst()
        val expectedAst = AstScript(
            listOf(
                FunctionDeclaration(
                    "test",
                    listOf(FunctionParameter("x", IntType()), FunctionParameter("y", IntType())),
                    IntType(),
                    Block(
                        listOf(ReturnExpression(IntLit("3")))
                    )
                )
            )
        )
        assertEquals(Gson().toJson(expectedAst), Gson().toJson(ast))
    }

    @Test
    fun funExpressionReturnMultiline() {
        val code = """fun test(x: Int, y: Int): Int { 
            print("Ciao")
            return 3
         }""".trimMargin()
        val ast = KotlinAntlrParserFacade.parse(code).root?.toAst()
        val expectedAst = AstScript(
            listOf(
                FunctionDeclaration(
                    "test",
                    listOf(FunctionParameter("x", IntType()), FunctionParameter("y", IntType())),
                    IntType(),
                    Block(
                        listOf(
                            Print(StringLit("Ciao")),
                            ReturnExpression(IntLit("3"))
                        )
                    )
                )
            )
        )
        assertEquals(Gson().toJson(expectedAst), Gson().toJson(ast))
    }

    @Test
    fun functionCall() {
        val code = "test(\"hello\", \"world\", 42, a)"
        val ast = KotlinAntlrParserFacade.parse(code).root?.toAst()
        val expectedAst = AstScript(
            listOf(
                FunctionCall(
                    "test", listOf(
                        StringLit("hello"),
                        StringLit("world"),
                        IntLit("42"),
                        VarReference("a", type = StringType())
                    )
                )
            )
        )
        assertEquals(Gson().toJson(expectedAst), Gson().toJson(ast))
    }


    @Test
    fun mapPrint() {
        val code = "print(a)"
        val ast = KotlinAntlrParserFacade.parse(code).root?.toAst()
        val expectedAst = AstScript(
            listOf(
                Print(VarReference("a", StringType()))
            )
        )
        assertEquals(Gson().toJson(expectedAst), Gson().toJson(ast))
    }

    @Test
    fun mapAssignment() {
        val code = "a = 5"
        val ast = KotlinAntlrParserFacade.parse(code).root?.toAst()
        val expectedAst = AstScript(
            listOf(
                Assignment(VarReference("a", StringType()), IntLit("5"))
            )
        )
        var assigment = ast?.statement?.first() as Assignment
        assertEquals("a", assigment.variable.generateKotlinCode())
        assertEquals("5", assigment.value.generateKotlinCode())
    }

    @Test
    fun mapIf() {
        val code = "if(true) print(\"Hello world\")"
        val ast = KotlinAntlrParserFacade.parse(code).root?.toAst()
        val expectedAst = AstScript(
            listOf(
                IfExpression(BoolLit("true"), Print(StringLit("Hello world")), elseBranch = null)
            )
        )
        assertEquals(expectedAst, ast)
    }

    @Test
    fun mapIfElse() {
        val code = "if(true){" +
                "print(\"Hello world\")" +
                "}else{" +
                "print(\"Bye world\")" +
                "}"
        val ast = KotlinAntlrParserFacade.parse(code).root?.toAst()
        val expectedAst = AstScript(
            listOf(
                IfExpression(
                    BoolLit("true"), Block(listOf(Print(StringLit("Hello world")))), elseBranch = Block(
                        listOf(
                            Print(StringLit("Bye world"))
                        )
                    )
                )
            )
        )
        assertEquals(expectedAst, ast)
    }

    @Test
    fun mapIfElseIf() {
        val code = "if(true){" +
                "print(\"Hello world\")" +
                "}else if(false){" +
                "print(\"Bye world\")" +
                "}"
        val ast = KotlinAntlrParserFacade.parse(code).root?.toAst()
        val expectedAst = AstScript(
            listOf(
                IfExpression(
                    BoolLit("true"), Block(listOf(Print(StringLit("Hello world")))), elseBranch = IfExpression(
                        BoolLit("false"), Block(listOf(Print(StringLit("Bye world")))), null
                    )
                )
            )
        )
        assertEquals(expectedAst, ast)
    }

    @Test
    fun mapIfElseNonBlock() {
        val code = "if(true)" +
                "print(\"Hello world\")" +
                "else " +
                "print(\"Bye world\")" +
                ""
        val ast = KotlinAntlrParserFacade.parse(code).root?.toAst()
        val expectedAst = AstScript(
            listOf(
                IfExpression(
                    BoolLit("true"),
                    Print(StringLit("Hello world")),
                    elseBranch = Print(StringLit("Bye world"))
                )
            )
        )
        assertEquals(expectedAst, ast)
    }
    @Test
    fun mapFor() {
        val code = "for(i in 1..42) print(\"Hello world\")"
        val ast = KotlinAntlrParserFacade.parse(code).root?.toAst()
        val expectedAst = AstScript(listOf(
            ForExpression("i", RangeExpression(leftExpression= IntLit(value="1"), rightExpression= IntLit(value="42", position=null), type= RangeType(type= IntType())), Print(StringLit("Hello world")))
        ))
        assertEquals(expectedAst, ast)
    }
    @Test
    fun mapForBlock() {
        val code = "for(i in 1..42){" +
                "print(\"Hello world\")" +
                "}"
        val ast = KotlinAntlrParserFacade.parse(code).root?.toAst()
        val expectedAst = AstScript(listOf(
            ForExpression("i", RangeExpression(leftExpression= IntLit(value="1"), rightExpression= IntLit(value="42", position=null), type= RangeType(type= IntType())), Block(listOf(Print(StringLit("Hello world")))))
        ))
        assertEquals(expectedAst, ast)
    }

    @Test
    fun mapIfWithLogical() {
        val code = "if(true && false || true && 1==1 && 2!=3 && 4<5 && 6>7 && 8<=9 && 10>=11) print(\"Hello world\")"
        val ast = KotlinAntlrParserFacade.parse(code).root?.toAst()
        val expectedAst = AstScript(
            listOf(IfExpression(condition=GTEqualExpression(left=LTEqualExpression(left=GreaterThanExpression(left=LessThanExpression(left=AndExpression(left=AndExpression(left=AndExpression(left=OrExpression(left=AndExpression(left=BoolLit(value="true", position=null), right=BoolLit(value="false", position=null), position=null), right=BoolLit(value="true", position=null), position=null), right=EqualExpression(left=IntLit(value="1", position=null), right=IntLit(value="1", position=null), position=null), position=null), right=NotEqualExpression(left=IntLit(value="2", position=null), right=IntLit(value="3", position=null), position=null), position=null), right=IntLit(value="4", position=null), position=null), right=AndExpression(left=IntLit(value="5", position=null), right=IntLit(value="6", position=null), position=null), position=null), right=AndExpression(left=IntLit(value="7", position=null), right=IntLit(value="8", position=null), position=null), position=null), right=AndExpression(left=IntLit(value="9", position=null), right=IntLit(value="10", position=null), position=null), position=null), right=IntLit(value="11", position=null), position=null), body=Print(value=StringLit(value="Hello world", position=null), position=null), elseBranch=null, position=null)
            )
        )
        assertEquals(expectedAst, ast)
    }

    @Test
    fun mapRangeExpression() {
        val code = "val a = 1..42"
        val ast = KotlinAntlrParserFacade.parse(code).root?.toAst()
        val expectedAst = AstScript(
            listOf(
                PropertyDeclaration(
                    varName = "a",
                    type = RangeType(type = IntType()),
                    value = RangeExpression(
                        leftExpression = IntLit(value = "1"),
                        rightExpression = IntLit(value = "42", position = null),
                        type = RangeType(type = IntType())
                    ),
                    mutable = false
                )
            )
        )

        assertEquals(expectedAst, ast)
    }

    @Test
    fun mapListOfExpression() {
        val code = "listOf<Int>(1, 2, 3)"
        val ast = KotlinAntlrParserFacade.parse(code).root?.toAst()
        val expectedAst = AstScript(
            listOf(
                ListExpression(
                    itemsType = IntType(position = null),
                    items = listOf(IntLit("1"), IntLit("2"), IntLit("3"))
                )
            )
        )

        assertEquals(expectedAst, ast)
    }


    @Test
    fun mapTextComposable() {
        val code = "Text(\"Hello world\")"
        val ast = KotlinAntlrParserFacade.parse(code).root?.toAst()
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
            val ast = KotlinAntlrParserFacade.parse(code).root?.toAst()
        } catch (e: Exception) {
            res = e
        }
        assertEquals(res.toString(), expected.toString())
    }

    @Test
    fun mapTextComposableWithParams() {
        val code = "Text(\"Hello world\", color = Color.Blue, fontWeight = FontWeight.Bold)"
        val ast = KotlinAntlrParserFacade.parse(code).root?.toAst()
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
        val ast = KotlinAntlrParserFacade.parse(code).root?.toAst()

        val expectedAst = AstScript(
            listOf(
                TextComposableCall(VarReference("greet", type = StringType()), null, null)
            )
        )
        assertEquals(Gson().toJson(expectedAst), Gson().toJson(ast))
        //val expectedAst = KotlinScript
    }

    @Test
    fun convertComposableFunction() {
        val code = "@Composable\nfun test(x: Int, y: Int) { Text(\"Hello\") }"
        val ast = KotlinAntlrParserFacade.parse(code).root?.toAst()
        val body = listOf(
            TextComposableCall(StringLit("Hello"), null, null)
        )
        val expectedAst = AstScript(
            listOf(
                WidgetDeclaration(
                    "test",
                    listOf(FunctionParameter("x", IntType()), FunctionParameter("y", IntType())),
                    Block(body)
                )
            )
        )
        assertEquals(Gson().toJson(expectedAst), Gson().toJson(ast))
    }

    @Test
    fun convertComposableDivider() {
        val code = "Divider()"
        val ast = KotlinAntlrParserFacade.parse(code).root?.toAst()

        val expectedAst = AstScript(
            listOf(
                DividerComposableCall(null, null)
            )
        )
        assertEquals(Gson().toJson(expectedAst), Gson().toJson(ast))
    }

    @Test
    fun convertComposableDividerWithThickness() {
        val code = "Divider(thickness = 8.dp)"
        val ast = KotlinAntlrParserFacade.parse(code).root?.toAst()

        val divider = (ast?.statement?.first() as DividerComposableCall)

        assertEquals(null, divider.color)
        assert(divider.frame != null)
        assert(divider.frame?.height != null)
        assert(divider.frame?.height is DpLit)
    }

    @Test
    fun convertComposableDividerWithThicknessAndColor() {
        val code = "Divider(thickness = 8.dp, color = Color.Blue)"
        val ast = KotlinAntlrParserFacade.parse(code).root?.toAst()

        val divider = (ast?.statement?.first() as DividerComposableCall)

        assert(divider.color is ColorBlue)
        assert(divider.frame != null)
        assert(divider.frame?.height != null)
        assert(divider.frame?.height is DpLit)
    }

    @Test
    fun convertComposableDividerWithThicknessAndRedColor() {
        val code = "Divider(thickness = 8.dp, color = Color.Red)"
        val ast = KotlinAntlrParserFacade.parse(code).root?.toAst()

        val divider = (ast?.statement?.first() as DividerComposableCall)

        assert(divider.color is ColorRed)
        assert(divider.frame != null)
        assert(divider.frame?.height != null)
        assert(divider.frame?.height is DpLit)
    }

    @Test
    fun convertComposableDividerWithThicknessAndGreenColor() {
        val code = "Divider(thickness = 8.dp, color = Color.Green)"
        val ast = KotlinAntlrParserFacade.parse(code).root?.toAst()

        val divider = (ast?.statement?.first() as DividerComposableCall)

        assert(divider.color is ColorGreen)
        assert(divider.frame != null)
        assert(divider.frame?.height != null)
        assert(divider.frame?.height is DpLit)
    }


    @Test
    fun testSpacerWithHeightComposable() {
        val code = "Spacer(modifier = Modifier.height(16.dp))"
        val ast = KotlinAntlrParserFacade.parse(code).root?.toAst()

        val spacer = (ast?.statement?.first() as SpacerComposableCall)

        assertIs<DpLit>(spacer.size?.height)

    }

    @Test
    fun testSpacerWithHeightAndWidthComposable() {
        val code = "Spacer(modifier = Modifier.height(16.dp).width(8.dp))"
        val ast = KotlinAntlrParserFacade.parse(code).root?.toAst()

        val spacer = (ast?.statement?.first() as SpacerComposableCall)

        assertIs<DpLit>(spacer.size?.height)
        assertIs<DpLit>(spacer.size?.width)
    }

    @Test
    fun testEmptySpacer() {
        val code = "Spacer()"
        val ast = KotlinAntlrParserFacade.parse(code).root?.toAst()
        val expectedAst = AstScript(
            listOf(
                SpacerComposableCall(Frame(width = DoubleLit("54.0"), height = DoubleLit("54.0")))
            )
        )
        val spacer = (ast?.statement?.first() as SpacerComposableCall)

        assert(spacer.size == null)
    }

    @Test
    fun testBox() {
        val code = """
            Box() {
            Text("Ciao", modifier = Modifier.zIndex(2))
            Divider(modifier = Modifier.zIndex(1))
            Spacer(modifier = Modifier.zIndex(3))
            Column(modifier = Modifier.zIndex(4)){}
            Row(modifier = Modifier.zIndex(5)){}
            Box(modifier = Modifier.zIndex(6)){}
            Button(action = {}, modifier = Modifier.zIndex(6)){}
            }
        """.trimIndent()
        val ast = KotlinAntlrParserFacade.parse(code).root?.toAst()

        val zstack = (ast?.statement?.first() as ZStackComposableCall)
        val block = zstack.body as Block
        val text = block.body[1] as TextComposableCall

        val divider = block.body[0] as DividerComposableCall
        val spacer = block.body[2] as SpacerComposableCall
        val column = block.body[3] as ColumnComposableCall
        val row = block.body[4] as RowComposableCall
        val box = block.body[5] as ZStackComposableCall
        val button = block.body[6] as ButtonComposableCall

        assertEquals(text.zIndex, IntLit("2"))
        assertEquals(divider.zIndex, IntLit("1"))
        assertEquals(spacer.zIndex, IntLit("3"))
        assertEquals(column.zIndex, IntLit("4"))
        assertEquals(row.zIndex, IntLit("5"))
        assertEquals(box.zIndex, IntLit("6"))
        assertEquals(button.zIndex, IntLit("6"))
    }

    @Test
    fun mapColumn() {
        val code = """
            Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
             horizontalAlignment = Alignment.Start
     )
        """.trimIndent()
        val ast = KotlinAntlrParserFacade.parse(code).root?.toAst()
        val expectedAst = AstScript(
            listOf(
                ColumnComposableCall(
                    spacing = DpLit("8"),
                    horizontalAlignment = StartAlignment(),
                    scrollable = false,
                    body = ControlStructureBody()
                )
            )
        )
        val column = ast?.statement?.first() as? ColumnComposableCall
        val spacing = column?.spacing as? DpLit
        val alignment = column?.horizontalAlignment as? HorizontalAlignment
        assertEquals(DpLit("8"), spacing)
        assertEquals(StartAlignment(), alignment)

    }

    @Test
    fun mapColumnWEndAlignment() {
        val code = """
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.End
            )
        """.trimIndent()
        val ast = KotlinAntlrParserFacade.parse(code).root?.toAst()
        val expectedAst = AstScript(
            listOf(
                ColumnComposableCall(
                    spacing = DpLit("8"),
                    horizontalAlignment = EndAlignment(),
                    scrollable = false,
                    body = ControlStructureBody()

                )
            )
        )
        val column = ast?.statement?.first() as? ColumnComposableCall
        val spacing = column?.spacing as? DpLit
        val alignment = column?.horizontalAlignment as? HorizontalAlignment
        assertEquals(DpLit("8"), spacing)
        assertEquals(EndAlignment(), alignment)

    }

    @Test
    fun mapColumnScroll() {
        val code = """
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.End
            )
        """.trimIndent()
        val ast = KotlinAntlrParserFacade.parse(code).root?.toAst()
        val expected = ColumnComposableCall(
            spacing = DpLit("8"),
            horizontalAlignment = EndAlignment(),
            scrollable = true,
            body = ControlStructureBody()
        )
        val column = ast?.statement?.first() as? ColumnComposableCall
        val spacing = column?.spacing as? DpLit
        val alignment = column?.horizontalAlignment as? HorizontalAlignment
        assertEquals(DpLit("8"), spacing)
        assertEquals(EndAlignment(), alignment)
        assertEquals(expected.scrollable, column?.scrollable)

    }

    @Test
    fun mapRowScroll() {
        val code = """
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Top
            )
        """.trimIndent()
        val ast = KotlinAntlrParserFacade.parse(code).root?.toAst()

        val column = ast?.statement?.first() as? RowComposableCall
        val spacing = column?.spacing as? DpLit
        val alignment = column?.verticalAlignment as? VerticalAlignment
        assertEquals(DpLit("8"), spacing)
        assertEquals(TopAlignment(), alignment)
        assertEquals(true, column?.scrollable)

    }

    @Test
    fun mapButtonComposableRef() {
        val code = "Button( onClick = {} ) { }"
        val ast = KotlinAntlrParserFacade.parse(code).root?.toAst()
        val expectedAst = AstScript(
            listOf(
                ButtonComposableCall(
                    action = Block(body = listOf()), body = Block(body = listOf(), )
                )
            )
        )
        assertEquals(Gson().toJson(expectedAst), Gson().toJson(ast))
    }

    @Test
    fun mapButtonComposable() {
        val code = """
            Button( onClick = {
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
    fun mapImageComposableRef(){
        val code = "Image(painter = painterResource(id = getResources().getIdentifier(\"nome-immagine-test\", \"drawable\", context.getPackageName())))"
        val ast = KotlinAntlrParserFacade.parse(code).root?.toAst()

        val expectedAst = AstScript(listOf(
            ImageComposableCall(StringLit("nome-immagine-test"), false, null)
        ))
        val image = ast?.statement?.first() as ImageComposableCall
        val name = image.value as StringLit
        assertEquals("nome-immagine-test", name.value)
        assertEquals(false, image.resizable)
        //val expectedAst = KotlinScript
    }

    @Test
    fun mapImageComposableRefWithFillMaxSizeAndContent(){
        val code = """
            Image(
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillWidth,
            painter = painterResource(id = getResources().getIdentifier("nome-immagine-test", "drawable", context.getPackageName())))
        """.trimIndent()

        val ast = KotlinAntlrParserFacade.parse(code).root?.toAst()
        val image = ast?.statement?.first() as ImageComposableCall
        val name = image.value as StringLit
        assertEquals("nome-immagine-test", name.value)
        assertEquals(true, image.resizable)
        assertIs<ContentFill>(image.aspectRatio)

    }

    @Test
    fun mapClassDeclaration() {
        val code = """
        class Person(
    firstName: String,
    lastName: String,
    age: Int,
    address: Address
): Address, Jks {
    val a: String = "Hello"
}
        """.trimMargin()
        val ast = KotlinAntlrParserFacade.parse(code).root?.toAst()


        val classDecl = ast?.statement?.first() as ClassDeclaration



        val dec = classDecl.body.first() as PropertyDeclaration
        var decl = classDecl.baseClasses.get(0)

        assertEquals("Person", classDecl.name)
        assertEquals("a", dec.varName)
        assertIs<UserType>(decl)
    }

    @Test
    fun mapClassDeclaration2() {
        val code = """
        class Person(
        firstName: String,
        lastName: String
        ): Address, Jks {
            init {
                print("Hello")
            }
        }""".trimMargin()
        val ast = KotlinAntlrParserFacade.parse(code).root?.toAst()


        val classDecl = ast?.statement?.first() as ClassDeclaration
        val constructor = classDecl.body.first() as PrimaryConstructor

        assertEquals("Person", classDecl.name)
        assertEquals(2, constructor.parameters.count())

    }

    @Test
    fun parseClassWithInitializer() {
        val code = """
        class Person(
        firstName: String,
        lastName: String
        ): Address, Jks {
        var name: String
        
            init {
                print("Hello")
                this.name.greet = "Hello"
                this.name.greet = this.surname
            }
        }""".trimMargin()
        val ast = KotlinAntlrParserFacade.parse(code).root?.toAst()


        val classDecl = ast?.statement?.first() as ClassDeclaration
        val constructor = classDecl.body[1] as PrimaryConstructor
        val body = constructor.body as Block
        assertEquals("Person", classDecl.name)
        assertEquals(2, constructor.parameters.count())
        assertEquals(3, body.body.count())

    }

    @Test
    fun parseDataClassWithInitializer() {
        val code = """
        data class Person(
        val firstName: String,
        val lastName: String
        ): Address, Jks {
        
        }""".trimMargin()
        val ast = KotlinAntlrParserFacade.parse(code).root?.toAst()


        val classDecl = ast?.statement?.first() as DataClassDeclaration
        assertEquals("Person", classDecl.name)
        assertEquals(2, classDecl.propertyList.count())


    }

}