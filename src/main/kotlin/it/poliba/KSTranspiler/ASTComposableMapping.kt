package it.poliba.KSTranspiler

import it.poliba.KSTranspiler.KotlinParser.BlueColorContext
import it.poliba.KSTranspiler.KotlinParser.BoldFontWeightContext
import it.poliba.KSTranspiler.KotlinParser.BottomAlignmentContext
import it.poliba.KSTranspiler.KotlinParser.CenterHorizontallyAlignmentContext
import it.poliba.KSTranspiler.KotlinParser.CenterVerticalltAlignmentContext
import it.poliba.KSTranspiler.KotlinParser.ColorParameterContext
import it.poliba.KSTranspiler.KotlinParser.ColumnComposeParameterContext
import it.poliba.KSTranspiler.KotlinParser.CustomColorContext
import it.poliba.KSTranspiler.KotlinParser.CustomWeightContext
import it.poliba.KSTranspiler.KotlinParser.EndAlignmentContext
import it.poliba.KSTranspiler.KotlinParser.FontWeightParameterContext
import it.poliba.KSTranspiler.KotlinParser.SizeSuffixContext
import it.poliba.KSTranspiler.SwiftParser.FrameSuffixContext

import it.poliba.KSTranspiler.KotlinParser.HorizontalAlignmentParameterContext
import it.poliba.KSTranspiler.KotlinParser.HorizontalScrollSuffixContext
import it.poliba.KSTranspiler.KotlinParser.ModifierColumnParameterContext
import it.poliba.KSTranspiler.KotlinParser.ModifierRawParameterContext
import it.poliba.KSTranspiler.KotlinParser.StartAlignmentContext
import it.poliba.KSTranspiler.KotlinParser.TopAlignmentContext
import it.poliba.KSTranspiler.KotlinParser.VerticalAlignmentParameterContext
import it.poliba.KSTranspiler.KotlinParser.VerticalArrangementParameterContext
import it.poliba.KSTranspiler.KotlinParser.VerticalScrollSuffixContext
import org.antlr.v4.runtime.atn.ContextSensitivityInfo

fun  KotlinParser.ComposableCallExpressionContext.toAst(considerPosition: Boolean = false): Expression {
    return this.composableCall().toAst(considerPosition)
}
fun KotlinParser.ComposableCallContext.toAst(considerPosition: Boolean = false): Expression = when(this){
    is KotlinParser.TextComposableContext -> this.toAst(considerPosition)
    is KotlinParser.DividerComposableContext -> this.toAst(considerPosition)
    is KotlinParser.SpacerComposableContext -> this.toAst(considerPosition)
    is KotlinParser.ColumnComposableContext -> this.toAst(considerPosition)
    is KotlinParser.RowComposableContext -> this.toAst(considerPosition)
    is KotlinParser.SpacerComposableContext -> SpacerComposableCall()
    is KotlinParser.DividerComposableContext -> DividerComposableCall()
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}

fun KotlinParser.DividerComposableContext.toAst(considerPosition: Boolean = false): Expression {
    val params = composableUIGenericWidgetSuffix().map { it.toAst(considerPosition) }
    val frame = params.firstOrNull { it is Frame } as Frame?

    return DividerComposableCall(frame, toPosition(considerPosition))
}

fun KotlinParser.SpacerComposableContext.toAst(considerPosition: Boolean = false): Expression {
    val params = composableUIGenericWidgetSuffix().map { it.toAst(considerPosition) }
    val frame = params.firstOrNull { it is Frame } as Frame?
    return SpacerComposableCall(frame, toPosition(considerPosition))
}

fun KotlinParser.ComposableUIGenericWidgetSuffixContext.toAst(considerPosition: Boolean = false): Any = when(this){
    is SizeSuffixContext -> toAst(considerPosition)
    else -> throw IllegalArgumentException("Parametro non riconosciuto")
}

fun KotlinParser.SizeSuffixContext.toAst(considerPosition: Boolean): Frame {
    return Frame(
        width = this.width.toAst(considerPosition),
        height = this.heigth.toAst(considerPosition),
        position = toPosition(considerPosition))
}




fun KotlinParser.ColumnComposableContext.toAst(considerPosition: Boolean = false): Expression{
    val verticalArrangement = columnComposeParameter().firstOrNull { it is VerticalArrangementParameterContext } as? VerticalArrangementParameterContext
    val verticalArrangementExpression = verticalArrangement?.expression()?.toAst(considerPosition)

    val alignment = columnComposeParameter().firstOrNull { it is HorizontalAlignmentParameterContext } as? HorizontalAlignmentParameterContext
    val alignmentExpression = alignment?.expression()?.toAst(considerPosition)

    val modifier = columnComposeParameter().firstOrNull() {it is ModifierColumnParameterContext} as? ModifierColumnParameterContext
    val scroll = modifier?.modifierParameter()?.modifier()?.modifierSuffix()?.firstOrNull { it is VerticalScrollSuffixContext } != null

    val block = if(this.block() != null) this.block().toAst(considerPosition) else Block(listOf(), toPosition(considerPosition))

    return ColumnComposableCall(verticalArrangementExpression, alignmentExpression, scroll, block, toPosition(considerPosition))
}

