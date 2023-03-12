package it.poliba.KSTranspiler

import org.stringtemplate.v4.STGroup
import org.stringtemplate.v4.STGroupFile
import java.lang.Exception
import java.util.ArrayList
import java.util.StringJoiner

val kotlinGroup: STGroup = STGroupFile("src/main/antlr/KotlinTemplate.stg")

fun AstFile.generateKotlinCode(): String{
    return declarations.joinToString("\n") { it.generateKotlinCode() }
}
fun AstScript.generateKotlinCode(): String{
    return statement.joinToString("\n") { it.generateKotlinCode() }
}

fun Declaration.generateKotlinCode(): String{
    return when(this){
        is PropertyDeclaration -> this.generateKotlinCode()
        is WidgetDeclaration -> this.generateKotlinCode()
        is FunctionDeclaration -> this.generateKotlinCode()
    }
}
fun Statement.generateKotlinCode(): String {
    return when (this) {
        is PropertyDeclaration -> this.generateKotlinCode()
        is Assignment -> this.generateKotlinCode()
        is Print -> this.generateKotlinCode()
        is IfExpression -> this.generateKotlinCode()
        is Expression -> this.generateKotlinCode()
        is FunctionDeclaration -> this.generateKotlinCode()
        else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
    }
}

fun FunctionDeclaration.generateKotlinCode(): String{
    val returnType = if(this.returnType != null) ": ${this.returnType.generateKotlinCode()}" else ""
    return "fun ${this.id}(${this.parameters.joinToString(", "){it.generateKotlinCode()}})"+returnType +
            "{\n${this.body.generateKotlinCode()}\n}"
}


fun FunctionParameter.generateKotlinCode(): String{
    return "${this.id}: ${this.type.generateKotlinCode()}"
}

fun Assignment.generateKotlinCode(): String{
    return "$varName = ${value.generateKotlinCode()}"
}

fun Print.generateKotlinCode(): String{
    return "print(${value.generateKotlinCode()})"
}

fun IfExpression.generateKotlinCode(): String{
    var result =  "if(${condition.generateKotlinCode()}){\n"+
            "${body.generateKotlinCode()}\n"+
            "}"
    elseBranch?.let {
        when (it){
            is IfExpression ->result += "else "+ it.generateKotlinCode()
            else -> result += "else{\n${it.generateKotlinCode()}\n}"

        }
    }
    return result
}

fun ControlStructureBody.generateKotlinCode(): String{
    return when(this){
        is Block -> this.generateKotlinCode()
        is Statement -> this.generateKotlinCode()
        else -> throw Exception("Not implemented")
    }
}

fun Block.generateKotlinCode(): String{
    return this.body.joinToString("\n") { "\t${it.generateKotlinCode()}" }
}
fun PropertyDeclaration.generateKotlinCode(): String{
    var prefix = if (mutable) "var" else "val"
    var type = type.generateKotlinCode()
    var value = value?.let {  " = ${value.generateKotlinCode()}"} ?: ""
    return "$prefix $varName:$type$value"
}

fun Expression.generateKotlinCode() : String = when (this) {
    is IntLit -> this.value
    is DoubleLit -> this.value
    is DpLit -> "${this.value}.dp"
    is VarReference -> this.varName
    is BinaryExpression -> this.generateKotlinCode()
    is StringLit -> "\"${this.value}\""
    is BoolLit -> "${this.value}"
    is FunctionCall -> "${this.name}(${this.parameters.map { it.generateKotlinCode() }.joinToString(", " )})"
    is RangeExpression -> "${this.leftExpression.generateKotlinCode()}..${this.rightExpression.generateKotlinCode()}"
    is ListExpression -> "[${this.items.map { it.generateKotlinCode() }.joinToString(", ")}]"
    is ColorLit -> this.generateKotlinCode()
    is FontWeightLit -> this.generateKotlinCode()
    is ReturnExpression -> "return ${this.returnExpression.generateKotlinCode()}"
    is TextComposableCall -> this.generateKotlinCode()
    is DividerComposableCall -> this.generateKotlinCode()
    is SpacerComposableCall -> this.generateKotlinCode()
    is ColumnComposableCall -> this.generateKotlinCode()
    is HorizontalAlignment -> this.generateKotlinCode()
    is ButtonComposableCall -> this.generateKotlinCode()
    is AspectRatioLit -> this.generateKotlinCode()
    is ImageComposableCall -> this.generateKotlinCode()
    //is KotlinParser.ParenExpressionContext -> expression().toAst(considerPosition)
    //is KotlinParser.TypeConversionContext -> TypeConversion(expression().toAst(considerPosition), targetType.toAst(considerPosition), toPosition(considerPosition))
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}

