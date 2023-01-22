package it.poliba.KSTranspiler

fun SwiftParser.SwiftScriptContext.toAst(considerPosition: Boolean = false) : AstScript {
    return AstScript(this.line().map { it.statement().toAst(considerPosition) }, toPosition(considerPosition))
}
fun SwiftParser.SwiftFileContext.toAst(considerPosition: Boolean = false) : AstFile {
    return AstFile(this.declaration().map { it.toAst(considerPosition) }, toPosition(considerPosition))
}

fun SwiftParser.DeclarationContext.toAst(considerPosition: Boolean = false): Declaration {
    return if(this.functionDeclaration()!= null){
        this.functionDeclaration().toAst()
    }else if(this.propertyDeclaration() != null){
        this.propertyDeclaration().toAst()
    } else if(this.structDeclaration() != null){
        this.structDeclaration().toAst()
    }else{
        throw UnsupportedOperationException("")
    }
}

fun SwiftParser.StructDeclarationContext.toAst(): Declaration{
    if(delegationSpecifiers().ID() != null){
        if(delegationSpecifiers().ID().map { it.text }.contains("View")){
            return this.toWidgetAST()
        }else{
            throw UnsupportedOperationException("")
        }
    }else{
        throw UnsupportedOperationException("")
    }

}
fun SwiftParser.FunctionDeclarationContext.toAst(considerPosition: Boolean = false): FunctionDeclaration {
    val id = this.ID().text
    val params = this.functionValueParameters().functionValueParameter().map { it.toAst() }
    var type: Type? = null
    if(this.type() != null){
        type = this.type().toAst()
    }
    var block = Block(listOf())
    if(this.functionBody().block() != null){
        block =  Block(this.functionBody().block().statement().map { it.toAst(considerPosition) })
    }else{
        val returnExpression = ReturnExpression(this.functionBody().expression().toAst())
        block = Block(listOf(returnExpression))
        if(type == null){
            type = returnExpression.type
        }
    }
    return FunctionDeclaration(id, params,type, block)
}

fun SwiftParser.FunctionValueParameterContext.toAst(): FunctionParameter {
    return FunctionParameter( this.parameter().ID().text, this.parameter().type().toAst())
}
fun SwiftParser.StatementContext.toAst(considerPosition: Boolean = false) : Statement = when (this) {
    is SwiftParser.PropertyDeclarationStatementContext -> this.propertyDeclaration().toAst(considerPosition)
    is SwiftParser.PrintStatementContext -> Print(print().expression().toAst(considerPosition), toPosition(considerPosition))
    is SwiftParser.AssignmentStatementContext -> Assignment(assignment().ID().text, assignment().expression().toAst(considerPosition), toPosition(considerPosition))
    is SwiftParser.ExpressionStatementContext -> expression().toAst()

    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}

fun SwiftParser.PropertyDeclarationContext.toAst(considerPosition: Boolean = false): PropertyDeclaration {
    return if(varDeclaration() != null){

        val type = if(varDeclaration().type() != null) varDeclaration().type().toAst() else expression().toAst().type
        var expression: Expression? = null
        if(expression() != null){
            expression = expression().toAst()
        }
        var body: Block? = null
        if(computedPropertyDeclarationBody()!= null){
            body = Block(computedPropertyDeclarationBody().block().statement().map { it.toAst() })
        }
        PropertyDeclaration(varDeclaration().ID().text, type, expression, mutable = true, getter = body)
    }else{
        val type = if(letDeclaration().type() != null) letDeclaration().type().toAst() else expression().toAst().type
        var expression: Expression? = null
        if(expression() != null){
            expression = expression().toAst()
        }
        var body: Block? = null
        if(computedPropertyDeclarationBody()!= null){
            body = Block(computedPropertyDeclarationBody().block().statement().map { it.toAst() })
        }
        PropertyDeclaration(letDeclaration().ID().text, type, expression, mutable = false, getter = body)

    }
}

fun SwiftParser.ExpressionContext.toAst(considerPosition: Boolean = false) : Expression = when (this) {
    is SwiftParser.IntLiteralContext -> IntLit(text, toPosition(considerPosition))
    is SwiftParser.BoolLiteralContext -> BoolLit(text, toPosition(considerPosition))
    is SwiftParser.CgFloatLiteralContext -> DpLit(INT_LIT().text, toPosition(considerPosition))
    is SwiftParser.StringLiteralExpressionContext -> toAst(considerPosition)
    is SwiftParser.VarReferenceContext -> VarReference(text, type = StringType(),  toPosition(considerPosition))
    is SwiftParser.BinaryOperationContext -> toAst(considerPosition)
    is SwiftParser.DoubleLiteralContext-> DoubleLit(text, toPosition(considerPosition))
    is SwiftParser.IfExpressionContext-> toAst(considerPosition)
    is SwiftParser.ReturnExpressionContext -> ReturnExpression(expression().toAst())
    is SwiftParser.WidgetCallExpressionContext -> toAst()
    is SwiftParser.HorizontalAlignmentExpressionContext -> toAst()
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}

fun SwiftParser.IfExpressionContext.toAst(considerPosition: Boolean): Expression {
    var elseBody = if(this.if_().elseBody != null) this.if_().elseBody.toAst() else null
    return IfExpression(this.if_().expression().toAst(), this.if_().body.toAst(), elseBody )
}


fun SwiftParser.ControlStructureBodyContext.toAst(considerPosition: Boolean = false): ControlStructureBody {
    if(this.block() != null){
        return Block(this.block().statement().map { it.toAst(considerPosition) })
    }else if(this.statement() != null){
        return this.statement().toAst()
    }else{
        return Block(listOf())
    }
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
    is SwiftParser.UserTypeContext -> UserType(ID().text)
    is SwiftParser.StringContext -> StringType()
    is SwiftParser.CgFloatContext -> DpType()
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
