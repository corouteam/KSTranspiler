package it.poliba.KSTranspiler

import org.stringtemplate.v4.STGroup
import org.stringtemplate.v4.STGroupFile
import java.lang.Exception
import java.lang.reflect.Constructor
import java.util.ArrayList
import java.util.StringJoiner

val kotlinGroup: STGroup = STGroupFile("src/main/antlr/KotlinTemplate.stg")

fun AstFile.generateKotlinCode(depth: Int = 0): String{
    return declarations.joinToString("\n") { it.generateKotlinCode(depth) }
}
fun AstScript.generateKotlinCode(depth: Int = 0): String{
    return statement.joinToString("\n") { it.generateKotlinCode(depth) }
}

fun Declaration.generateKotlinCode(depth: Int = 0): String{
    return when(this){
        is PropertyDeclaration -> this.generateKotlinCode(depth)
        is WidgetDeclaration -> this.generateKotlinCode(depth)
        is FunctionDeclaration -> this.generateKotlinCode(depth)
        is ClassDeclaration -> this.generateKotlinCode(depth)
        is DataClassDeclaration -> this.generateKotlinCode(depth)
        is PrimaryConstructor -> this.generateKotlinCode(depth)
    }
}

fun PrimaryConstructor.generateKotlinCode(depth: Int = 0): String{
    return "${getPrefix(depth)}init ${this.body.generateKotlinCode(depth)}"
}

fun ClassDeclaration.generateKotlinCode(depth: Int = 0): String{
    var baseClassesString = ""
    if(baseClasses.isNotEmpty()){
        baseClassesString += ": "
        baseClassesString += baseClasses.joinToString(", "){it.generateCode()}
    }
    var bodyStatement = body.map { it.generateKotlinCode(depth+1) }
    var bodyString = bodyStatement.joinToString("\n"){"$it"}
    var constructor = ""
    (body.firstOrNull { it is PrimaryConstructor } as PrimaryConstructor).parameters?.let {
        if(it.isNotEmpty()){
            var params = it.joinToString(",\n") { it.generateKotlinCode() }
            constructor = "(\n$params\n)"
        }
    }
    var res = "${getPrefix(depth)}class $name$constructor$baseClassesString {\n$bodyString\n${getPrefix(depth)}}"
    return res
}

fun DataClassDeclaration.generateKotlinCode(depth: Int = 0): String{
    var baseClassesString = ""
    if(baseClasses.isNotEmpty()){
        baseClassesString += ": "
        baseClassesString += baseClasses.joinToString(", "){it.generateCode()}
    }
    var bodyStatement = body.map { it.generateKotlinCode(depth+1) }
    var bodyString = bodyStatement.joinToString("\n"){"$it"}
    var constructor = ""
    if(propertyList.isNotEmpty()){
        var params = propertyList.joinToString(",\n") { it.generateKotlinCode(depth +1) }
        constructor = "(\n$params\n)"
    }

    var res = "${getPrefix(depth)}data class $name$constructor$baseClassesString {\n$bodyString\n${getPrefix(depth)}}"
    return res
}
fun Statement.generateKotlinCode(depth: Int = 0): String {
    return when (this) {
        is PropertyDeclaration -> this.generateKotlinCode(depth)
        is Assignment -> this.generateKotlinCode(depth)
        is Print -> this.generateKotlinCode(depth)
        is IfExpression -> this.generateKotlinCode(depth)
        is Expression -> this.generateKotlinCode(depth)
        is FunctionDeclaration -> this.generateKotlinCode(depth)
        else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
    }
}

fun FunctionDeclaration.generateKotlinCode(depth: Int = 0): String{
    val returnType = if(this.returnType != null) ": ${this.returnType.generateKotlinCode()}" else ""
    return "${getPrefix(depth)}fun ${this.id}(${this.parameters.joinToString(", "){it.generateKotlinCode()}})"+returnType +
            "${this.body.generateKotlinCode(depth)}"
}


fun FunctionParameter.generateKotlinCode(): String{
    return "${this.id}: ${this.type.generateKotlinCode()}"
}

fun Assignment.generateKotlinCode(depth: Int = 0): String{
    return "${getPrefix(depth)}${variable.generateKotlinCode()} = ${value.generateKotlinCode()}"
}

