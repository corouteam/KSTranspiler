package it.poliba.KSTranspiler

import it.poliba.KSTranspiler.SwiftParser.BlockContext

fun SwiftParser.SwiftScriptContext.toAst(considerPosition: Boolean = false) : AstScript {
    return AstScript(this.line().map { it.statement().toAst(considerPosition) }, toPosition(considerPosition))
}
fun SwiftParser.SwiftFileContext.toAst(considerPosition: Boolean = false) : AstFile {
    return AstFile(this.declaration().map { it.toAst(considerPosition) }, toPosition(considerPosition))
}

fun SwiftParser.DeclarationContext.toAst(considerPosition: Boolean = false): Declaration {
    return if(this.functionDeclaration()!= null){
        this.functionDeclaration().toAst(considerPosition)
    }else if(this.propertyDeclaration() != null){
        this.propertyDeclaration().toAst(considerPosition)
    } else if(this.structDeclaration() != null){
        this.structDeclaration().toAst(considerPosition)
    }else{
        throw UnsupportedOperationException("")
    }
}

fun SwiftParser.StructDeclarationContext.toAst(considerPosition: Boolean = false): Declaration{
    if(delegationSpecifiers().ID() != null){
        if(delegationSpecifiers().ID().map { it.text }.contains("View")){
            return this.toWidgetAST(considerPosition)
        }else{
            throw UnsupportedOperationException("")
        }
    }else{
        throw UnsupportedOperationException("")
    }

}
fun SwiftParser.FunctionDeclarationContext.toAst(considerPosition: Boolean = false): FunctionDeclaration {
    val id = this.ID().text
    val params = this.functionValueParameters().functionValueParameter().map { it.toAst(considerPosition) }
    var type: Type? = null
    if(this.type() != null){
        type = this.type().toAst(considerPosition)
    }
    var block = Block(listOf(), toPosition(considerPosition))
    if(this.functionBody().block() != null){
        block =  Block(this.functionBody().block().statement().map { it.toAst(considerPosition) }, toPosition(considerPosition))
    }

    return FunctionDeclaration(id, params,type, block, toPosition(considerPosition))
}

fun SwiftParser.FunctionValueParameterContext.toAst(considerPosition: Boolean = false): FunctionParameter {
    return FunctionParameter( this.parameter().ID().text, this.parameter().type().toAst(considerPosition), toPosition(considerPosition))
}
fun SwiftParser.StatementContext.toAst(considerPosition: Boolean = false) : Statement = when (this) {
    is SwiftParser.PropertyDeclarationStatementContext -> this.propertyDeclaration().toAst(considerPosition)
    is SwiftParser.PrintStatementContext -> Print(print().expression().toAst(considerPosition), toPosition(considerPosition))
    is SwiftParser.AssignmentStatementContext -> Assignment(assignment().ID().text, assignment().expression().toAst(considerPosition), toPosition(considerPosition))
    is SwiftParser.ExpressionStatementContext -> expression().toAst(considerPosition)

    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}

fun SwiftParser.PropertyDeclarationContext.toAst(considerPosition: Boolean = false): PropertyDeclaration {
    return if(varDeclaration() != null){

        val type = if(varDeclaration().type() != null) varDeclaration().type().toAst(considerPosition) else expression().toAst(considerPosition).type
        var expression: Expression? = null
        if(expression() != null){
            expression = expression().toAst(considerPosition)
        }
        var body: Block? = null
        if(computedPropertyDeclarationBody()!= null){
            body = Block(computedPropertyDeclarationBody().block().statement().map { it.toAst(considerPosition) }, toPosition(considerPosition))
        }
        PropertyDeclaration(
            varName = varDeclaration().ID().text,
            type = type,
            value = expression,
            mutable = true,
            getter = body,
            position = toPosition(considerPosition))
    }else{
        val type = if(letDeclaration().type() != null) letDeclaration().type().toAst(considerPosition) else expression().toAst(considerPosition).type
        var expression: Expression? = null
        if(expression() != null){
            expression = expression().toAst(considerPosition)
        }
        var body: Block? = null
        if(computedPropertyDeclarationBody()!= null){
            body = Block(computedPropertyDeclarationBody().block().statement().map { it.toAst(considerPosition) }, toPosition(considerPosition))
        }
        PropertyDeclaration(letDeclaration().ID().text, type, expression,
            mutable = false,
            getter = body,
            position = toPosition(considerPosition))

    }
}

