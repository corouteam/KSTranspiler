package it.poliba.KSTranspiler

import it.poliba.KSTranspiler.KotlinParser.BlueColorContext
import it.poliba.KSTranspiler.KotlinParser.BoldFontWeightContext
import it.poliba.KSTranspiler.KotlinParser.ColorParameterContext
import it.poliba.KSTranspiler.KotlinParser.CustomColorContext
import it.poliba.KSTranspiler.KotlinParser.CustomWeightContext
import it.poliba.KSTranspiler.KotlinParser.FontWeightParameterContext
import it.poliba.KSranspiler.*

fun  KotlinParser.ComposableCallExpressionContext.toAst(): Expression{
    return this.composableCall().toAst()
}
fun KotlinParser.ComposableCallContext.toAst(): Expression = when(this){
    is KotlinParser.TextComposableContext -> this.toAst()
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}

fun KotlinParser.TextComposableContext.toAst(): Expression{
    val expressionAst = this.expression().toAst()
    if(expressionAst.type != StringType()) throw IllegalArgumentException("String expected in Text composable")
    val params = textComposeParameter().map { it.toAst() }
    val color = params.firstOrNull { it is ColorLit } as ColorLit?
    val fontWeight = params.firstOrNull { it is FontWeightLit } as FontWeightLit?
    return TextComposableCall(expressionAst, color, fontWeight)
}

fun KotlinParser.TextComposeParameterContext.toAst(): Expression = when(this){
    is ColorParameterContext -> this.color().toAst()
    is FontWeightParameterContext -> this.fontWeight().toAst()
    else -> throw java.lang.IllegalArgumentException("Color not recognized")
}

fun KotlinParser.ColorContext.toAst(): Expression = when(this){
        is CustomColorContext -> CustomColor(StringLit(COLOR_LITERAL().text))
        is BlueColorContext ->  ColorBlue()
        else -> throw java.lang.IllegalArgumentException("Color not recognized")
}

fun KotlinParser.FontWeightContext.toAst(): Expression = when(this){
    is CustomWeightContext -> CustomFontWeight(IntLit(INT_LIT().text))
    is BoldFontWeightContext ->  FontWeightBold()
    else -> throw java.lang.IllegalArgumentException("Color not recognized")
}

fun KotlinParser.FunctionDeclarationContext.toWidgetAst(considerPosition: Boolean = false): WidgetDeclaration{
    val id = this.ID().text
    val params = this.functionValueParameters().functionValueParameter().map { it.toAst() }
    var block = Block(listOf())
    if(this.functionBody().block() != null){
        block =  Block(this.functionBody().block().statement().map { it.toAst(considerPosition) })
    }else{
        val returnExpression = ReturnExpression(this.functionBody().expression().toAst())
        block = Block(listOf(returnExpression))

    }
    return WidgetDeclaration(id, params, block)
}