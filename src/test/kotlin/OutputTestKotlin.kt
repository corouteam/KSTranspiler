package it.poliba.KSTranspiler

import it.poliba.KSTranspiler.facade.KotlinParserFacade
import it.poliba.KSTranspiler.facade.SwiftParserFacade
import it.poliba.KSTranspiler.facade.SwiftParserFacadeScript
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class  OutputTestKotlin {

    @Test
    fun convertVarPropertyDefinition() {
        var code = "let a: Int = 5"
        val parseResult = SwiftParserFacade.parse(code).root
        val expected = "val a:Int = 5"
        assertEquals(expected, parseResult?.generateKotlinCode())
    }

    @Test
    fun convertDoublePropertyDefinition() {
        var code = "let a:Double = 5.9"
        val parseResult = SwiftParserFacade.parse(code).root!!
        val expected = "val a:Double = 5.9"
        assertEquals(expected, parseResult.generateKotlinCode())
    }

    @Test
    fun convertSumPropertyDefinition() {
        var code = "let a = 5 + 7"
        val parseResult = SwiftParserFacade.parse(code).root!!
        val expected = "val a:Int = 5 + 7"
        assertEquals(expected, parseResult.generateKotlinCode())
    }

    @Test
    fun convertMinusPropertyDefinition() {
        var code = "let a = 5 - 7"
        val parseResult = SwiftParserFacade.parse(code).root!!
        val expected = "val a:Int = 5 - 7"
        assertEquals(expected, parseResult.generateKotlinCode())
    }

    @Test
    fun convertDivisionPropertyDefinition() {
        var code = "let a = 5.0 / 7.0"
        val parseResult = SwiftParserFacade.parse(code).root!!
        val expected = "val a:Double = 5.0 / 7.0"
        assertEquals(expected, parseResult.generateKotlinCode())
    }

    @Test
    fun convertAssignmentPropertyDefinition() {

        var code = "a = 5"
        val parseResult = SwiftParserFacadeScript.parse(code).root!!
        val expected = "a = 5"
        assertEquals(expected, parseResult.generateKotlinCode())
    }

    @Test
    fun convertPrintPropertyDefinition() {
        var code = "print(\"aa\")"
        val parseResult = SwiftParserFacadeScript.parse(code).root!!
        val expected = "print(\"aa\")"
        assertEquals(expected, parseResult.generateKotlinCode())
    }

    @Test
    fun convertIf() {
        val code = "if(true){ print(\"Is true \")}"
        val result = "if(true){\n\tprint(\"Is true \")\n}"
        val parseResult = SwiftParserFacadeScript.parse(code)
        assertEquals(result, parseResult.root!!.generateKotlinCode())

    }

    @Test
    fun convertIfElse() {
        val code = "if(true){ print(\"Is true \") }else{print(\"Is false\")}"
        val result = "if(true){\n\tprint(\"Is true \")\n}else{\n\tprint(\"Is false\")\n}"
        val parseResult = SwiftParserFacadeScript.parse(code).root!!
        assertEquals(result, parseResult.generateKotlinCode())

    }

    @Test
    fun convertIfElseIf() {
        val code = "if(true){ print(\"Is true \") }else if(false){print(\"Is false\")}"
        val result = "if(true){\n\tprint(\"Is true \")\n}else if(false){\n\tprint(\"Is false\")\n}"
        val parseResult = SwiftParserFacadeScript.parse(code).root!!
        assertEquals(result, parseResult.generateKotlinCode())
    }

    @Test
    fun convertIfElseIfElse() {
        val code = "if(true){ print(\"Is true \") }else if(false){print(\"Is false\")}else{print(\"never\")}"
        val result =
            "if(true){\n\tprint(\"Is true \")\n}else if(false){\n\tprint(\"Is false\")\n}else{\n\tprint(\"never\")\n}"
        val parseResult = SwiftParserFacadeScript.parse(code).root!!
        assertEquals(result, parseResult.generateKotlinCode())
    }

    @Test
    fun convertFunction() {
        val code = "func test(x: Int, y: Int){\n\tprint(\"ciao\")\n}"
        val result = """
            fun test(x: Int, y: Int){
            	print("ciao")
            }
        """.trimIndent()
        val parseResult = SwiftParserFacade.parse(code).root!!
        assertEquals(result, parseResult.generateKotlinCode())
    }

    @Test
    fun convertExpressionFunctionReturn() {
        val code = "func test(x: Int, y: Int)-> Int{\n\treturn 3\n}"
        val result = """
            fun test(x: Int, y: Int): Int{
            	return 3
            }
        """.trimIndent()
        val parseResult = SwiftParserFacade.parse(code).root!!
        assertEquals(result, parseResult.generateKotlinCode())
    }

    @Test
    fun convertFunctionCall() {
        val code = """test("hello", "world", 42)"""
        val result = """test("hello", "world", 42)"""
        val parseResult = SwiftParserFacadeScript.parse(code).root!!
        assertEquals(result, parseResult.generateKotlinCode())
    }

    @Test
    fun convertIntRangeExpression() {
        var code = "let range = 1...42"
        val result = "val range:ClosedRange<Int> = 1..42"
        val parseResult = SwiftParserFacade.parse(code).root!!
        assertEquals(result, parseResult.generateKotlinCode())
    }

    @Test
    fun convertDoubleRangeExpression() {
        var code = "let range = 1.1...42.1"
        val result = "val range:ClosedRange<Double> = 1.1..42.1"
        val parseResult = SwiftParserFacade.parse(code).root!!
        assertEquals(result, parseResult.generateKotlinCode())
    }

    //   @Test
    //   fun convertIntListExpression(){
    //       var code = "let list = [1, 2, 3]"
    //       val result = "val list = listOf<Int>(1, 2, 3)"
    //       val parseResult = SwiftParserFacade.parse(code).root!!
    //       assertEquals(result, parseResult.generateKotlinCode())
    //   }

    //   @Test
    //   fun convertStringListExpression(){
    //       var code = "val list = listOf<String>(\"a\", \"b\", \"c\")"
    //       val result = "let list:[String] = [\"a\", \"b\", \"c\"]"
    //       val parseResult = SwiftParserFacade.parse(code).root!!
    //       assertEquals(result, parseResult.generateKotlinCode())
    //   }

    @Test
    fun convertTextWidget() {
        val code = "Text(\"Hello world\")\n.foregroundColor(Color.blue)\n.fontWeight(Font.Weight.bold)"
        val result = "Text(\"Hello world\", color = Color.Blue, fontWeight = FontWeight.Bold)"
        val parseResult = SwiftParserFacadeScript.parse(code).root!!
        assertEquals(result, parseResult.generateKotlinCode())
    }


    @Test
    fun convertCustomTextWidgetDeclaration() {
        val code = """
            struct test: View{
                var x: Int
                var y: Int
                var body: some View {
                    Text("Hello")
                }
            }
        """.trimIndent()

        val result = """
            @Composable
            fun test(x: Int, y: Int){
            	Text("Hello")
            }
            """.trimIndent()

        val parseResult = SwiftParserFacade.parse(code)
        assertEquals(result, parseResult.root!!.generateKotlinCode())
    }

    @Test
    fun convertColumn() {
        val code = """
         VStack(
         	alignment: HorizontalAlignment.leading,
         	spacing: CGFloat(10)){}
        """.trimIndent()

        val result = """
Column(horizontalAlignment = Alignment.Start, verticalArrangement = 10.dp){
    
}
            """.trimIndent()

        val parseResult = SwiftParserFacadeScript.parse(code)
        assertEquals(result, parseResult.root!!.generateKotlinCode())
    }

    @Test
    fun mapDivider() {
        val code = """
            Divider()
            """.trimIndent()
        val result = "Divider()"
        val parseResult = SwiftParserFacadeScript.parse(code)
        assertEquals(result, parseResult.root!!.generateKotlinCode())
    }

//    @Test
//    fun convertColumnScrollable(){
//
//        val code = """
//        	ScrollView(.vertical){
//        		VStack(
//        		alignment: HorizontalAlignment.leading,
//        		spacing: CGFloat(10)){
//
//        		}
//        	}
//    """.trimIndent()
//
//        val result = """
//            Column(
//            modifier = Modifier.verticalScroll(rememberScrollState()),
//            verticalArrangement = Arrangement.spacedBy(10.dp),
//            horizontalAlignment = Alignment.Start)
//            """.trimIndent()
//        val parseResult = SwiftParserFacadeScript.parse(code)
//
//        assertEquals(result, parseResult.root!!.generateKotlinCode())
//    }

    @Test
    fun mapDividerWithParams() {
        val code = "Divider()\n\t.frame(height: CGFloat(8))"

        val result = """
                Divider(modifier: Modifier.height(8.dp))
            """.trimIndent()
        val parseResult = SwiftParserFacadeScript.parse(code)
        assertEquals(result, parseResult.root!!.generateKotlinCode())
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
    fun mapSpacerWithParams() {
        val code = "Spacer()\n\t.frame(width: CGFloat(54), height: CGFloat(54))"

        val result = """
            Spacer(modifier = Modifier.width(54.dp).height(54.dp))
            """.trimIndent()
        val parseResult = SwiftParserFacadeScript.parse(code)
        assertEquals(result, parseResult.root!!.generateKotlinCode())
    }

    @Test
    fun mapButton() {
        val code = """
            Button(action: {
            	print("Ok")
            }){
            	Text("Ciao")
            } 
        """.trimIndent()

        val result = """
Button(onClick = {
	print("Ok")
}){
	Text("Ciao")
} 
            """.trimIndent()
        val parseResult = SwiftParserFacadeScript.parse(code)
        assertEquals(result, parseResult.root!!.generateKotlinCode())
    }


    @Test
    fun convertSimpleClass() {
        val result = """
        class Person(
        firstName: String,
        lastName: String
        ): Address, Jks {
            init {
                print("Hello")
            }
        }""".trimMargin()
        val code = """
class Person: Address, Jks {
	init(firstName: String, lastName: String) {
		print("Hello")
	}
}""".trimIndent()
        val parseResult = SwiftParserFacade.parse(code)
        assertEquals(result, parseResult.root!!.generateKotlinCode())
    }

    @Test
    fun convertClassWithThis(){
        val expect = """
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
        val code = """
class Person: Address, Jks {
	var firstName:String
	init(firstName: String, lastName: String) {
		print("Hello")
		self.firstName = firstName
	}
}""".trimIndent()
        val parseResult = SwiftParserFacade.parse(code)
        assertEquals(expect, parseResult.root!!.generateKotlinCode())
    }
}