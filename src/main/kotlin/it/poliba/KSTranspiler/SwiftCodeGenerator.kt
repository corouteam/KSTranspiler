package it.poliba.KSTranspiler

import it.poliba.KSranspiler.*
import org.stringtemplate.v4.STGroup
import org.stringtemplate.v4.STGroupFile

val group: STGroup = STGroupFile("/Users/francescopaolodellaquila/Desktop/UNIVERSITA/Magistrale/Language/KSTranspiler/src/main/antlr/SwiftTemplate.stg")

fun KotlinFile.generateCode(): String{
    return statements.map { it.generateCode() }.joinToString("\n")
}

fun Statement.generateCode(): String {
    return when (this) {
        is PropertyDeclaration -> this.generateCode()
        is VarDeclaration -> this.generateCode()
        is ReadOnlyVarDeclaration -> this.generateCode()
        is Assignment -> this.generateCode()
        is Print -> this.generateCode()
        else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
    }
}

fun PropertyDeclaration.generateCode(): String{
    val st = group.getInstanceOf("propertyDeclaration")
    st.add("name",varName)
    st.add("type", type.generateCode())
    st.add("value", value.generateCode())
    st.add("mutable", mutable)
    return st.render()
}

fun VarDeclaration.generateCode(): String{
    val st = group.getInstanceOf("decl")
    st.add("type", "var")
    st.add("name", this.varName)
    st.add("value", this.value.generateCode())
    return st.render()
}

fun ReadOnlyVarDeclaration.generateCode(): String{
    val st = group.getInstanceOf("decl")
    st.add("type", "let")
    st.add("name", this.varName)
    st.add("value", this.value.generateCode())
    return st.render()
}

fun Expression.generateCode() : String = when (this) {
    is IntLit -> this.value
    is DecLit -> this.value
    is VarReference -> this.varName
    is BinaryExpression -> this.generateCode()
    //is KotlinParser.ParenExpressionContext -> expression().toAst(considerPosition)
    //is KotlinParser.TypeConversionContext -> TypeConversion(expression().toAst(considerPosition), targetType.toAst(considerPosition), toPosition(considerPosition))
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}

fun Type.generateCode() : String = when (this) {
    is IntType -> "Int"
    is DecimalType -> "Double"
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