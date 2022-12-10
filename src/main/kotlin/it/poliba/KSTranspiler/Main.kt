package it.poliba.KSTranspiler

import it.poliba.KSTranspiler.parsing.KotlinParserFacade
import org.stringtemplate.v4.STGroup
import org.stringtemplate.v4.STGroupFile


object App {
    @JvmStatic
    fun main(args: Array<String>) {
        var code = """if(true){val a = 5}""".trim()
        var code2 = "let a = 5 + 2"
        var actual = code
        val parseResult = KotlinParserFacade.parse(actual).root!!
      //  var ast = parseResult.toAst()
        //println(ast.generateCode())

    }

}
