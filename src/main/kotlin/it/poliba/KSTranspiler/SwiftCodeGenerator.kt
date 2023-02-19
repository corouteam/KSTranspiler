package it.poliba.KSTranspiler

import org.stringtemplate.v4.STGroup
import org.stringtemplate.v4.STGroupFile
import java.lang.Exception

val group: STGroup = STGroupFile("src/main/antlr/SwiftTemplate.stg")
fun AstFile.generateCode(): String{
    return declarations.joinToString("\n") { it.generateCode() }
}
fun AstScript.generateCode(): String{
    return statement.joinToString("\n") { it.generateCode() }
}

fun Declaration.generateCode(): String{
    return when(this){
        is PropertyDeclaration -> this.generateCode()
        is WidgetDeclaration -> this.generateCode()
        is FunctionDeclaration -> this.generateCode()
    }
}
fun Statement.generateCode(): String {
    return when (this) {
        is PropertyDeclaration -> this.generateCode()
        is Assignment -> this.generateCode()
        is Print -> this.generateCode()
        is IfExpression -> this.generateCode()
        is Expression -> this.generateCode()
        is FunctionDeclaration -> this.generateCode()
        else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
    }
}

fun FunctionDeclaration.generateCode(): String{
    val returnType = if(this.returnType != null) "-> ${this.returnType.generateCode()}" else ""
    return "func ${this.id}(${this.parameters.joinToString(", "){it.generateCode()}})"+returnType +
            "{\n\t${this.body.generateCode()}\n}"
}


fun FunctionParameter.generateCode(): String{
    return "${this.id}: ${this.type.generateCode()}"
}

fun Assignment.generateCode(): String{
    return "$varName = ${value.generateCode()}"
}

fun Print.generateCode(): String{
    return "print(${value.generateCode()})"
}

fun IfExpression.generateCode(): String{
    var result =  "if(${condition.generateCode()}){\n"+
            "\t${body.generateCode()}\n"+
            "}"
    elseBranch?.let {
        when (it){
            is IfExpression ->result += "else "+ it.generateCode()
            else -> result += "else{\n\t${it.generateCode()}\n}"

        }
    }
    return result
}

fun ControlStructureBody.generateCode(): String{
    return when(this){
        is Block -> this.generateCode()
        is Statement -> this.generateCode()
        else -> throw Exception("Not implemented")
    }
}

fun Block.generateCode(): String{
    return this.body.joinToString("\n") { it.generateCode() }
}
fun PropertyDeclaration.generateCode(): String{
    val st = group.getInstanceOf("propertyDeclaration")
    st.add("name",varName)
    st.add("type", type.generateCode())
    value?.let {
        st.add("value", value.generateCode())
    }
    st.add("mutable", mutable)
    return st.render()
}

fun Expression.generateCode() : String = when (this) {
    is IntLit -> this.value
    is DoubleLit -> this.value
    is VarReference -> this.varName
    is BinaryExpression -> this.generateCode()
    is StringLit -> "\"${this.value}\""
    is BoolLit -> "${this.value}"
    is FunctionCall -> "${this.name}(${this.parameters.map { it.generateCode() }.joinToString(", " )})"
    is RangeExpression -> "${this.leftExpression.generateCode()}...${this.rightExpression.generateCode()}"
    is ListExpression -> "[${this.items.map { it.generateCode() }.joinToString(", ")}]"
    is ColorLit -> this.generateCode()
    is FontWeightLit -> this.generateCode()
    is ReturnExpression -> "return ${this.returnExpression.generateCode()}"
    is TextComposableCall -> this.generateCode()
    is ImageComposableCall -> this.generateCode()
    is PainterResource -> this.generateCode()
    //is KotlinParser.ParenExpressionContext -> expression().toAst(considerPosition)
    //is KotlinParser.TypeConversionContext -> TypeConversion(expression().toAst(considerPosition), targetType.toAst(considerPosition), toPosition(considerPosition))
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}

fun Type.generateCode() : String = when (this) {
    is IntType -> "Int"
    is DoubleType -> "Double"
    is StringType -> "String"
    is BoolType -> "Boolean"
    is RangeType -> "ClosedRange<${this.type.generateCode()}>"
    is ListType -> "[${this.itemsType.generateCode()}]"
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}

fun BinaryExpression.generateCode(): String = when(this) {
    is SumExpression -> "${left.generateCode()} + ${right.generateCode()}"
    is SubtractionExpression -> "${left.generateCode()} - ${right.generateCode()}"
    is MultiplicationExpression -> "${left.generateCode()} * ${right.generateCode()}"
    is DivisionExpression -> "${left.generateCode()} / ${right.generateCode()}"
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}
fun TextComposableCall.generateCode(): String{
    val base =  "Text(${this.value.generateCode()})"
    var colorSuffix = ""
    var boldSuffix = ""
    this.color?.generateCode()?.let {
        colorSuffix = "\n.foregroundColor($it)"
    }
    this.fontWeight?.generateCode()?.let {
        boldSuffix = "\n.fontWeight($it)"
    }
    return "$base$colorSuffix$boldSuffix"
}

fun ImageComposableCall.generateCode(): String{
    val base =  "Image(${this.value.generateCode()})"

    var resizableSuffix = ""
    var aspectRatioSuffix = ""

    this.resizable?.generateCode()?.let {
        resizableSuffix = "\n.resizable()"
    }
    this.aspectRatio?.generateCode()?.let {
        aspectRatioSuffix = "\n.aspectRatio(contentMode: $it)"
    }
    return "$base$resizableSuffix$aspectRatioSuffix"
}

fun PainterResource.generateCode(): String{
    var imageName = ""

    this.image?.generateCode().let {
        imageName = it ?: ""
    }
    return imageName
}

fun ColorLit.generateCode(): String = when(this){
    is ColorBlue -> "Color.blue"
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}

fun FontWeightLit.generateCode(): String = when(this){
    is FontWeightBold -> "Font.Weight.bold"
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}

fun WidgetDeclaration.generateCode(): String {
    val convertedProperties = this.parameters.joinToString("\n") { "var ${it.id}: ${it.type.generateCode()}" }
    val body = "var body: some View {\n ${body.generateCode()}\n}"
    return "struct $id: View{\n$convertedProperties\n${body}\n}"
}