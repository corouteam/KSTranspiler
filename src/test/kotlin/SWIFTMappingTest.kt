package it.poliba.KSTranspiler

import com.google.gson.Gson
import it.poliba.KSTranspiler.facade.SwiftAntlrParserFacade
import it.poliba.KSTranspiler.facade.SwiftAntlrParserFacadeScript
import it.poliba.KSTranspiler.facade.SwiftParserFacade
import it.poliba.KSTranspiler.facade.SwiftParserFacadeScript
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


}