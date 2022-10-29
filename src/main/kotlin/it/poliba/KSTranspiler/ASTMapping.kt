package it.poliba.KSTranspiler

import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.Token

import it.poliba.KSranspiler.*

interface ParseTreeToAstMapper<in PTN : ParserRuleContext, out ASTN : Node> {
    fun map(parseTreeNode: PTN) : ASTN
}

fun SandyParser.SandyFileContext.toAst(considerPosition: Boolean = false) : SandyFile = SandyFile(this.line().map { it.statement().toAst(considerPosition) }, toPosition(considerPosition))

fun Token.startPoint() = Point(line, charPositionInLine)

fun Token.endPoint() = Point(line, charPositionInLine + text.length)

fun ParserRuleContext.toPosition(considerPosition: Boolean) : Position? {
    return if (considerPosition) Position(start.startPoint(), stop.endPoint()) else null
}

fun SandyParser.StatementContext.toAst(considerPosition: Boolean = false) : Statement = when (this) {
    is SandyParser.VarDeclarationStatementContext -> VarDeclaration(varDeclaration().assignment().ID().text, varDeclaration().assignment().expression().toAst(considerPosition), toPosition(considerPosition))
    is SandyParser.LetDeclarationStatementContext -> ReadOnlyVarDeclaration(letDeclaration().assignment().ID().text, letDeclaration().assignment().expression().toAst(considerPosition), toPosition(considerPosition))
    is SandyParser.AssignmentStatementContext -> Assignment(assignment().ID().text, assignment().expression().toAst(considerPosition), toPosition(considerPosition))
    is SandyParser.PrintStatementContext -> Print(print().expression().toAst(considerPosition), toPosition(considerPosition))
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}

fun SandyParser.ExpressionContext.toAst(considerPosition: Boolean = false) : Expression = when (this) {
    is SandyParser.BinaryOperationContext -> toAst(considerPosition)
    is SandyParser.IntLiteralContext -> IntLit(text, toPosition(considerPosition))
    is SandyParser.DecimalLiteralContext -> DecLit(text, toPosition(considerPosition))
    is SandyParser.ParenExpressionContext -> expression().toAst(considerPosition)
    is SandyParser.VarReferenceContext -> VarReference(text, toPosition(considerPosition))
    is SandyParser.TypeConversionContext -> TypeConversion(expression().toAst(considerPosition), targetType.toAst(considerPosition), toPosition(considerPosition))
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}

fun SandyParser.TypeContext.toAst(considerPosition: Boolean = false) : Type = when (this) {
    is SandyParser.IntegerContext -> IntType(toPosition(considerPosition))
    is SandyParser.DecimalContext -> DecimalType(toPosition(considerPosition))
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}

fun SandyParser.BinaryOperationContext.toAst(considerPosition: Boolean = false) : Expression = when (operator.text) {
    "+" -> SumExpression(left.toAst(considerPosition), right.toAst(considerPosition), toPosition(considerPosition))
    "-" -> SubtractionExpression(left.toAst(considerPosition), right.toAst(considerPosition), toPosition(considerPosition))
    "*" -> MultiplicationExpression(left.toAst(considerPosition), right.toAst(considerPosition), toPosition(considerPosition))
    "/" -> DivisionExpression(left.toAst(considerPosition), right.toAst(considerPosition), toPosition(considerPosition))
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}

class SandyParseTreeToAstMapper : ParseTreeToAstMapper<SandyParser.SandyFileContext, SandyFile> {
    override fun map(parseTreeNode: SandyParser.SandyFileContext): SandyFile = parseTreeNode.toAst()
}