fun Print.generateKotlinCode(depth: Int = 0): String{
    return "${getPrefix(depth)}print(${value.generateKotlinCode()})"
}

fun IfExpression.generateKotlinCode(depth: Int = 0): String{
    var result =  "${getPrefix(depth)}if(${condition.generateKotlinCode()}) "+
            "${body.generateKotlinCode(depth)}"
    elseBranch?.let {
        when (it){
            is IfExpression ->result += "else "+ it.generateKotlinCode(depth)
            else -> result += "else ${it.generateKotlinCode()}"

        }
    }
    return result
}

fun ControlStructureBody.generateKotlinCode(depth: Int = 0): String{
    return when(this){
        is Block -> this.generateKotlinCode(depth)
        is Statement -> this.generateKotlinCode(depth)
        else -> throw Exception("Not implemented")
    }
}

fun Block.generateKotlinCode(depth: Int = 0): String{
    return "{\n${this.body.joinToString("\n") { it.generateKotlinCode(depth+1)}}\n${getPrefix(depth)}}"
}
fun PropertyDeclaration.generateKotlinCode(depth: Int = 0): String{
    var prefix = if (mutable) "var" else "val"
    var type = type.generateKotlinCode()
    var value = value?.let {  " = ${value.generateKotlinCode()}"} ?: ""
    return "${getPrefix(depth)}$prefix $varName:$type$value"
}

fun Expression.generateKotlinCode(depth: Int=0) : String = when (this) {
    is IntLit -> "${getPrefix(depth)}${this.value}"
    is DoubleLit -> "${getPrefix(depth)}${this.value}"
    is DpLit -> "${getPrefix(depth)}${this.value}.dp"
    is VarReference -> "${getPrefix(depth)}${this.varName}"
    is BinaryExpression -> this.generateKotlinCode(depth)
    is StringLit -> "${getPrefix(depth)}\"${this.value}\""
    is BoolLit -> "${getPrefix(depth)}${this.value}"
    is FunctionCall -> "${getPrefix(depth)}${this.name}(${this.parameters.map { it.generateKotlinCode() }.joinToString(", " )})"
    is RangeExpression -> "${getPrefix(depth)}${this.leftExpression.generateKotlinCode()}..${this.rightExpression.generateKotlinCode()}"
    is ListExpression -> "${getPrefix(depth)}[${this.items.map { it.generateKotlinCode() }.joinToString(", ")}]"
    is ColorLit -> this.generateKotlinCode(depth)
    is FontWeightLit -> this.generateKotlinCode(depth)
    is ReturnExpression -> "${getPrefix(depth)}return ${this.returnExpression.generateKotlinCode()}"
    is TextComposableCall -> this.generateKotlinCode(depth)
    is DividerComposableCall -> this.generateKotlinCode(depth)
    is SpacerComposableCall -> this.generateKotlinCode(depth)
    is ColumnComposableCall -> this.generateKotlinCode(depth)
    is HorizontalAlignment -> this.generateKotlinCode(depth)
    is ButtonComposableCall -> this.generateKotlinCode(depth)
    is AccessExpression -> this.generateKotlinCode(depth)
    is ThisExpression -> this.generateKotlinCode(depth)
    //is KotlinParser.ParenExpressionContext -> expression().toAst(considerPosition)
    //is KotlinParser.TypeConversionContext -> TypeConversion(expression().toAst(considerPosition), targetType.toAst(considerPosition), toPosition(considerPosition))
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}

fun DividerComposableCall.generateKotlinCode(depth: Int=0): String{
    var params: ArrayList<String> = arrayListOf()

    color?.let { params.add("color: ${it.generateKotlinCode()}") }
    frame?.let { params.add("modifier: ${it.generateKotlinCode()}") }

    print("SIZE: ${params.size}")
    var paramString = params.joinToString("")
    return "${getPrefix(depth)}Divider($paramString)"
}
fun ButtonComposableCall.generateKotlinCode(depth: Int=0): String{
    return "${getPrefix(depth)}Button(onClick = ${this.action.generateKotlinCode(depth)})${this.body.generateKotlinCode(depth)} "
}
fun SpacerComposableCall.generateKotlinCode(depth: Int=0): String{
    val suffix = size?.let { "${it.generateKotlinCode()}" } ?: ""

    return "${getPrefix(depth)}Spacer(modifier = $suffix)"
}

