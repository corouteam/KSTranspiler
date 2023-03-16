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
        is ClassDeclaration -> this.generateCode(depth)
        is DataClassDeclaration -> this.generateCode(depth)
        is PrimaryConstructor -> this.generateCode(depth)
    }
}

fun PrimaryConstructor.generateCode(depth: Int = 0): String{
    return "${getPrefix(depth)}init(${this.parameters.joinToString(", "){it.generateCode()}}) "+
            this.body.generateCode(depth)
}

fun DataClassDeclaration.generateCode(depth: Int = 0): String{
    var baseClassesString = ""
    if(baseClasses.isNotEmpty()){
        baseClassesString += ": "
        baseClassesString += baseClasses.joinToString(", "){it.generateCode()}
    }

    var bodyString = body.joinToString(""){"\n${it.generateCode(depth+1)}"}
    var params = propertyList.joinToString("\n"){"${it.generateCode(depth+1)}"}

    var res = "${getPrefix(depth)}struct $name$baseClassesString {\n$params$bodyString\n${getPrefix(depth)}}"
    return res
}

fun ClassDeclaration.generateCode(depth: Int = 0): String{
    var baseClassesString = ""
    if(baseClasses.isNotEmpty()){
        baseClassesString += ": "
        baseClassesString += baseClasses.joinToString(", "){it.generateCode()}
    }

    var bodyString = body.joinToString("\n"){"${it.generateCode(depth+1)}"}

    var res = "${getPrefix(depth)}class $name$baseClassesString {\n$bodyString\n${getPrefix(depth)}}"
    return res
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
    return "${getPrefix(depth)}func ${this.id}(${this.parameters.joinToString(", "){it.generateCode()}})"+returnType +
            "${this.body.generateCode(depth)}"
}


fun FunctionParameter.generateCode(): String{
    return "${this.id}: ${this.type.generateCode()}"
}

fun Assignment.generateCode(depth: Int = 0): String{
    return "${getPrefix(depth)}${variable.generateCode()} = ${value.generateCode()}"
}

fun Print.generateCode(depth: Int = 0): String{
    return "${getPrefix(depth)}print(${value.generateCode()})"
}

fun IfExpression.generateCode(depth: Int = 0): String{
    var result =  "${getPrefix(depth)}if(${condition.generateCode()}) ${body.generateCode(depth)}"
    elseBranch?.let {
        when (it){
            is IfExpression ->result += " else "+ it.generateCode(depth)
            else -> result += " else ${it.generateCode(depth)}"

        }
    }
    return result
}

fun ForExpression.generateCode(depth: Int = 0): String{
    return "${getPrefix(depth)}for $varName in ${range.generateCode()} {\n"+
            "\t${body.generateCode()}\n"+
            "}"
}

fun ControlStructureBody.generateCode(depth: Int = 0): String{
    return when(this){
        is Block -> "${this.generateCode(depth)}"
        is Statement -> "${this.generateCode(depth)}"
        else -> throw Exception("Not implemented")
    }
}

fun Block.generateCode(depth: Int = 0): String{
    return "{\n${this.body.joinToString("\n") { it.generateCode(depth+1) }}\n${getPrefix(depth)}}"
}
fun PropertyDeclaration.generateCode(depth: Int = 0): String{
    var prefix = if (mutable) "var" else "let"
    var type = type.generateCode()
    var value = value?.let {  " = ${value.generateCode()}"} ?: ""
    return "${getPrefix(depth)}$prefix $varName:$type$value"
}

fun Expression.generateCode(depth: Int = 0) : String = when (this) {
    is IntLit -> "${getPrefix(depth)}${this.value}"
    is DoubleLit -> "${getPrefix(depth)}${this.value}"
    is DpLit -> "${getPrefix(depth)}CGFloat(${this.value})"
    is VarReference ->"${getPrefix(depth)}${this.varName}"
    is BinaryExpression -> this.generateCode(depth)
    is StringLit -> "${getPrefix(depth)}\"${this.value}\""
    is BoolLit -> "${getPrefix(depth)}${this.value}"
    is FunctionCall -> "${getPrefix(depth)}${this.name}(${this.parameters.map { it.generateCode(depth) }.joinToString(", " )})"
    is RangeExpression -> "${getPrefix(depth)}${this.leftExpression.generateCode(depth)}...${this.rightExpression.generateCode(depth)}"
    is ListExpression -> "${getPrefix(depth)}[${this.items.map { it.generateCode(depth) }.joinToString(", ")}]"
    is ColorLit -> this.generateCode(depth)
    is FontWeightLit -> this.generateCode(depth)
    is ReturnExpression -> "${getPrefix(depth)}return ${this.returnExpression.generateCode()}"
    is TextComposableCall -> this.generateCode(depth)
    is DividerComposableCall -> this.generateCode(depth)
    is SpacerComposableCall -> this.generateCode(depth)
    is ColumnComposableCall -> this.generateCode(depth)
    is RowComposableCall -> this.generateCode(depth)
    is HorizontalAlignment -> this.generateCode(depth)
    is ButtonComposableCall -> this.generateCode(depth)
    is AccessExpression -> this.generateCode(depth)
    is ThisExpression -> this.generateCode(depth)
    is AspectRatioLit -> this.generateCode(depth)
    is ForExpression -> this.generateCode(depth)
    //is KotlinParser.ParenExpressionContext -> expression().toAst(considerPosition)
    //is KotlinParser.TypeConversionContext -> TypeConversion(expression().toAst(considerPosition), targetType.toAst(considerPosition), toPosition(considerPosition))
    is ImageComposableCall -> this.generateCode(depth)
    is VerticalAlignment -> this.generateCode(depth)
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
    return "${getPrefix(depth)}Divider()$paramString"
}
fun ButtonComposableCall.generateCode(depth: Int = 0): String{
    return "${getPrefix(depth)}Button(action: ${this.action.generateCode(depth)})${this.body.generateCode(depth)}"
}
fun SpacerComposableCall.generateCode(depth: Int = 0): String{
    val suffix = size?.let { "\n\t${it.generateCode(depth)}" } ?: ""

    return "${getPrefix(depth)}Spacer()$suffix"
}

