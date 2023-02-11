package it.poliba.KSTranspiler

import org.stringtemplate.v4.STGroup
import org.stringtemplate.v4.STGroupFile
import java.lang.Exception
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
            "{\n\t${this.body.generateKotlinCode()}\n}"
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
            "\t${body.generateKotlinCode()}\n"+
            "}"
    elseBranch?.let {
        when (it){
            is IfExpression ->result += "else "+ it.generateKotlinCode()
            else -> result += "else{\n\t${it.generateKotlinCode()}\n}"

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
    return this.body.joinToString("\n") { it.generateKotlinCode() }
}
fun PropertyDeclaration.generateKotlinCode(): String{
    val st = kotlinGroup.getInstanceOf("propertyDeclaration")
    st.add("name",varName)
    st.add("type", type.generateKotlinCode())
    value?.let {
        st.add("value", value.generateKotlinCode())
    }
    st.add("mutable", mutable)
    return st.render()
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
    is RangeExpression -> "${this.leftExpression.generateKotlinCode()}...${this.rightExpression.generateKotlinCode()}"
    is ListExpression -> "[${this.items.map { it.generateKotlinCode() }.joinToString(", ")}]"
    is ColorLit -> this.generateKotlinCode()
    is FontWeightLit -> this.generateKotlinCode()
    is ReturnExpression -> "return ${this.returnExpression.generateKotlinCode()}"
    is TextComposableCall -> this.generateKotlinCode()
    is DividerComposableCall -> this.generateKotlinCode()
    is SpacerComposableCall -> this.generateKotlinCode()
    is ColumnComposableCall -> this.generateKotlinCode()
    is HorizontalAlignment -> this.generateKotlinCode()
    //is KotlinParser.ParenExpressionContext -> expression().toAst(considerPosition)
    //is KotlinParser.TypeConversionContext -> TypeConversion(expression().toAst(considerPosition), targetType.toAst(considerPosition), toPosition(considerPosition))
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}

fun DividerComposableCall.generateKotlinCode(): String{
    var suffix = frame?.generateKotlinCode() ?: ""
    return "Divider()$suffix"
}

fun SpacerComposableCall.generateKotlinCode(): String{
    val suffix = size?.generateKotlinCode() ?: ""

    return "Spacer()$suffix"
}

fun Frame.generateKotlinCode(): String{
    return "\n\t.size(width: ${width.generateKotlinCode()}, height: ${height.generateKotlinCode()})"
}


fun Type.generateKotlinCode() : String = when (this) {
    is IntType -> "Int"
    is DoubleType -> "Double"
    is StringType -> "String"
    is BoolType -> "Bool"
    //TODO is RangeType -> "ClosedRange<${this.type.generateKotlinCode()}>"
    //TODO is ListType -> "[${this.itemsType.generateKotlinCode()}]"
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}

//TODO: Implement this
/*
fun BinaryExpression.generateKotlinCode(): String = when(this) {
    is SumExpression -> "${left.generateKotlinCode()} + ${right.generateKotlinCode()}"
    is SubtractionExpression -> "${left.generateKotlinCode()} - ${right.generateKotlinCode()}"
    is MultiplicationExpression -> "${left.generateKotlinCode()} * ${right.generateKotlinCode()}"
    is DivisionExpression -> "${left.generateKotlinCode()} / ${right.generateKotlinCode()}"
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}

fun TextComposableCall.generateKotlinCode(): String{
    val base =  "Text(${this.value.generateKotlinCode()})"
    var colorSuffix = ""
    var boldSuffix = ""
    this.color?.generateKotlinCode()?.let {
        colorSuffix = "\n.foregroundColor($it)"
    }
    this.fontWeight?.generateKotlinCode()?.let {
        boldSuffix = "\n.fontWeight($it)"
    }
    return "$base$colorSuffix$boldSuffix"
}

fun ColorLit.generateKotlinCode(): String = when(this){
    is ColorBlue -> "Color.blue"
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}

fun FontWeightLit.generateKotlinCode(): String = when(this){
    is FontWeightBold -> "Font.Weight.bold"
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}

fun WidgetDeclaration.generateKotlinCode(): String {
    val convertedProperties = this.parameters.joinToString("\n") { "var ${it.id}: ${it.type.generateKotlinCode()}" }
    val body = "var body: some View {\n ${body.generateKotlinCode()}\n}"
    return "struct $id: View{\n$convertedProperties\n${body}\n}"
}

fun ColumnComposableCall.generateKotlinCode(): String{
    val bodyString = body.generateKotlinCode()
    var param1: String? = null
    horizontalAlignment?.generateKotlinCode()?.let {
        param1 ="\n\talignment: $it"
    }
    var param2: String? = null
    spacing?.generateKotlinCode()?.let {
        param2 = "\n\tspacing: $it"
    }
    val parameters = listOf(param1, param2).filterNotNull().joinToString(",")
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
        return """VStack($parameters){
    $bodyString
}"""
    }
}

fun HorizontalAlignment.generateKotlinCode(): String{
    return when(this){
        StartAlignment -> "HorizontalAlignment.start"
        EndAlignment -> "HorizontalAlignment.end"
        CenterHorizAlignment -> "HorizontalAlignment.center"
    }
}*/
