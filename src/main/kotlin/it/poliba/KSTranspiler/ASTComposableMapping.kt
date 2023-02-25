package it.poliba.KSTranspiler

import it.poliba.KSTranspiler.KotlinParser.BlueColorContext
import it.poliba.KSTranspiler.KotlinParser.BoldFontWeightContext
import it.poliba.KSTranspiler.KotlinParser.ColorParameterContext
import it.poliba.KSTranspiler.KotlinParser.CustomColorContext
import it.poliba.KSTranspiler.KotlinParser.CustomWeightContext
import it.poliba.KSTranspiler.KotlinParser.FontWeightParameterContext
import java.awt.Paint

fun  KotlinParser.ComposableCallExpressionContext.toAst(): Expression {
    return this.composableCall().toAst()
}
fun KotlinParser.ComposableCallContext.toAst(): Expression = when(this){
    is KotlinParser.TextComposableContext -> this.toAst()
    is KotlinParser.ImageComposableContext -> this.toAst()
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}

fun KotlinParser.TextComposableContext.toAst(): Expression {
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

fun KotlinParser.ImageComposableContext.toAst(): Expression {
    val params = this.imageComposeParameter().map { it.toAst() }

    val painter = params.firstOrNull { it is PainterResource } as PainterResource? ?: throw IllegalArgumentException("PainterResource expected in Image composable")

    val resizable = params.firstOrNull { it is Resizable } as Resizable?
    val contentFill = params.firstOrNull { it is ContentFill } as ContentFill?
    val contentFit = params.firstOrNull { it is ContentFit } as ContentFit?

    return ImageComposableCall(painter.image!!, resizable, contentFill ?: contentFit)


}


fun KotlinParser.ImageComposeParameterContext.toAst(): Expression = when(this){
    is KotlinParser.PainterParameterContext -> this.painter().toAst()
    is KotlinParser.ResizableContext -> Resizable()
    is KotlinParser.ContentScaleFillWidthContext -> ContentFill()
    is KotlinParser.ContentScaleFitContext -> ContentFit()
    else -> throw java.lang.IllegalArgumentException("Pain not recognized")
}

fun KotlinParser.PainterContext.toAst(): Expression = when(this){
    is KotlinParser.PainterResourceContext -> this.painterResourceParam().toAst()
    else -> throw java.lang.IllegalArgumentException("Pain not recognized")
}

fun KotlinParser.PainterResourceParamContext.toAst() = when(this){
    is KotlinParser.PainterResourceParameterContext -> this.getResource.toAst()
    else -> throw java.lang.IllegalArgumentException("Pain not recognized")
}

fun KotlinParser.ResourceContext.toAst() = when(this){
    is KotlinParser.DrawableResourceContext -> PainterResource(this.imageName.toAst())
    else -> throw java.lang.IllegalArgumentException("Pain not recognized")
}

fun KotlinParser.FunctionDeclarationContext.toWidgetAst(considerPosition: Boolean = false): WidgetDeclaration {
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