fun SwiftParser.ExpressionContext.toAst(considerPosition: Boolean = false) : Expression = when (this) {
    is SwiftParser.IntLiteralContext -> IntLit(text, toPosition(considerPosition))
    is SwiftParser.BoolLiteralContext -> BoolLit(text, toPosition(considerPosition))
    is SwiftParser.CgFloatLiteralContext -> DpLit(INT_LIT().text, toPosition(considerPosition))
    is SwiftParser.StringLiteralExpressionContext -> toAst(considerPosition)
    is SwiftParser.VarReferenceContext -> VarReference(text, type = StringType(toPosition(considerPosition)),  toPosition(considerPosition))
    is SwiftParser.BinaryOperationContext -> toAst(considerPosition)
    is SwiftParser.DoubleLiteralContext-> DoubleLit(text, toPosition(considerPosition))
    is SwiftParser.FunctionCallContext ->toAst(considerPosition)
    is SwiftParser.IfExpressionContext-> toAst(considerPosition)
    is SwiftParser.RangeExpressionContext -> toAst(considerPosition)
    is SwiftParser.ReturnExpressionContext -> ReturnExpression(expression().toAst(considerPosition), toPosition(considerPosition))
    is SwiftParser.WidgetCallExpressionContext -> toAst(considerPosition)
    is SwiftParser.HorizontalAlignmentExpressionContext -> toAst(considerPosition)
    is SwiftParser.VerticalAlignmentExpressionContext -> toAst(considerPosition)
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}

fun SwiftParser.FunctionCallContext.toAst(considerPosition: Boolean): Expression {
    return FunctionCall(
        name = this.name.text,
        parameters = this.functionCallParameters().expression().map { it.toAst(considerPosition) },
        position = toPosition(considerPosition)
    )
}

fun SwiftParser.RangeExpressionContext.toAst(considerPosition: Boolean): Expression {
    return RangeExpression(
        leftExpression = this.left.toAst(considerPosition),
        rightExpression = this.right.toAst(considerPosition),
        type = toRangeType(considerPosition),
        position = toPosition(considerPosition),)
}

fun SwiftParser.RangeExpressionContext.toRangeType(considerPosition: Boolean): Type {
    // TODO don't just check left type
    return RangeType(this.left.toAst(considerPosition).type, toPosition(considerPosition))
}

fun SwiftParser.IfExpressionContext.toAst(considerPosition: Boolean): Expression {
    var elseBody = if(this.if_().elseBody != null) this.if_().elseBody.toAst(considerPosition) else null
    return IfExpression(this.if_().expression().toAst(considerPosition), this.if_().body.toAst(considerPosition), elseBody, toPosition(considerPosition))
}


fun SwiftParser.ControlStructureBodyContext.toAst(considerPosition: Boolean = false): ControlStructureBody {
    if(this.block() != null){
        return this.block().toAst(considerPosition)
    }else if(this.statement() != null){
        return this.statement().toAst(considerPosition)
    }else{
        return Block(listOf(), toPosition(considerPosition))
    }
}

fun BlockContext.toAst(considerPosition: Boolean = false): Block{
    return Block(this.statement().map { it.toAst(considerPosition) }, toPosition(considerPosition))
}

fun SwiftParser.StringLiteralContext.toAst(considerPosition: Boolean): Expression {
    var valueString = ""
    this.lineStringLiteral().lineStringContent().forEach { valueString = valueString + ""+it.LineStrText().text }
    return StringLit(valueString, toPosition(considerPosition))
}

fun SwiftParser.StringLiteralExpressionContext.toAst(considerPosition: Boolean): Expression {
    var valueString = ""
    this.stringLiteral().lineStringLiteral().lineStringContent().forEach { valueString = valueString + ""+it.LineStrText().text }
    return StringLit(valueString, toPosition(considerPosition))
}
fun SwiftParser.TypeContext.toAst(considerPosition: Boolean = false) : Type = when (this) {
    is SwiftParser.IntegerContext -> IntType(toPosition(considerPosition))
    is SwiftParser.DoubleContext -> DoubleType(toPosition(considerPosition))
    is SwiftParser.UserTypeContext -> UserType(ID().text, toPosition(considerPosition))
    is SwiftParser.StringContext -> StringType(toPosition(considerPosition))
    is SwiftParser.CgFloatContext -> DpType(toPosition(considerPosition))
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}

fun SwiftParser.BinaryOperationContext.toAst(considerPosition: Boolean = false) : Expression = when (operator.text) {
    "+" -> SumExpression(left.toAst(considerPosition), right.toAst(considerPosition), toPosition(considerPosition))
    "-" -> SubtractionExpression(left.toAst(considerPosition), right.toAst(considerPosition), toPosition(considerPosition))
    "*" -> MultiplicationExpression(left.toAst(considerPosition), right.toAst(considerPosition), toPosition(considerPosition))
    "/" -> DivisionExpression(left.toAst(considerPosition), right.toAst(considerPosition), toPosition(considerPosition))
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}

class KotlinParseTreeToAstMapperSwift : ParseTreeToAstMapper<SwiftParser.SwiftFileContext, AstFile> {
    override fun map(parseTreeNode: SwiftParser.SwiftFileContext): AstFile = parseTreeNode.toAst()
}