fun DividerComposableCall.generateKotlinCode(): String{
    var params: ArrayList<String> = arrayListOf()

    color?.let { params.add("color: ${it.generateKotlinCode()}") }
    frame?.let { params.add("modifier: ${it.generateKotlinCode()}") }

    print("SIZE: ${params.size}")
    var paramString = params.joinToString("")
    return "Divider($paramString)"
}
fun ButtonComposableCall.generateKotlinCode(): String{
    return "Button(onClick = {\n${this.action.generateKotlinCode()}\n}){\n${this.body.generateKotlinCode()}\n} "
}
fun SpacerComposableCall.generateKotlinCode(): String{
    val suffix = size?.let { "${it.generateKotlinCode()}" } ?: ""

    return "Spacer(modifier = $suffix)"
}

fun Frame.generateKotlinCode(): String{
    var width = width?.let { ".width(${width.generateKotlinCode()})" }
    var height = height?.let { ".height(${height.generateKotlinCode()})" }

    val params = listOf(width, height).filterNotNull()

    return "Modifier${params.joinToString("")}"
}


fun Type.generateKotlinCode() : String = when (this) {
    is IntType -> "Int"
    is DoubleType -> "Double"
    is StringType -> "String"
    is BoolType -> "Boolean"
    is RangeType -> "ClosedRange<${this.type.generateKotlinCode()}>"
    is ListType -> "[${this.itemsType.generateKotlinCode()}]"
    is AspectRatioType -> "ContentScale"
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}

fun BinaryExpression.generateKotlinCode(): String = when(this) {
    is SumExpression -> "${left.generateKotlinCode()} + ${right.generateKotlinCode()}"
    is SubtractionExpression -> "${left.generateKotlinCode()} - ${right.generateKotlinCode()}"
    is MultiplicationExpression -> "${left.generateKotlinCode()} * ${right.generateKotlinCode()}"
    is DivisionExpression -> "${left.generateKotlinCode()} / ${right.generateKotlinCode()}"
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}
fun TextComposableCall.generateKotlinCode(): String{
    val parameters = arrayListOf<String>()

    parameters.add("${this.value.generateKotlinCode()}")

    this.color?.generateKotlinCode()?.let {
        parameters.add("color = $it")
    }

    this.fontWeight?.generateKotlinCode()?.let {
        parameters.add("fontWeight = $it")
    }

    return "Text(${parameters.joinToString(", ") { it }})"
}

fun ColorLit.generateKotlinCode(): String = when(this){
    is ColorBlue -> "Color.Blue"
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}

fun FontWeightLit.generateKotlinCode(): String = when(this){
    is FontWeightBold -> "FontWeight.Bold"
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}

fun WidgetDeclaration.generateKotlinCode(): String {
    val convertedProperties = this.parameters.joinToString(", ") { "${it.id}: ${it.type.generateKotlinCode()}" }
    val body = "${body.generateKotlinCode()}"
    return "@Composable\nfun $id($convertedProperties){\n${body}\n}"
}

fun ColumnComposableCall.generateKotlinCode(): String{
    val bodyString = body.generateKotlinCode()

    val arguments = arrayListOf<String>()

    horizontalAlignment?.generateKotlinCode()?.let {
        arguments.add("horizontalAlignment = $it")
    }

    spacing?.generateKotlinCode()?.let {
        arguments.add("verticalArrangement = $it")
    }
    val parameters = arguments.joinToString(", ")
    val stack = """
VStack($parameters){
    $bodyString
}
""".trimIndent()
    if(scrollable){
        return "ScrollView(.vertical){\n\tVStack($parameters){\n" +
                "\t\t$bodyString\n" +
                "\t}\n}"
    }else{
        return """Column($parameters){
    $bodyString
}"""
    }
}

fun AspectRatioLit.generateKotlinCode(depth: Int = 0): String{
    var res = when(this){
        is ContentFit -> "ContentScale.Fit"
        is ContentFill -> "ContentScale.FillWidth"
        else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
    }
    return "$res" //TODO: ADD PREFIX
}


fun ImageComposableCall.generateKotlinCode(): String{
    var imageName = this.value.generateKotlinCode()
    var painter = "painter = painterResource(id = getResources().getIdentifier($imageName, \"drawable\", context.getPackageName()))"

    var parameters = arrayListOf<String>()
    var modifierParams = arrayListOf<String>()
        //TODO: resizable
    this.aspectRatio?.generateKotlinCode()?.let {
        parameters.add("contentScale = $it")
    }
    if(resizable){
        modifierParams.add(".fillMaxSize()")
    }
    zIndex?.let {
        modifierParams.add(".zIndex(${it.generateKotlinCode()}")
    }
    if(modifierParams.isNotEmpty()){
        var modifierParamsString = modifierParams.joinToString("")
        parameters.add("modifier = Modifier$modifierParamsString")
    }
    if(parameters.isEmpty()){
        return "Image($painter)"
    }else{
        var param = parameters.joinToString ( ",\n" )
        return "Image(\n$painter,\n$param)"

    }
}





fun HorizontalAlignment.generateKotlinCode(): String{
    return when(this){
        is StartAlignment -> "Alignment.Start"
        is EndAlignment -> "Alignment.End"
        is CenterHorizAlignment -> "Alignment.Center"
    }
}