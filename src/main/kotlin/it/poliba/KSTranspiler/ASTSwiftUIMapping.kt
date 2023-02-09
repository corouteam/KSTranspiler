package it.poliba.KSTranspiler

import it.poliba.KSTranspiler.SwiftParser.BoldSuffixContext
import it.poliba.KSTranspiler.SwiftParser.ForegroundColorSuffixContext

fun  SwiftParser.WidgetCallExpressionContext.toAst(): Expression {
    return this.widgetCall().toAst()
}
fun SwiftParser.WidgetCallContext.toAst(): Expression = when(this){
    is SwiftParser.TextWidgetContext -> this.toAst()
    is SwiftParser.ImageWidgetContext -> this.toAst()
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

fun SwiftParser.ImageWidgetContext.toAst(): Expression {
    val expressionAst = this.expression().toAst()
    if(expressionAst.type != StringType()) throw IllegalArgumentException("String expected in Image composable")
    val params = swiftUIImageSuffix().map { it.toAst() }
    val resizable = params.firstOrNull { it is ResizableLit } as ResizableLit?
    val aspectRatio = params.firstOrNull { it is AspectRatioLit } as AspectRatioLit?
    val frame = params.firstOrNull { it is FrameLit } as FrameLit?
    return ImageComposableCall(expressionAst, resizable, aspectRatio, frame)
}

fun SwiftParser.SwiftUITextSuffixContext.toAst(): Expression = when(this){
    is ForegroundColorSuffixContext -> color().toAst()
    is BoldSuffixContext -> FontWeightBold()
    else -> throw IllegalArgumentException("Parametro non riconosciuto")
}

fun SwiftParser.SwiftUIImageSuffixContext.toAst(): Expression = when(this){
    is SwiftParser.ResizableSuffixContext -> Resizable()
    is SwiftParser.AspectRatioSuffixContext -> contentMode().toAst()
    is SwiftParser.FrameSuffixContext -> Frame(width = 54.0, height = 54.0) //TODO recuperare i 2 parametri
    else -> throw IllegalArgumentException("Parametro non riconosciuto")
}

fun SwiftParser.ContentModeContext.toAst(): Expression = when(this){
    is SwiftParser.ContentModeFitContext -> ContentFit()
    is SwiftParser.ContentModeFillContext -> ContentFill()
    else -> throw java.lang.IllegalArgumentException("ContentMode not recognized")
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