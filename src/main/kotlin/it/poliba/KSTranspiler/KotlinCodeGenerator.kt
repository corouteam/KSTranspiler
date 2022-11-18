package it.poliba.KSTranspiler

import it.poliba.KSranspiler.*
import org.stringtemplate.v4.STGroup
import org.stringtemplate.v4.STGroupFile
import java.util.function.BinaryOperator

val group: STGroup = STGroupFile("/Users/annalabellarte/Dev/Università/KSTranspiler2/src/main/antlr/test.stg")

fun SandyFile.generateCode(): String{
    return statements.map { it.generateCode() }.joinToString("\n")
}

fun Statement.generateCode(): String {
    return when (this) {
        is VarDeclaration -> this.generateCode()
        is ReadOnlyVarDeclaration -> this.generateCode()
        is Assignment -> "TODO ASSIGNMENT"
        is Print -> "TODO PRINT"
        else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
    }
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
    //is SandyParser.ParenExpressionContext -> expression().toAst(considerPosition)
    //is SandyParser.TypeConversionContext -> TypeConversion(expression().toAst(considerPosition), targetType.toAst(considerPosition), toPosition(considerPosition))
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}

fun BinaryExpression.generateCode(): String = when(this){
    is SumExpression -> "${left.generateCode()} + ${right.generateCode()}"
    is SubtractionExpression -> "${left.generateCode()} + ${right.generateCode()}"
    is MultiplicationExpression -> "${left.generateCode()} + ${right.generateCode()}"
    is DivisionExpression -> "${left.generateCode()} + ${right.generateCode()}"
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}