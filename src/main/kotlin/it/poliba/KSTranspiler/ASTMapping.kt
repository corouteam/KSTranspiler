package it.poliba.KSTranspiler

import com.strumenta.kolasu.model.Node
import com.strumenta.kolasu.model.Point
import com.strumenta.kolasu.model.Position
import it.poliba.KSTranspiler.KotlinParser.PropertyDeclarationContext
import it.poliba.KSTranspiler.KotlinParser.PropertyDeclarationStatementContext
import it.poliba.KSTranspiler.KotlinParser.VarDeclarationContext
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.Token

import it.poliba.KSranspiler.*

interface ParseTreeToAstMapper<in PTN : ParserRuleContext, out ASTN : Node> {
    fun map(parseTreeNode: PTN) : ASTN
}

fun KotlinParser.KotlinFileContext.toAst(considerPosition: Boolean = false) : KotlinFile = KotlinFile(this.line().map { it.statement().toAst(considerPosition) }, toPosition(considerPosition))

fun Token.startPoint() = Point(line, charPositionInLine)

fun Token.endPoint() = Point(line, charPositionInLine + text.length)

fun ParserRuleContext.toPosition(considerPosition: Boolean) : Position? {
    return if (considerPosition) Position(start.startPoint(), stop.endPoint()) else null
}

fun KotlinParser.StatementContext.toAst(considerPosition: Boolean = false) : Statement = when (this) {
    is KotlinParser.PropertyDeclarationStatementContext -> this.propertyDeclaration().toAst(considerPosition)
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
    /*is KotlinParser.VarDeclarationStatementContext -> VarDeclaration(varDeclaration().assignment().ID().text, varDeclaration().assignment().expression().toAst(considerPosition), toPosition(considerPosition))
    is KotlinParser.LetDeclarationStatementContext -> ReadOnlyVarDeclaration(letDeclaration().assignment().ID().text, letDeclaration().assignment().expression().toAst(considerPosition), toPosition(considerPosition))
    is KotlinParser.AssignmentStatementContext -> Assignment(assignment().ID().text, assignment().expression().toAst(considerPosition), toPosition(considerPosition))
    is KotlinParser.PrintStatementContext -> Print(print().expression().toAst(considerPosition), toPosition(considerPosition))
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)*/
}

fun KotlinParser.PropertyDeclarationContext.toAst(considerPosition: Boolean = false): Statement {
    return if(varDeclaration() != null){

        val type = if(varDeclaration().type() != null) varDeclaration().type().toAst() else expression().toAst().type
        PropertyDeclaration(varDeclaration().ID().text, type, expression().toAst(), mutable = true)
    }else{
        val type = if(valDeclaration().type() != null) valDeclaration().type().toAst() else expression().toAst().type

        PropertyDeclaration(valDeclaration().ID().text, type, expression().toAst(), mutable = false)

    }
}
fun KotlinParser.ExpressionContext.toAst(considerPosition: Boolean = false) : Expression = when (this) {
    is KotlinParser.IntLiteralContext -> IntLit(text, toPosition(considerPosition))
    /*is KotlinParser.BinaryOperationContext -> toAst(considerPosition)
    is KotlinParser.IntLiteralContext -> IntLit(text, toPosition(considerPosition))
    is KotlinParser.DecimalLiteralContext -> DecLit(text, toPosition(considerPosition))
    is KotlinParser.ParenExpressionContext -> expression().toAst(considerPosition)
    is KotlinParser.VarReferenceContext -> VarReference(text, toPosition(considerPosition))
    is KotlinParser.TypeConversionContext -> TypeConversion(expression().toAst(considerPosition), targetType.toAst(considerPosition), toPosition(considerPosition))
    */else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}

fun KotlinParser.TypeContext.toAst(considerPosition: Boolean = false) : Type = when (this) {
    is KotlinParser.IntegerContext -> IntType(toPosition(considerPosition))
   // is KotlinParser.DecimalContext -> DecimalType(toPosition(considerPosition))
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}

fun KotlinParser.BinaryOperationContext.toAst(considerPosition: Boolean = false) : Expression = when (operator.text) {
    "+" -> SumExpression(left.toAst(considerPosition), right.toAst(considerPosition), toPosition(considerPosition))
    "-" -> SubtractionExpression(left.toAst(considerPosition), right.toAst(considerPosition), toPosition(considerPosition))
    "*" -> MultiplicationExpression(left.toAst(considerPosition), right.toAst(considerPosition), toPosition(considerPosition))
    "/" -> DivisionExpression(left.toAst(considerPosition), right.toAst(considerPosition), toPosition(considerPosition))
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}

class KotlinParseTreeToAstMapper : ParseTreeToAstMapper<KotlinParser.KotlinFileContext, KotlinFile> {
    override fun map(parseTreeNode: KotlinParser.KotlinFileContext): KotlinFile = parseTreeNode.toAst()
}