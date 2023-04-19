package it.poliba.KSTranspiler

import it.poliba.KSTranspiler.SwiftParser.BlockContext

fun SwiftParser.SwiftScriptContext.toAst(considerPosition: Boolean = false) : AstScript {
    return AstScript(this.line().map {
        if(it.statement() != null){
            it.statement().toAst(considerPosition)
        }else{
            it.declaration().toAst(considerPosition)
        }
    },  toPosition(considerPosition))
}

fun SwiftParser.DeclarationContext.toAst(considerPosition: Boolean = false): Declaration {
    return if(this.functionDeclaration()!= null){
        this.functionDeclaration().toAst(considerPosition)
    }else if(this.propertyDeclaration() != null){
        this.propertyDeclaration().toAst(considerPosition)
    }else if(this.classDeclaration() != null){
        this.classDeclaration().toAst(considerPosition)
    }else{
        throw UnsupportedOperationException("")
    }
}


fun SwiftParser.ClassDeclarationContext.toAst(considerPosition: Boolean = false): Declaration{
    var name = ID().text
    var baseClasses = delegationSpecifiers().type().map { it.toAst(considerPosition) }
    var body = classBody().classMemberDeclaration().map { it.toAst(considerPosition) as? Declaration}.filterNotNull()
    if(CLASS() != null){
        return ClassDeclaration(name,body, baseClasses)
    }else{
        if(delegationSpecifiers().type().map { it.toAst(false) }.contains(UserType("View"))){
            return this.toWidgetAST(considerPosition)
        }else{
            var parameters = body.filter { it is PropertyDeclaration }.map { it as PropertyDeclaration }
            var bodyWithoutParams = body.filterNot { it is PropertyDeclaration }
            return DataClassDeclaration(name, parameters, bodyWithoutParams, baseClasses)
        }
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
    is SwiftParser.AssignmentStatementContext -> Assignment(assignment().left.toAst(considerPosition), assignment().right.toAst(considerPosition), toPosition(considerPosition))
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
    is SwiftParser.ContentModeExpressionContext -> contentMode().toAst(considerPosition)
    is SwiftParser.ComplexExpressionContext -> toAst(considerPosition)
    is SwiftParser.ColorLiteralContext -> color().toAst(considerPosition)
    is SwiftParser.FontWeightLiteralContext -> toAst(considerPosition)
    is SwiftParser.ForExpressionContext -> toAst(considerPosition)
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}

fun SwiftParser.ForExpressionContext.toAst(considerPosition: Boolean): Expression {
    return ForExpression(this.for_().ID().text, this.for_().expression().toAst(considerPosition), this.for_().body.toAst(considerPosition))
}
fun SwiftParser.ComplexExpressionContext.toAst(considerPosition: Boolean): Expression{
    var base = if(ID() != null){
        VarReference(ID().text, StringType())
    }else if(functionCallExpression()!= null){
        functionCallExpression().toAst(considerPosition)
    }else if(SELF() != null){
        ThisExpression(toPosition(considerPosition))
    }else{
        throw Exception("Not recognized id at ${toPosition(considerPosition)}")
    }

    if(accessSuffix().isNotEmpty()){
        return getAccessSuffix(accessSuffix(), base)
    }else{
        return base
    }

}

fun SwiftParser.FontWeightLiteralContext.toAst(considerPosition: Boolean): Expression{
    return when(fontWeight()){
        is SwiftParser.BlackContext -> FontWeightBlack(toPosition(considerPosition))
        is SwiftParser.HeavyContext -> FontWeightExtraBold(toPosition(considerPosition))
        is SwiftParser.BoldContext -> FontWeightBold(toPosition(considerPosition))
        is SwiftParser.SemiboldContext -> FontWeightSemiBold(toPosition(considerPosition))
        is SwiftParser.MediumContext -> FontWeightMedium(toPosition(considerPosition))
        is SwiftParser.RegularContext -> FontWeightNormal(toPosition(considerPosition))
        is SwiftParser.LightContext -> FontWeightLight(toPosition(considerPosition))
        is SwiftParser.ThinContext -> FontWeightExtraLight(toPosition(considerPosition))
        is SwiftParser.UltralightContext -> FontWeightThin(toPosition(considerPosition))
        else -> throw Exception("FontWeight not recognized")
    }
}


fun getAccessSuffix(value: List<SwiftParser.AccessSuffixContext>, base: Expression): AccessExpression{
    if(value.size == 1){
        var access = value.last().navSuffix().toAst(true)
        var exp = value.last().expression().toAst(true)
        return AccessExpression( exp, base, access)
    }else{
        var access = value.last().navSuffix().toAst(true)
        var exp = value.last().expression().toAst(true)
        return AccessExpression( exp, getAccessSuffix(value.dropLast(1), base), access)
    }
}

fun SwiftParser.NavSuffixContext.toAst(considerPosition: Boolean): AccessOperator{
    return if(this  is SwiftParser.ElvisNavigationContext){
        ElvisOperator(toPosition(considerPosition))
    }else{
        DotOperator(toPosition(considerPosition))
    }
}
fun SwiftParser.FunctionCallExpressionContext.toAst(considerPosition: Boolean): Expression {
    return FunctionCall(
        name = this.name.text,
        parameters = this.functionCallParameters().expression().map { it.toAst(considerPosition) },
        position = toPosition(considerPosition)
    )
}
fun SwiftParser.FunctionCallContext.toAst(considerPosition: Boolean): Expression {
    return FunctionCall(
        name = this.functionCallExpression().name.text,
        parameters = this.functionCallExpression().functionCallParameters().expression().map { it.toAst(considerPosition) },
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
    /*is SwiftParser.FunctionCallContext -> FunctionCallType(toPosition(considerPosition))
    is SwiftParser.RangeExpressionContext -> toRangeType(considerPosition)*/
    is SwiftParser.IntegerContext -> IntType(toPosition(considerPosition))
    is SwiftParser.DoubleContext -> DoubleType(toPosition(considerPosition))
    is SwiftParser.StringContext -> StringType(toPosition(considerPosition))
    is SwiftParser.BoolContext -> BoolType(toPosition(considerPosition))
    is SwiftParser.ContentModeTypeContext -> AspectRatioType(toPosition(considerPosition))
    is SwiftParser.ColorTypeContext -> ColorType(toPosition(considerPosition))
    is SwiftParser.FontWeightTypeContext -> FontWeightType(toPosition(considerPosition))
    is SwiftParser.UserTypeContext -> UserType(ID().text, toPosition(considerPosition))
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

