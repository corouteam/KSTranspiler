package it.poliba.KSTranspiler

import it.poliba.KSTranspiler.KotlinParser.CenterVerticalltAlignmentContext
import it.poliba.KSTranspiler.SwiftParser.BoldSuffixContext
import it.poliba.KSTranspiler.SwiftParser.BottomAlignmentContext
import it.poliba.KSTranspiler.SwiftParser.CenterVerticalAlignmentContext
import it.poliba.KSTranspiler.SwiftParser.ForegroundColorSuffixContext
import it.poliba.KSTranspiler.SwiftParser.FrameSuffixContext
import it.poliba.KSTranspiler.SwiftParser.LeadingAlignmentContext
import it.poliba.KSTranspiler.SwiftParser.SpacingParameterContext
import it.poliba.KSTranspiler.SwiftParser.TopAlignmentContext
import it.poliba.KSTranspiler.SwiftParser.TrailingAlignmentContext
import org.antlr.v4.runtime.atn.ContextSensitivityInfo
import java.lang.Exception

fun  SwiftParser.WidgetCallExpressionContext.toAst(considerPosition: Boolean = false): Expression {
    return this.widgetCall().toAst(considerPosition)
}
fun SwiftParser.WidgetCallContext.toAst(considerPosition: Boolean): Expression = when(this){
    is SwiftParser.TextWidgetContext -> this.toAst(considerPosition)
    is SwiftParser.DividerWidgetContext -> this.toAst(considerPosition)
    is SwiftParser.SpacerWidgetContext -> this.toAst(considerPosition)
    is SwiftParser.VStackWidgetContext -> this.toAst(considerPosition)
    is SwiftParser.HStackWidgetContext -> this.toAst(considerPosition)
    is SwiftParser.ScrollViewWidgetContext -> this.toAst(considerPosition)
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}

fun SwiftParser.TextWidgetContext.toAst(considerPosition: Boolean = false): Expression {
    val expressionAst = this.expression().toAst(considerPosition)
    if(expressionAst.type != StringType(toPosition(considerPosition))) throw IllegalArgumentException("String expected in Text composable")
    val params = swiftUITextSuffix().map { it.toAst(considerPosition) }
    val color = params.firstOrNull { it is ColorLit } as ColorLit?
    val fontWeight = params.firstOrNull { it is FontWeightLit } as FontWeightLit?
    return TextComposableCall(expressionAst, color, fontWeight, toPosition(considerPosition))
}

fun SwiftParser.DividerWidgetContext.toAst(considerPosition: Boolean = false): Expression {
    val params = swiftUIGenericWidgetSuffix().map { it.toAst(considerPosition) }
    val frame = params.firstOrNull { it is Frame } as Frame?

    return DividerComposableCall(frame, toPosition(considerPosition))
}

fun SwiftParser.SpacerWidgetContext.toAst(considerPosition: Boolean = false): Expression {
    val params = swiftUIGenericWidgetSuffix().map { it.toAst(considerPosition) }
    val frame = params.firstOrNull { it is Frame } as Frame?

    return SpacerComposableCall(frame, toPosition(considerPosition))
}

fun SwiftParser.SwiftUITextSuffixContext.toAst(considerPosition: Boolean = false): Expression = when(this){
    is ForegroundColorSuffixContext -> color().toAst(considerPosition)
    is BoldSuffixContext -> FontWeightBold(toPosition(considerPosition))
    else -> throw IllegalArgumentException("Parametro non riconosciuto")
}

fun SwiftParser.SwiftUIGenericWidgetSuffixContext.toAst(considerPosition: Boolean = false): Any = when(this){
    is FrameSuffixContext -> toAst(considerPosition)
    else -> throw IllegalArgumentException("Parametro non riconosciuto")
}

fun SwiftParser.ColorContext.toAst(considerPosition: Boolean = false): Expression = when(this){
    is SwiftParser.BlueColorContext ->  ColorBlue(toPosition(considerPosition))
    else -> throw java.lang.IllegalArgumentException("Color not recognized")
}

fun SwiftParser.FrameSuffixContext.toAst(considerPosition: Boolean = false): Frame {
    return Frame(
        width = this.width.toAst(considerPosition),
        height = this.heigth.toAst(considerPosition),
        position = toPosition(considerPosition))
}


