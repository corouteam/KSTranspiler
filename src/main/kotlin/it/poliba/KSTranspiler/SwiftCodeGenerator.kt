package it.poliba.KSTranspiler

import org.stringtemplate.v4.STGroup
import org.stringtemplate.v4.STGroupFile
import java.lang.Exception
import java.util.ArrayList
import java.util.StringJoiner

//val group: STGroup = STGroupFile("src/main/antlr/SwiftTemplate.stg")
fun AstFile.generateCode(depth: Int = 0): String{
    return declarations.joinToString("\n") { it.generateCode(depth) }
}
fun AstScript.generateCode(depth: Int = 0): String{
    return statement.joinToString("\n") { it.generateCode(depth) }
}

fun Declaration.generateCode(depth: Int = 0): String{
    return when(this){
        is PropertyDeclaration -> this.generateCode(depth)
        is WidgetDeclaration -> this.generateCode(depth)
        is FunctionDeclaration -> this.generateCode(depth)
    }
}
fun Statement.generateCode(depth: Int = 0): String {
    return when (this) {
        is PropertyDeclaration -> this.generateCode(depth)
        is Assignment -> this.generateCode(depth)
        is Print -> this.generateCode(depth)
        is IfExpression -> this.generateCode(depth)
        is Expression -> this.generateCode(depth)
        is FunctionDeclaration -> this.generateCode(depth)
        else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
    }
}

fun FunctionDeclaration.generateCode(depth: Int = 0): String{
    val returnType = if(this.returnType != null) "-> ${this.returnType.generateCode()}" else ""
    return "func ${this.id}(${this.parameters.joinToString(", "){it.generateCode()}})"+returnType +
            "${this.body.generateCode(depth)}"
}


fun FunctionParameter.generateCode(): String{
    return "${this.id}: ${this.type.generateCode()}"
}

fun Assignment.generateCode(depth: Int = 0): String{
    return "$varName = ${value.generateCode(depth)}"
}

fun Print.generateCode(depth: Int = 0): String{
    return "print(${value.generateCode(depth)})"
}

fun IfExpression.generateCode(depth: Int = 0): String{
    var result =  "if(${condition.generateCode(depth)})${body.generateCode(depth)}"
    elseBranch?.let {
        when (it){
            is IfExpression ->result += " else "+ it.generateCode(depth)
            else -> result += " else${it.generateCode(depth)}"

        }
    }
    return result
}

fun ControlStructureBody.generateCode(depth: Int = 0): String{
    return when(this){
        is Block -> "${this.generateCode(depth)}"
        is Statement -> "${getPrefix(depth)} ${this.generateCode(depth+1)}"
        else -> throw Exception("Not implemented")
    }
}

fun Block.generateCode(depth: Int = 0): String{
    return "{\n${this.body.joinToString("\n") { "${getPrefix(depth+1)}${it.generateCode(depth)}" }}\n${getPrefix(depth)}}"
}
fun PropertyDeclaration.generateCode(depth: Int = 0): String{
    var prefix = if (mutable) "var" else "let"
    var type = type.generateCode()
    var value = value?.let {  " = ${value.generateCode(depth)}"} ?: ""
    return "$prefix $varName:$type$value"
}

fun Expression.generateCode(depth: Int = 0) : String = when (this) {
    is IntLit -> this.value
    is DoubleLit -> this.value
    is DpLit -> "CGFloat(${this.value})"
    is VarReference -> this.varName
    is BinaryExpression -> this.generateCode(depth)
    is StringLit -> "\"${this.value}\""
    is BoolLit -> "${this.value}"
    is FunctionCall -> "${this.name}(${this.parameters.map { it.generateCode(depth) }.joinToString(", " )})"
    is RangeExpression -> "${this.leftExpression.generateCode(depth)}...${this.rightExpression.generateCode(depth)}"
    is ListExpression -> "[${this.items.map { it.generateCode(depth) }.joinToString(", ")}]"
    is ColorLit -> this.generateCode(depth)
    is FontWeightLit -> this.generateCode(depth)
    is ReturnExpression -> "return ${this.returnExpression.generateCode(depth)}"
    is TextComposableCall -> this.generateCode(depth)
    is DividerComposableCall -> this.generateCode(depth)
    is SpacerComposableCall -> this.generateCode(depth)
    is ColumnComposableCall -> this.generateCode(depth)
    is HorizontalAlignment -> this.generateCode(depth)
    is ButtonComposableCall -> this.generateCode(depth)
    is AspectRatioLit -> this.generateCode(depth)
    //is KotlinParser.ParenExpressionContext -> expression().toAst(considerPosition)
    //is KotlinParser.TypeConversionContext -> TypeConversion(expression().toAst(considerPosition), targetType.toAst(considerPosition), toPosition(considerPosition))
    is ImageComposableCall -> this.generateCode()

    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}

fun AspectRatioLit.generateCode(depth: Int = 0): String{
    var res = when(this){
        is ContentFit -> "ContentMode.fit"
        is ContentFill -> "ContentMode.fill"
    }
    return "${getPrefix(depth)}$res"
}

