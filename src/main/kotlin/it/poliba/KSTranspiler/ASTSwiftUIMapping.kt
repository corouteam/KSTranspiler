package it.poliba.KSTranspiler

import it.poliba.KSTranspiler.SwiftParser.BoldSuffixContext
import it.poliba.KSTranspiler.SwiftParser.ForegroundColorSuffixContext
import it.poliba.KSTranspiler.SwiftParser.LeadingAlignmentContext
import it.poliba.KSTranspiler.SwiftParser.SpacingParameterContext
import java.lang.Exception

fun  SwiftParser.WidgetCallExpressionContext.toAst(): Expression {
    return this.widgetCall().toAst()
}
fun SwiftParser.WidgetCallContext.toAst(): Expression = when(this){
    is SwiftParser.TextWidgetContext -> this.toAst()
    is SwiftParser.VStackWidgetContext -> this.toAst()
    is SwiftParser.ScrollViewWidgetContext -> this.toAst()
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}

fun SwiftParser.TextWidgetContext.toAst(): Expression {
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


fun SwiftParser.StructDeclarationContext.toWidgetAST(): WidgetDeclaration{
    var id = this.ID().text
    val bodyInstruction = this.classBody().classMemberDeclarations().declaration().map { it.toAst() }
    val properties = bodyInstruction.filter { it is PropertyDeclaration  && it.varName != "body"}.map { it as PropertyDeclaration }
    val functionParameters = properties.map { FunctionParameter(it.varName, it.type) }
    val body = bodyInstruction.first() { it is PropertyDeclaration  && it.varName == "body"} as PropertyDeclaration
    return WidgetDeclaration(id, functionParameters,body.getter!!)
}


fun SwiftParser.VStackWidgetContext.toAst(): Expression{
    val alignment = swiftUIColumnParam().firstOrNull { it is SwiftParser.AlignmentParameterContext } as SwiftParser.AlignmentParameterContext
    val alignmentExpression = alignment.expression().toAst()

    val spacing = swiftUIColumnParam().firstOrNull { it is SwiftParser.SpacingParameterContext } as SpacingParameterContext
    var spacingExpression = spacing.expression().toAst()

    if (spacingExpression is IntLit){
        spacingExpression = DpLit(spacingExpression.value)
    }

    val block = if(this.block() != null) this.block().toAst() else Block(listOf())

    return ColumnComposableCall(spacing = spacingExpression, horizontalAlignment = alignmentExpression, false, block )
}

fun SwiftParser.HorizontalAlignmentExpressionContext.toAst(): Expression{
    when(this.horizontalAlignment()){
        is LeadingAlignmentContext -> return StartAlignment
        else -> throw java.lang.IllegalArgumentException("HorizontalAlignmentExpressionContext not recognized")
    }
}

fun SwiftParser.ScrollViewWidgetContext.toAst(): Expression{
    val block = if(this.block() != null) this.block().toAst() else Block(listOf())
    if(this.ID() != null){
        if(this.ID().text == "vertical"){
            return ColumnComposableCall(null, null, true, block )
        }else if(this.ID().text == "horizontal"){
            return RowComposableCall(null, null, true, block )

        }else{
            throw java.lang.IllegalArgumentException("Scroll parameter not recognized")
        }
    }else{
        return ColumnComposableCall(null, null, true, block)
    }
}
