package it.poliba.KSTranspiler

import it.poliba.KSTranspiler.facade.KotlinParserFacade
import it.poliba.KSTranspiler.facade.KotlinParserFacadeScript
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class  OutputTest {

    @Test
    fun convertVarPropertyDefinition(){
        var code = "val a = 5"
        val parseResult = KotlinParserFacade.parse(code).root
        val expected = "let a:Int = 5"
        assertEquals(expected, parseResult?.generateCode())
    }

    @Test
    fun convertDoublePropertyDefinition(){
        var code = "val a = 5.9"
        val parseResult = KotlinParserFacade.parse(code).root!!
        val expected = "let a:Double = 5.9"
        assertEquals(expected, parseResult.generateCode())
    }

    @Test
    fun convertSumPropertyDefinition(){
        var code = "val a = 5 + 7"
        val parseResult = KotlinParserFacade.parse(code).root!!
        val expected = "let a:Int = 5 + 7"
        assertEquals(expected, parseResult.generateCode())
    }

    @Test
    fun convertMinusPropertyDefinition(){
        var code = "val a = 5 - 7"
        val parseResult = KotlinParserFacade.parse(code).root!!
        val expected = "let a:Int = 5 - 7"
        assertEquals(expected, parseResult.generateCode())
    }

    @Test
    fun convertDivisionPropertyDefinition(){
        var code = "val a = 5.0 / 7.0"
        val parseResult = KotlinParserFacade.parse(code).root!!
        val expected = "let a:Double = 5.0 / 7.0"
        assertEquals(expected, parseResult.generateCode())
    }

    @Test
    fun convertAssignmentPropertyDefinition(){

        var code = "a = 5"
        val parseResult = KotlinParserFacadeScript.parse(code).root!!
        val expected = "a = 5"
        assertEquals(expected, parseResult.generateCode())
    }

    @Test
    fun convertPrintPropertyDefinition(){
        var code = "print(\"aa\")"
        val parseResult = KotlinParserFacadeScript.parse(code).root!!
        val expected = "print(\"aa\")"
        assertEquals(expected, parseResult.generateCode())
    }
    @Test
    fun convertIf(){
        val code = "if(true){ print(\"Is true \")}"
        val result = "if(true) {\n\tprint(\"Is true \")\n}"
        val parseResult = KotlinParserFacadeScript.parse(code)
        assertEquals(result, parseResult.root!!.generateCode())

    }

    @Test
    fun convertIfExpression(){
        val code = "if(true) print(\"Is true \")"
        val result = "if(true) print(\"Is true \")"
        val parseResult = KotlinParserFacadeScript.parse(code)
        assertEquals(result, parseResult.root!!.generateCode())

    }
    @Test
    fun convertIfElseExpression(){
        val code = "if(true) print(\"Is true \") else print(\"Is false \")"
        val result = "if(true) print(\"Is true \") else print(\"Is false \")"
        val parseResult = KotlinParserFacadeScript.parse(code)
        assertEquals(result, parseResult.root!!.generateCode())

    }

    @Test
    fun convertIfElse(){
        val code = "if(true) { print(\"Is true \") }else{print(\"Is false\")}"
        val result = "if(true) {\n\tprint(\"Is true \")\n} else {\n\tprint(\"Is false\")\n}"
        val parseResult = KotlinParserFacadeScript.parse(code).root!!
        assertEquals(result, parseResult.generateCode())

    }

    @Test
    fun convertIfElseIf(){
        val code = "if(true) { print(\"Is true \") }else if(false){print(\"Is false\")}"
        val result = "if(true) {\n\tprint(\"Is true \")\n} else if(false) {\n\tprint(\"Is false\")\n}"
        val parseResult = KotlinParserFacadeScript.parse(code).root!!
        assertEquals(result, parseResult.generateCode())
    }

    @Test
    fun convertIfElseIfElse(){
        val code = "if(true) { print(\"Is true \") }else if(false){print(\"Is false\")}else{print(\"never\")}"
        val result = "if(true) {\n\tprint(\"Is true \")\n} else if(false) {\n\tprint(\"Is false\")\n} else {\n\tprint(\"never\")\n}"
        val parseResult = KotlinParserFacadeScript.parse(code).root!!
        assertEquals(result, parseResult.generateCode())
    }

    @Test
    fun convertFunction(){
        val code = "fun test(x: Int, y: Int){\tprint(\"ciao\")}"
        val result = "func test(x: Int, y: Int){\n\tprint(\"ciao\")\n}"
        val parseResult = KotlinParserFacade.parse(code).root!!
        assertEquals(result, parseResult.generateCode())
    }

    @Test
    fun convertExpressionFunction(){
        val code = "fun test(x: Int, y: Int) = 3"
        val result = "func test(x: Int, y: Int)-> Int{\n\treturn 3\n}"
        val parseResult = KotlinParserFacade.parse(code).root!!
        assertEquals(result, parseResult.generateCode())
    }

    @Test
    fun convertExpressionFunctionReturn(){
        val code = "fun test(x: Int, y: Int): Int { return 3}"
        val result = "func test(x: Int, y: Int)-> Int{\n\treturn 3\n}"
        val parseResult = KotlinParserFacade.parse(code).root!!
        assertEquals(result, parseResult.generateCode())
    }

    @Test
    fun convertFunctionCall(){
        val code = """test("hello", "world", 42)"""
        val result = """test("hello", "world", 42)"""
        val parseResult = KotlinParserFacadeScript.parse(code).root!!
        assertEquals(result, parseResult.generateCode())
    }

    @Test
    fun convertIntRangeExpression(){
        var code = "val range = 1..42"
        val result = "let range:ClosedRange<Int> = 1...42"
        val parseResult = KotlinParserFacade.parse(code).root!!
        assertEquals(result, parseResult.generateCode())
    }

    @Test
    fun convertDoubleRangeExpression(){
        var code = "val range = 1.1..42.1"
        val result = "let range:ClosedRange<Double> = 1.1...42.1"
        val parseResult = KotlinParserFacade.parse(code).root!!
        assertEquals(result, parseResult.generateCode())
    }

    @Test
    fun convertIntListExpression(){
        var code = "val list = listOf<Int>(1, 2, 3)"
        val result = "let list:[Int] = [1, 2, 3]"
        val parseResult = KotlinParserFacade.parse(code).root!!
        assertEquals(result, parseResult.generateCode())
    }

    @Test
    fun convertStringListExpression(){
        var code = "val list = listOf<String>(\"a\", \"b\", \"c\")"
        val result = "let list:[String] = [\"a\", \"b\", \"c\"]"
        val parseResult = KotlinParserFacade.parse(code).root!!
        assertEquals(result, parseResult.generateCode())
    }

    @Test
    fun convertTextWidget(){
        val code = "Text(\"Hello world\", color = Color.Blue, fontWeight = FontWeight.Bold)"
        val result = "Text(\"Hello world\")\n.foregroundColor(Color.blue)\n.fontWeight(Font.Weight.bold)"
        val parseResult = KotlinParserFacadeScript.parse(code).root!!
        assertEquals(result, parseResult.generateCode())
    }


    @Test
    fun convertCustomTextWidgetDeclaration(){
        val code = """
            @Composable
            fun test(x: Int, y: Int) { Text("Hello") }
            """.trimIndent()
        val result = """
struct test: View{
	var x: Int
	var y: Int
	var body: some View {
		Text("Hello")
	}
}
        """.trimIndent()
        val parseResult = KotlinParserFacade.parse(code)
        assertEquals(result, parseResult.root!!.generateCode())
    }

    @Test
    fun convertColumn() {
        val code = """
            Column(verticalArrangement = Arrangement.spacedBy(10.dp), horizontalAlignment = Alignment.Start)
            """.trimIndent()
        val result = """
         VStack(
         	alignment: HorizontalAlignment.leading,
         	spacing: CGFloat(10)){
   
         }
        """.trimIndent()

        val parseResult = KotlinParserFacadeScript.parse(code)
        assertEquals(result, parseResult.root!!.generateCode())
    }

    @Test
     fun mapDivider(){
        val code = """
            Divider()
            """.trimIndent()
        val result = "Divider()"
        val parseResult = KotlinParserFacadeScript.parse(code)
        assertEquals(result, parseResult.root!!.generateCode())
    }

    @Test
    fun convertColumnScrollable(){
        val code = """
            Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.Start)
            """.trimIndent()
        val result = """
ScrollView(.vertical){
	VStack(
	alignment: HorizontalAlignment.leading,
	spacing: CGFloat(10)){

	}
}
    """.trimIndent()
        val parseResult = KotlinParserFacadeScript.parse(code)

        assertEquals(result, parseResult.root!!.generateCode())
    }

    @Test
    fun mapDividerWithParams(){
        val code = """
            Divider(thickness = 8.dp)
            """.trimIndent()
        val result = "Divider()\n\t.frame(height: CGFloat(8))"
        val parseResult = KotlinParserFacadeScript.parse(code)
        assertEquals(result, parseResult.root!!.generateCode())
    }


    @Test
    fun convertColumnScrollableWithText() {
        val code = """
            Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.Start){
                Text("Ciao")
            }
            """.trimIndent()
        val result = """
        	ScrollView(.vertical){
        		VStack(
        		alignment: HorizontalAlignment.start,
        		spacing: CGFloat(10)){
        			Text("Ciao")
        		}
        	}
    """.trimIndent()
    }

    @Test
    fun mapSpacerWithParams(){
        val code = """
            Spacer(modifier = Modifier.width(54.dp).height(54.dp))
            """.trimIndent()
        val result = "Spacer()\n\t.frame(width: CGFloat(54), height: CGFloat(54))"
        val parseResult = KotlinParserFacadeScript.parse(code)
        assertEquals(result, parseResult.root!!.generateCode())
    }

    @Test
    fun mapButton(){
        val code = """
            Button( onClick = {
                print("Ok")
            }){
                Text("Ciao") 
            }
            """.trimIndent()
        val result = """
            Button(action: {
            	print("Ok")
            }){
            	Text("Ciao")
            }
        """.trimIndent()
        val parseResult = KotlinParserFacadeScript.parse(code)
        assertEquals(result, parseResult.root!!.generateCode())
    }

    @Test
    fun convertSimpleClass(){
        val code = """
        class Person(
        firstName: String,
        lastName: String
        ): Address, Jks {
            init {
                print("Hello")
            }
        }""".trimMargin()
        val expect = """
class Person: Address, Jks {
	init(firstName: String, lastName: String) {
		print("Hello")
	}
}""".trimIndent()
        val parseResult = KotlinParserFacade.parse(code)
        assertEquals(expect, parseResult.root!!.generateCode())
    }

@Test
fun convertDataClass(){
    val code = """
        data class Person(
        firstName: String,
        lastName: String
        ): Address, Jks {
            init {
                print("Hello")
            }
        }""".trimMargin()
    val expect = """
struct Person: Address, Jks {
	init(firstName: String, lastName: String) {
		print("Hello")
	}
}""".trimIndent()
    val parseResult = KotlinParserFacade.parse(code)
    assertEquals(expect, parseResult.root!!.generateCode())
}

    @Test
    fun convertClassWithThis(){
        val code = """
        class Person(
        firstName: String,
        lastName: String
        ): Address, Jks {
        var firstName: String
            init {
                print("Hello")
                this.firstName = firstName
            }
        }""".trimMargin()
        val expect = """
class Person: Address, Jks {
	var firstName:String
	init(firstName: String, lastName: String) {
		print("Hello")
		self.firstName = firstName
	}
}""".trimIndent()
        val parseResult = KotlinParserFacade.parse(code)
        assertEquals(expect, parseResult.root!!.generateCode())
    }

    @Test
    fun convertClassDefaultConstructor(){
        val code = """
        class Person(
        var firstName: String,
        val lastName: String
        ): Address, Jks {
        
        }""".trimMargin()
        val expect = """
class Person: Address, Jks {
	var firstName:String
	let lastName:String
	init(firstName: String, lastName: String) {
		self.firstName = firstName
		self.lastName = lastName
	}
}""".trimIndent()
        val parseResult = KotlinParserFacade.parse(code)
        assertEquals(expect, parseResult.root!!.generateCode())
    }

}