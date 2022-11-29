package it.poliba.KSTranspiler

import it.poliba.KSranspiler.*
import org.stringtemplate.v4.STGroup
import org.stringtemplate.v4.STGroupFile

val group: STGroup = STGroupFile("/Users/annalabellarte/Dev/Università/KSTranspiler2/src/main/antlr/SwiftTemplate.stg")
fun KotlinFile.generateCode(): String{
     return statements.map { it.generateCode() }.joinToString("\n")
}

fun Statement.generateCode(): String {
    return when (this) {
        is PropertyDeclaration -> this.generateCode()
        is Assignment -> this.generateCode()
        is Print -> this.generateCode()
        else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
    }
}

fun Assignment.generateCode(): String{
    return "$varName = ${value.generateCode()}"
}

fun Print.generateCode(): String{
    return "print(${value.generateCode()})"
}

fun PropertyDeclaration.generateCode(): String{
    val st = group.getInstanceOf("propertyDeclaration")
    st.add("name",varName)
    st.add("type", type.generateCode())
    st.add("value", value.generateCode())
    st.add("mutable", mutable)
    return st.render()
}

fun Expression.generateCode() : String = when (this) {
    is IntLit -> this.value
    is DoubleLit -> this.value
    is VarReference -> this.varName
    is BinaryExpression -> this.generateCode()
    is StringLit -> "\"${this.value}\""
    //is KotlinParser.ParenExpressionContext -> expression().toAst(considerPosition)
    //is KotlinParser.TypeConversionContext -> TypeConversion(expression().toAst(considerPosition), targetType.toAst(considerPosition), toPosition(considerPosition))
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}

fun Type.generateCode() : String = when (this) {
    is IntType -> "Int"
    is DoubleType -> "Double"
    is StringType -> "String"
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}

fun BinaryExpression.generateCode(): String = when(this){
    is SumExpression -> "${left.generateCode()} + ${right.generateCode()}"
    is SubtractionExpression -> "${left.generateCode()} - ${right.generateCode()}"
    is MultiplicationExpression -> "${left.generateCode()} * ${right.generateCode()}"
    is DivisionExpression -> "${left.generateCode()} / ${right.generateCode()}"
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}