fun Frame.generateCode(depth: Int = 0): String{
    var width = width?.let { "width: ${width.generateCode()}" }?: ""
    var height = height?.let { "height: ${height.generateCode()}" }?: ""
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
    is UserType -> this.name
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
    is FontWeightType -> "Font.Weight"
    is HorizontalAlignmentType -> "HorizontalAlignment"
    is VerticalAlignmentType -> "VerticalAlignment"
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}

fun BinaryExpression.generateCode(depth: Int = 0): String {
    var exp = when(this) {
        is SumExpression -> "${left.generateCode()} + ${right.generateCode()}"
        is SubtractionExpression -> "${left.generateCode()} - ${right.generateCode()}"
        is MultiplicationExpression -> "${left.generateCode()} * ${right.generateCode()}"
        is DivisionExpression -> "${left.generateCode()} / ${right.generateCode()}"
        else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
    }
    return "${getPrefix(depth)}${exp}"
}
fun TextComposableCall.generateCode(depth: Int = 0): String{

    val base =  "Text(${this.value.generateCode()})"
    var colorSuffix = ""
    var boldSuffix = ""
    this.color?.generateCode(depth)?.let {
        colorSuffix = "\n.foregroundColor($it)"
    }
    this.fontWeight?.generateCode(depth)?.let {
        boldSuffix = "\n.fontWeight($it)"
    }
    return "${getPrefix(depth)}$base$colorSuffix$boldSuffix"
}

fun ColorLit.generateCode(depth: Int = 0): String = when(this){
    is ColorBlue -> "${getPrefix(depth)}Color.blue"
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}

fun FontWeightLit.generateCode(depth: Int = 0): String = when(this){
    is FontWeightBold -> "${getPrefix(depth)}Font.Weight.bold"
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}

fun WidgetDeclaration.generateCode(depth: Int = 0): String {
    val convertedProperties = this.parameters.joinToString("\n") { "${getPrefix(depth+1)}var ${it.id}: ${it.type.generateCode()}" }
    val body = "${getPrefix(depth+1)}var body: some View ${body.generateCode(depth + 1)}"
    return "${getPrefix(depth)}struct $id: View{\n$convertedProperties\n${body}\n}"
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
        return "${getPrefix(depth)}ScrollView(.vertical){\n${getPrefix(depth + 1)}VStack($parameters)" +
                "$bodyString\n" +
                "${getPrefix(depth)}}"
    }else{
        val bodyString = body.generateCode(depth)
        return """${getPrefix(depth)}VStack($parameters)$bodyString"""
    }
}

fun RowComposableCall.generateCode(depth: Int = 0): String{
    var param1: String? = null
    verticalAlignment?.generateCode(depth)?.let {
        param1 ="\n\talignment: $it"
    }
    var param2: String? = null
    spacing?.generateCode(depth)?.let {
        param2 = "\n\tspacing: $it"
    }
    val parameters = listOf(param1, param2).filterNotNull().joinToString(",")

    if(scrollable){
        val bodyString = body.generateCode(depth+1)
        return "${getPrefix(depth)}ScrollView(.horizontal){\n${getPrefix(depth + 1)}HStack($parameters)" +
                "$bodyString\n" +
                "${getPrefix(depth)}}"
    }else{
        val bodyString = body.generateCode(depth)
        return """${getPrefix(depth)}HStack($parameters)$bodyString"""
    }
}

fun HorizontalAlignment.generateCode(depth: Int = 0): String{
    var alignment = when(this){
        is StartAlignment -> "HorizontalAlignment.leading"
        is EndAlignment -> "HorizontalAlignment.trailing"
        is CenterHorizAlignment -> "HorizontalAlignment.center"
    }
    return "${getPrefix(depth)}$alignment"
}

fun VerticalAlignment.generateCode(depth: Int = 0): String{
    var alignment = when(this){
        is TopAlignment -> "VerticalAlignment.top"
        is BottomAlignment -> "VerticalAlignment.bottom"
        is CenterVerticallyAlignment -> "VerticalAlignment.center"
    }
    return "${getPrefix(depth)}$alignment"
}

fun AccessExpression.generateCode(depth: Int): String{
    return "${prefix.generateCode(depth)}${accessOperator.generateCode()}${child.generateCode()}"
}

fun AccessOperator.generateCode(): String{
    return when (this){
        is DotOperator -> "."
        is ElvisOperator -> "?."
    }
}

fun ThisExpression.generateCode(depth: Int): String{
    return "${getPrefix(depth)}self"
}


fun getPrefix(depth: Int): String{
    var prefix = ""
    for (i in 1..depth){
        prefix += "\t"
    }
    return prefix
}


fun ImageComposableCall.generateCode(depth: Int = 0): String{
    val base =  "${getPrefix(depth)}Image(${this.value.generateCode()})"

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