fun Frame.generateKotlinCode(depth: Int=0): String{
    var width = width?.let { ".width(${width.generateKotlinCode()})" }
    var height = height?.let { ".height(${height.generateKotlinCode()})" }

    val params = listOf(width, height).filterNotNull()

    return "${getPrefix(depth)}Modifier${params.joinToString("")}"
}


fun Type.generateKotlinCode(depth: Int=0) : String = when (this) {
    is IntType -> "Int"
    is DoubleType -> "Double"
    is StringType -> "String"
    is BoolType -> "Boolean"
    is RangeType -> "ClosedRange<${this.type.generateKotlinCode()}>"
    is ListType -> "[${this.itemsType.generateKotlinCode()}]"
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}

fun BinaryExpression.generateKotlinCode(depth: Int=0): String = when(this) {
    is SumExpression -> "${getPrefix(depth)}${left.generateKotlinCode()} + ${right.generateKotlinCode()}"
    is SubtractionExpression -> "${getPrefix(depth)}${left.generateKotlinCode()} - ${right.generateKotlinCode()}"
    is MultiplicationExpression -> "${getPrefix(depth)}${left.generateKotlinCode()} * ${right.generateKotlinCode()}"
    is DivisionExpression -> "${getPrefix(depth)}${left.generateKotlinCode()} / ${right.generateKotlinCode()}"
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}
fun TextComposableCall.generateKotlinCode(depth: Int=0): String{
    val parameters = arrayListOf<String>()

    parameters.add("${this.value.generateKotlinCode()}")

    this.color?.generateKotlinCode()?.let {
        parameters.add("color = $it")
    }

    this.fontWeight?.generateKotlinCode()?.let {
        parameters.add("fontWeight = $it")
    }

    return "${getPrefix(depth)}Text(${parameters.joinToString(", ") { it }})"
}

fun ColorLit.generateKotlinCode(depth: Int=0): String = when(this){
    is ColorBlue -> "${getPrefix(depth)}Color.Blue"
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}

fun FontWeightLit.generateKotlinCode(depth: Int=0): String = when(this){
    is FontWeightBold -> "${getPrefix(depth)}FontWeight.Bold"
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}

fun WidgetDeclaration.generateKotlinCode(depth: Int=0): String {
    val convertedProperties = this.parameters.joinToString(", ") { "${it.id}: ${it.type.generateKotlinCode()}" }
    val body = "${body.generateKotlinCode()}"
    return "${getPrefix(depth)}@Composable\n${getPrefix(depth)}fun $id($convertedProperties)${body}"
}

fun ColumnComposableCall.generateKotlinCode(depth: Int=0): String{

    val arguments = arrayListOf<String>()

    horizontalAlignment?.generateKotlinCode()?.let {
        arguments.add("horizontalAlignment = $it")
    }

    spacing?.generateKotlinCode()?.let {
        arguments.add("verticalArrangement = $it")
    }
    val parameters = arguments.joinToString(", ")
    //TODO: CHECK SCOLLABLE AFTER FIX
    if(scrollable){
        val bodyString = body.generateKotlinCode(depth+1)
        return "${getPrefix(depth)}ScrollView(.vertical){\n${getPrefix(depth)}VStack($parameters)" +
                "$bodyString\n${getPrefix(depth)}}"
    }else{
        val bodyString = body.generateKotlinCode(depth)
        return """${getPrefix(depth)}Column($parameters)$bodyString"""
    }
}

fun HorizontalAlignment.generateKotlinCode(depth: Int=0): String{
    return when(this){
        is StartAlignment -> "${getPrefix(depth)}Alignment.Start"
        is EndAlignment -> "${getPrefix(depth)}Alignment.End"
        is CenterHorizAlignment -> "${getPrefix(depth)}Alignment.Center"
    }
}
fun AccessExpression.generateKotlinCode(depth: Int = 0): String{
    return "${prefix.generateKotlinCode(depth)}${accessOperator.generateKotlinCode()}${child.generateKotlinCode()}"
}

fun AccessOperator.generateKotlinCode(depth: Int=0): String{
    return when (this){
        is DotOperator -> "."
        is ElvisOperator -> "?."
    }
}
fun ThisExpression.generateKotlinCode(depth: Int=0): String{
    return "${getPrefix(depth)}this"
}