fun KotlinParser.RowComposableContext.toAst(considerPosition: Boolean = false): Expression{
    val verticalAlignment = rowComposeParameter().firstOrNull { it is VerticalAlignmentParameterContext } as? VerticalAlignmentParameterContext
    val verticalAlignmentExpression = verticalAlignment?.expression()?.toAst(considerPosition)

    val arrangement = rowComposeParameter().firstOrNull { it is KotlinParser.HorizontalArrangementParameterContext } as? KotlinParser.HorizontalArrangementParameterContext
    val arrangementExpression = arrangement?.expression()?.toAst(considerPosition)

    val modifier = rowComposeParameter().firstOrNull() {it is ModifierRawParameterContext} as? ModifierRawParameterContext
    val scroll = modifier?.modifierParameter()?.modifier()?.modifierSuffix()?.firstOrNull { it is HorizontalScrollSuffixContext } != null

    val block = if(this.block() != null) this.block().toAst(considerPosition) else Block(listOf(), toPosition(considerPosition))

    return RowComposableCall(arrangementExpression, verticalAlignmentExpression, scroll, block, toPosition(considerPosition))
}

fun KotlinParser.TextComposableContext.toAst(considerPosition: Boolean = false): Expression {
    val expressionAst = this.expression().toAst(considerPosition)
    if(expressionAst.type.nodeType != StringType(toPosition(considerPosition)).nodeType) throw IllegalArgumentException("String expected in Text composable")
    val params = textComposeParameter().map { it.toAst(considerPosition) }
    val color = params.firstOrNull { it is ColorLit } as ColorLit?
    val fontWeight = params.firstOrNull { it is FontWeightLit } as FontWeightLit?
    return TextComposableCall(expressionAst, color, fontWeight, toPosition(considerPosition))
}

fun KotlinParser.TextComposeParameterContext.toAst(considerPosition: Boolean = false): Expression = when(this){
    is ColorParameterContext -> this.color().toAst(considerPosition)
    is FontWeightParameterContext -> this.fontWeight().toAst(considerPosition)
    else -> throw java.lang.IllegalArgumentException("Color not recognized")
}

fun KotlinParser.ColorContext.toAst(considerPosition: Boolean = false): Expression = when(this){
        is CustomColorContext -> CustomColor(StringLit(COLOR_LITERAL().text, toPosition(considerPosition)), toPosition(considerPosition))
        is BlueColorContext ->  ColorBlue(toPosition(considerPosition))
        else -> throw java.lang.IllegalArgumentException("Color not recognized")
}

fun KotlinParser.FontWeightContext.toAst(considerPosition: Boolean = false): Expression = when(this){
    is CustomWeightContext -> CustomFontWeight(IntLit(INT_LIT().text, toPosition(considerPosition)), toPosition(considerPosition))
    is BoldFontWeightContext ->  FontWeightBold(toPosition(considerPosition))
    else -> throw java.lang.IllegalArgumentException("Color not recognized")
}

fun KotlinParser.FunctionDeclarationContext.toWidgetAst(considerPosition: Boolean = false): WidgetDeclaration {
    val id = this.ID().text
    val params = this.functionValueParameters().functionValueParameter().map { it.toAst(considerPosition) }
    var block: Block
    if(this.functionBody().block() != null){
        block =  Block(this.functionBody().block().statement().map { it.toAst(considerPosition) }, toPosition(considerPosition))
    }else{
        val returnExpression = ReturnExpression(this.functionBody().expression().toAst(considerPosition), toPosition(considerPosition))
        block = Block(listOf(returnExpression), toPosition(considerPosition))

    }
    return WidgetDeclaration(id, params, block, toPosition(considerPosition))
}


fun KotlinParser.ArrangementExpressionContext.toAst(considerPosition: Boolean = false): Expression{
    return this.arrangement().expression().toAst(considerPosition)
}

fun KotlinParser.HorizhontalAlignmentExpressionContext.toAst(considerPosition: Boolean = false): Expression{
    when (this.horizontalAlignment()){
        is StartAlignmentContext -> return StartAlignment(toPosition(considerPosition))
        is EndAlignmentContext -> return EndAlignment(toPosition(considerPosition))
        is CenterHorizontallyAlignmentContext -> return CenterHorizAlignment(toPosition(considerPosition))
        else -> throw  throw java.lang.IllegalArgumentException("Alignment not recognized")
    }
}


fun KotlinParser.VerticalAlignmentExpressionContext.toAst(considerPosition: Boolean = false): Expression{
    when (this.verticalAlignment()){
        is TopAlignmentContext -> return TopAlignment(toPosition(considerPosition))
        is BottomAlignmentContext -> return BottomAlignment(toPosition(considerPosition))
        is CenterVerticalltAlignmentContext -> return CenterVerticallyAlignment(toPosition(considerPosition))
        else -> throw  throw java.lang.IllegalArgumentException("Alignment not recognized")
    }
}