fun SwiftParser.StructDeclarationContext.toWidgetAST(considerPosition: Boolean = false): WidgetDeclaration{
    var id = this.ID().text
    val bodyInstruction = this.classBody().classMemberDeclarations().declaration().map { it.toAst(considerPosition) }
    val properties = bodyInstruction.filter { it is PropertyDeclaration  && it.varName != "body"}.map { it as PropertyDeclaration }
    val functionParameters = properties.map { FunctionParameter(it.varName, it.type, toPosition(considerPosition)) }
    val body = bodyInstruction.first() { it is PropertyDeclaration  && it.varName == "body"} as PropertyDeclaration
    return WidgetDeclaration(id, functionParameters,body.getter!!, toPosition(considerPosition))
}


fun SwiftParser.VStackWidgetContext.toAst(considerPosition: Boolean = false): Expression{
    val alignment = swiftUIColumnParam().firstOrNull { it is SwiftParser.AlignmentParameterContext } as? SwiftParser.AlignmentParameterContext
    val alignmentExpression = alignment?.expression()?.toAst(considerPosition)

    val spacing = swiftUIColumnParam().firstOrNull { it is SwiftParser.SpacingParameterContext } as? SpacingParameterContext
    var spacingExpression = spacing?.expression()?.toAst(considerPosition)

    if (spacingExpression is IntLit){
        spacingExpression = DpLit(spacingExpression.value, toPosition(considerPosition))
    }

    val block = if(this.block() != null) this.block().toAst(considerPosition) else Block(listOf(), toPosition(considerPosition))

    return ColumnComposableCall(spacing = spacingExpression, horizontalAlignment = alignmentExpression, false, block, toPosition(considerPosition))
}

fun SwiftParser.HStackWidgetContext.toAst(considerPosition: Boolean = false): Expression{
    val alignment = swiftUIColumnParam().firstOrNull { it is SwiftParser.AlignmentParameterContext } as? SwiftParser.AlignmentParameterContext
    val alignmentExpression = alignment?.expression()?.toAst(considerPosition)

    val spacing = swiftUIColumnParam().firstOrNull { it is SwiftParser.SpacingParameterContext } as? SpacingParameterContext
    var spacingExpression = spacing?.expression()?.toAst(considerPosition)

    if (spacingExpression is IntLit){
        spacingExpression = DpLit(spacingExpression.value, toPosition(considerPosition))
    }

    val block = if(this.block() != null) this.block().toAst(considerPosition) else Block(listOf(), toPosition(considerPosition))

    return RowComposableCall(spacing = spacingExpression, verticalAlignment = alignmentExpression, false, block, toPosition(considerPosition))
}

fun SwiftParser.HorizontalAlignmentExpressionContext.toAst(considerPosition: Boolean = false): Expression{
    when(this.horizontalAlignment()){
        is LeadingAlignmentContext -> return StartAlignment(toPosition(considerPosition))
        is TrailingAlignmentContext -> return EndAlignment(toPosition(considerPosition))
        is SwiftParser.CenterHorizontalAlignmentContext -> return CenterHorizAlignment(toPosition(considerPosition))
        else -> throw java.lang.IllegalArgumentException("HorizontalAlignmentExpressionContext not recognized")
    }
}

fun SwiftParser.VerticalAlignmentExpressionContext.toAst(considerPosition: Boolean = false): Expression{
    when(this.verticalAlignment()){
        is TopAlignmentContext -> return TopAlignment(toPosition(considerPosition))
        is BottomAlignmentContext -> return BottomAlignment(toPosition(considerPosition))
        is CenterVerticalAlignmentContext-> return CenterVerticallyAlignment(toPosition(considerPosition))
        else -> throw java.lang.IllegalArgumentException("VerticalAlignmentExpressionContext not recognized")
    }
}


fun SwiftParser.ScrollViewWidgetContext.toAst(considerPosition: Boolean = false): Expression{
    val block = if(this.block() != null) this.block().toAst(considerPosition) else Block(listOf(), toPosition(considerPosition))
    if(this.ID() != null){
        if(this.ID().text == "vertical"){
            return ColumnComposableCall(null, null, true, block, toPosition(considerPosition))
        }else if(this.ID().text == "horizontal"){
            return RowComposableCall(null, null, true, block, toPosition(considerPosition))

        }else{
            throw java.lang.IllegalArgumentException("Scroll parameter not recognized")
        }
    }else{
        return ColumnComposableCall(null, null, true, block, toPosition(considerPosition))
    }
}
