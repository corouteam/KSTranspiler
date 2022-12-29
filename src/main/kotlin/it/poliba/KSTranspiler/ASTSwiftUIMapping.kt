package it.poliba.KSTranspiler

import it.poliba.KSTranspiler.KotlinParser.BlueColorContext
import it.poliba.KSTranspiler.KotlinParser.BoldFontWeightContext
import it.poliba.KSTranspiler.KotlinParser.ColorParameterContext
import it.poliba.KSTranspiler.KotlinParser.CustomColorContext
import it.poliba.KSTranspiler.KotlinParser.CustomWeightContext
import it.poliba.KSTranspiler.KotlinParser.FontWeightParameterContext
import it.poliba.KSTranspiler.SwiftParser.BoldSuffixContext
import it.poliba.KSTranspiler.SwiftParser.ForegroundColorSuffixContext
import it.poliba.KSranspiler.*
import java.util.ListResourceBundle

fun  SwiftParser.ComposableCallExpressionContext.toAst(): Expression{
    return this.composableCall().toAst()
}
fun SwiftParser.ComposableCallContext.toAst(): Expression = when(this){
    is SwiftParser.TextComposableContext -> this.toAst()
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}

fun SwiftParser.TextComposableContext.toAst(): Expression{
    val expressionAst = this.expression().toAst()
    if(expressionAst.type != StringType()) throw IllegalArgumentException("String expected in Text composable")
    val params = swiftUITextSuffix().map { it.toAst() }
    val color = params.firstOrNull { it is ColorLit } as ColorLit?
    val fontWeight = params.firstOrNull { it is FontWeightLit } as FontWeightLit?
    return TextComposableCall(expressionAst, color, fontWeight)
}

fun SwiftParser.SwiftUITextSuffixContext.toAst(): Expression = when(this){
    is ForegroundColorSuffixContext -> color().toAst()
    is BoldSuffixContext -> FontWeightBold()
    else -> throw IllegalArgumentException("Parametro non riconosciuto")
}

fun SwiftParser.ColorContext.toAst(): Expression = when(this){
    is SwiftParser.BlueColorContext ->  ColorBlue()
    else -> throw java.lang.IllegalArgumentException("Color not recognized")
}