fun DividerComposableCall.generateCode(depth: Int = 0): String{
    var params: ArrayList<String> = arrayListOf()
    color?.let { params.add(".overlay(${it.generateCode(depth)})") }
    frame?.let { params.add(it.generateCode(depth)) }
    print("SIZE: ${params.size}")
    var paramString = params.joinToString("\n\t ")
    if(paramString.isNotBlank()){
        paramString = "\n\t$paramString"
    }
    return "Divider()$paramString"
}
fun ButtonComposableCall.generateCode(depth: Int = 0): String{
    return "Button(action: ${this.action.generateCode(depth)})${this.body.generateCode(depth)}"
}
fun SpacerComposableCall.generateCode(depth: Int = 0): String{
    val suffix = size?.let { "\n\t${it.generateCode(depth)}" } ?: ""

    return "Spacer()$suffix"
}

fun Frame.generateCode(depth: Int = 0): String{
    var width = width?.let { "width: ${width.generateCode(depth)}" }?: ""
    var height = height?.let { "height: ${height.generateCode(depth)}" }?: ""
    var params = ""
    if (width != "" && height != ""){
        params = "$width, $height"
    }else if (width != ""){
        params = "$width"
    }else{
        params = "$height"
    }

    return ".frame($params)"
}


fun Type.generateCode() : String = when (this) {
    is IntType -> "Int"
    is DoubleType -> "Double"
    is StringType -> "String"
    is BoolType -> "Boolean"
    is RangeType -> "ClosedRange<${this.type.generateCode()}>"
    is ListType -> "[${this.itemsType.generateCode()}]"
    is ImageComposableType -> "Image"
    is TextComposableType -> "Text"
    is ColumnComposableType -> "VStack"
    is SpacerComposableType -> "Spacer"
    is ZStackComposableType -> "ZStack"
    is ButtonComposableType -> "Button"
    is DividerComposableType -> "Divider"
    is DpType -> "CGFloat"
    is ColorType -> "Color"
    is AspectRatioType -> "ContentMode"
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}

fun BinaryExpression.generateCode(depth: Int = 0): String = when(this) {
    is SumExpression -> "${left.generateCode(depth)} + ${right.generateCode(depth)}"
    is SubtractionExpression -> "${left.generateCode(depth)} - ${right.generateCode(depth)}"
    is MultiplicationExpression -> "${left.generateCode(depth)} * ${right.generateCode(depth)}"
    is DivisionExpression -> "${left.generateCode(depth)} / ${right.generateCode(depth)}"
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}
fun TextComposableCall.generateCode(depth: Int = 0): String{
    val base =  "Text(${this.value.generateCode(depth)})"
    var colorSuffix = ""
    var boldSuffix = ""
    this.color?.generateCode(depth)?.let {
        colorSuffix = "\n.foregroundColor($it)"
    }
    this.fontWeight?.generateCode(depth)?.let {
        boldSuffix = "\n.fontWeight($it)"
    }
    return "$base$colorSuffix$boldSuffix"
}

fun ColorLit.generateCode(depth: Int = 0): String = when(this){
    is ColorBlue -> "Color.blue"
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}

fun FontWeightLit.generateCode(depth: Int = 0): String = when(this){
    is FontWeightBold -> "Font.Weight.bold"
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}

fun WidgetDeclaration.generateCode(depth: Int = 0): String {
    val convertedProperties = this.parameters.joinToString("\n") { "${getPrefix(depth+1)}var ${it.id}: ${it.type.generateCode()}" }
    val body = "${getPrefix(depth+1)}var body: some View ${body.generateCode(depth + 1)}"
    return "struct $id: View{\n$convertedProperties\n${body}\n}"
}



fun ColumnComposableCall.generateCode(depth: Int = 0): String{
    var param1: String? = null
    horizontalAlignment?.generateCode(depth)?.let {
        param1 ="\n\talignment: $it"
    }
    var param2: String? = null
    spacing?.generateCode(depth)?.let {
        param2 = "\n\tspacing: $it"
    }
    val parameters = listOf(param1, param2).filterNotNull().joinToString(",")

    if(scrollable){
        val bodyString = body.generateCode(depth+1)
        return "ScrollView(.vertical){\n${getPrefix(depth + 1)}VStack($parameters)" +
                "$bodyString\n" +
                "${getPrefix(depth)}}"
    }else{
        val bodyString = body.generateCode(depth)
        return """VStack($parameters)$bodyString"""
    }
}

fun HorizontalAlignment.generateCode(depth: Int = 0): String{
    return when(this){
        is StartAlignment -> "HorizontalAlignment.leading"
        is EndAlignment -> "HorizontalAlignment.trailing"
        is CenterHorizAlignment -> "HorizontalAlignment.center"
    }
}

fun getPrefix(depth: Int): String{
    var prefix = ""
    for (i in 1..depth){
        prefix += "\t"
    }
    return prefix
}


fun ImageComposableCall.generateCode(): String{
    val base =  "Image(${this.value.generateCode()})"

    var resizableSuffix = ""
    var aspectRatioSuffix = ""

   if(this.resizable) {
        resizableSuffix = "\n.resizable()"
    }
    this.aspectRatio?.generateCode()?.let {
        aspectRatioSuffix = "\n.aspectRatio(contentMode: $it)"
    }
    return "$base$resizableSuffix$aspectRatioSuffix"
}
