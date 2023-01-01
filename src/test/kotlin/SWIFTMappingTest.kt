package it.poliba.KSTranspiler

import com.google.gson.Gson
import it.poliba.KSTranspiler.parsing.SwiftParserFacade
import it.poliba.KSTranspiler.parsing.SwiftParserFacadeScript
import it.poliba.KSTranspiler.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SWIFTMappingTest {
    private fun mapResource(
        code: String,
    ): KSFile {
        val result = SwiftParserFacade.parse(code)

        if (result.isCorrect()) {
            return result.root?.toAst() ?: throw Exception("ParserRuleContext was null")
        } else {
            throw result.errors.first()
        }
    }

    private fun mapResourceScript(
        code: String,
    ): KSScript {
        val result = SwiftParserFacadeScript.parse(code)

        if (result.isCorrect()) {
            return result.root?.toAst() ?: throw Exception("ParserRuleContext was null")
        } else {
            throw result.errors.first()
        }
    }


    @Test
    fun mapTextComposable(){
        val code = "Text(\"Hello world\")"
        val ast = mapResourceScript(code)
        val expectedAst = KSScript(listOf(
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
            val ast = mapResourceScript(code)
        }catch (e: Exception){
            res = e
        }
        assertEquals(res.toString(), expected.toString())
    }

    @Test
    fun mapTextComposableWithParams(){
        val code = "Text(\"Hello world\").foregroundColor(Color.blue).fontWeight(Font.Weight.bold)"
        val ast = mapResourceScript(code)
        val expectedAst = KSScript(listOf(
            TextComposableCall(StringLit("Hello world"), ColorBlue(), FontWeightBold())
        ))
        assertEquals(Gson().toJson(expectedAst), Gson().toJson(ast))
    }

    @Test
    fun mapTextComposableRef(){
        val code = "Text(greet)"
        val ast = mapResourceScript(code)
        val expectedAst = KSScript(listOf(
            TextComposableCall(VarReference("greet", type = StringType()), null, null)
        ))
        assertEquals(Gson().toJson(expectedAst), Gson().toJson(ast))
        //val expectedAst = KotlinScript
    }


}