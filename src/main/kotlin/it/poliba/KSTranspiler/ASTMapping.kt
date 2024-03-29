package it.poliba.KSTranspiler

import com.strumenta.kolasu.model.Node
import com.strumenta.kolasu.model.Point
import com.strumenta.kolasu.model.Position
import it.poliba.KSTranspiler.KotlinParser.AccessSuffixContext
import it.poliba.KSTranspiler.KotlinParser.BlockContext
import it.poliba.KSTranspiler.KotlinParser.ControlStructureBodyContext
import it.poliba.KSTranspiler.KotlinParser.ElvisNavigationContext
import it.poliba.KSTranspiler.KotlinParser.PropertyDeclarationContext
import it.poliba.KSTranspiler.KotlinParser.PropertyDeclarationStatementContext
import it.poliba.KSTranspiler.KotlinParser.StringLiteralExpressionContext
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.Token
import java.util.ArrayList

interface ParseTreeToAstMapper<in PTN : ParserRuleContext, out ASTN : Node> {
    fun map(parseTreeNode: PTN) : ASTN
}

fun KotlinParser.KotlinScriptContext.toAst(considerPosition: Boolean = false) : AstScript {
    return AstScript(this.line().map {
    if(it.statement() != null){
        it.statement().toAst(considerPosition)
    }else{
        it.declaration().toAst(considerPosition)
    }
 },  toPosition(considerPosition))
}
fun Token.startPoint() = Point(line, charPositionInLine)

fun Token.endPoint() = Point(line, charPositionInLine + text.length)

fun ParserRuleContext.toPosition(considerPosition: Boolean) : Position? {
    return if (considerPosition) Position(start.startPoint(), stop.endPoint()) else null
}

fun KotlinParser.DeclarationContext.toAst(considerPosition: Boolean = false): Declaration {
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

//TODO: Handle non Composable annotation
fun KotlinParser.FunctionDeclarationContext.toAst(considerPosition: Boolean = false): Declaration {
    var annotation: String = ""
    if(annotation() != null){
        annotation = annotation().identifier().text
    }
    if(annotation == "Composable"){
        return this.toWidgetAst(considerPosition)
    }else{
        return this.toNormalAst(considerPosition)
    }
}
fun KotlinParser.ClassDeclarationContext.toClassAst(considerPosition: Boolean = false): Declaration {
    var constProp = listOf<FunctionParameter>()
    var constructor: PrimaryConstructor? = null
    val defaultConstructorPropertiesParams = arrayListOf<PropertyDeclaration>()
    var classPropertiesNumber = 0

    if(primaryConstructor() != null && primaryConstructor().classParameter() != null){
        constProp = primaryConstructor().classParameter().map {
            if(it.VAR() != null || it.VAL() != null){
                var prop = PropertyDeclaration(it.identifier().text, it.type().toAst(), null, null,it.VAR() != null)
                defaultConstructorPropertiesParams.add(prop)
                classPropertiesNumber += 1
            }

            FunctionParameter(it.identifier().text, it.type().toAst(considerPosition))
        }
    }
    var body = arrayListOf<Declaration>()
    if (classBody() != null && classBody().declaration() != null){
        body.addAll(defaultConstructorPropertiesParams)
        body.addAll(classBody().declaration().map { it.toAst(considerPosition) })

    }
    val baseClasses = extendedClasses().type().map { it.toAst(considerPosition) }
    var constructorBody: ControlStructureBody = Block(listOf())
    if(classBody() != null && classBody().constructor() != null && classBody().constructor().isNotEmpty()){
        constructorBody = classBody().constructor().first().functionBody().block().toAst(considerPosition)
    }
    if(constProp.isNotEmpty()){
        if(defaultConstructorPropertiesParams.isNotEmpty()){
            var body = ArrayList((constructorBody as Block).body)
            body.addAll(defaultConstructorPropertiesParams.map {
                var variable = AccessExpression(VarReference(it.varName, it.type, it.position), ThisExpression(null),  DotOperator(null))
                Assignment(variable, VarReference(it.varName, it.type))
            })
            constructorBody = Block(body.toList())
        }
        constructor = PrimaryConstructor(constProp, constructorBody, toPosition(considerPosition))
    }
    constructor?.let {
        body.add(it)
    }
    return ClassDeclaration(identifier().text,  body, baseClasses, toPosition(considerPosition))

}
fun KotlinParser.ClassDeclarationContext.toAst(considerPosition: Boolean = false): Declaration {
    return if(DATA() != null){
        // data class must have only property parameters
        this.toDataClass(considerPosition)
    }else{
        this.toClassAst(considerPosition)
    }
}

fun KotlinParser.ClassDeclarationContext.toDataClass(considerPosition: Boolean): DataClassDeclaration{
    val bodyDecl = classBody().declaration().map { it.toAst(considerPosition)}
    val baseClasses = extendedClasses().type().map { it.toAst(considerPosition) }
    var properties = listOf<PropertyDeclaration>()
    if(primaryConstructor() != null && primaryConstructor().classParameter().isNotEmpty()){
        if(primaryConstructor().classParameter().firstOrNull { it.VAR() == null && it .VAL() == null } != null){
            throw Error("Data class must have only property parameters", this.toPosition(true))
        }
        properties = primaryConstructor().classParameter().map { PropertyDeclaration(it.identifier().text, it.type().toAst(considerPosition), null, null, it.VAR() != null, toPosition(considerPosition)) }
    }
   return DataClassDeclaration(identifier().text, properties, bodyDecl, baseClasses, toPosition(considerPosition))
}


fun KotlinParser.FunctionDeclarationContext.toNormalAst(considerPosition: Boolean = false): FunctionDeclaration {
    val id = this.identifier().text
    val params = this.functionValueParameters().functionValueParameter().map { it.toAst(considerPosition) }
    var type: Type? = null
    if(this.type() != null){
        type = this.type().toAst(considerPosition)
    }
    var block: Block
    if(this.functionBody().block() != null){
        block = Block(this.functionBody().block().statement().map { it.toAst(considerPosition) }, toPosition(considerPosition))
    }else{
        val returnExpression = ReturnExpression(this.functionBody().expression().toAst(considerPosition), toPosition(considerPosition))
        block = Block(listOf(returnExpression), toPosition(considerPosition))
        if(type == null){
            type = returnExpression.type
        }
    }
    return FunctionDeclaration(id, params,type, block, toPosition(considerPosition))
}

fun KotlinParser.FunctionValueParameterContext.toAst(considerPosition: Boolean = false): FunctionParameter {
    return FunctionParameter( this.parameter().ID().text, this.parameter().type().toAst(considerPosition), toPosition(considerPosition))
}
fun KotlinParser.StatementContext.toAst(considerPosition: Boolean = false) : Statement = when (this) {
    is PropertyDeclarationStatementContext -> this.propertyDeclaration().toAst(considerPosition)
    is KotlinParser.PrintStatementContext -> Print(print().expression().toAst(considerPosition), toPosition(considerPosition))
    is KotlinParser.AssignmentStatementContext -> Assignment(assignment().left.toAst(considerPosition), assignment().right.toAst(considerPosition), toPosition(considerPosition))
    is KotlinParser.ExpressionStatementContext -> expression().toAst(considerPosition)

    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}

fun PropertyDeclarationContext.toAst(considerPosition: Boolean = false): PropertyDeclaration {
    return if(varDeclaration() != null){
        var expression: Expression? = null
        if(expression() != null){
            expression = expression().toAst(considerPosition)
        }
        val type = if(varDeclaration().type() != null) varDeclaration().type().toAst(considerPosition) else  expression?.type ?: throw Exception("Type missing")
        PropertyDeclaration(varDeclaration().identifier().text, type, expression,
            mutable = true,
            position = toPosition(considerPosition))
    }else{
        var expression: Expression? = null

        if(expression() != null){
            expression = expression().toAst(considerPosition)
        }
        val type = if(valDeclaration().type() != null) valDeclaration().type().toAst(considerPosition) else expression?.type ?: throw Exception("Type missing")

        PropertyDeclaration(valDeclaration().identifier().text, type, expression,
            mutable = false,
            position = toPosition(considerPosition))

    }
}

fun KotlinParser.ExpressionContext.toAst(considerPosition: Boolean = false) : Expression = when (this) {
    is KotlinParser.IntLiteralContext -> IntLit(text, toPosition(considerPosition))
    is KotlinParser.BoolLiteralContext -> BoolLit(text, toPosition(considerPosition))
    is KotlinParser.DpLiteralContext -> DpLit(this.INT_LIT().text, toPosition(considerPosition))
    is StringLiteralExpressionContext -> toAst(considerPosition)
    is KotlinParser.VarReferenceContext -> VarReference(text, type = StringType(toPosition(considerPosition)), toPosition(considerPosition))
    is KotlinParser.BinaryOperationContext -> toAst(considerPosition)
    is KotlinParser.LogicalOperationContext -> toAst(considerPosition)
    is KotlinParser.DoubleLiteralContext-> DoubleLit(text, toPosition(considerPosition))
    is KotlinParser.IfExpressionContext-> toAst(considerPosition)
    is KotlinParser.ForExpressionContext-> toAst(considerPosition)
    is KotlinParser.RangeExpressionContext -> toAst(considerPosition)
    is KotlinParser.FunctionCallContext -> toAst(considerPosition)
    is KotlinParser.ListExpressionContext -> toAst(considerPosition)
    is KotlinParser.ReturnExpressionContext -> ReturnExpression(expression().toAst(considerPosition), toPosition(considerPosition))
    is KotlinParser.ComposableCallExpressionContext -> toAst(considerPosition)
    is KotlinParser.ArrangementExpressionContext -> toAst(considerPosition)
    is KotlinParser.HorizhontalAlignmentExpressionContext -> toAst(considerPosition)
    is KotlinParser.VerticalAlignmentExpressionContext -> toAst(considerPosition)
    is KotlinParser.ComplexExpressionContext -> toAst(considerPosition)
    is KotlinParser.ContentScaleExpressionContext -> toAst(considerPosition)
    is KotlinParser.ColorLiteralContext -> this.color().toAst(considerPosition)
    is KotlinParser.FontWeightLiteralContext -> this.fontWeight().toAst(considerPosition)
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}

fun KotlinParser.ComplexExpressionContext.toAst(considerPosition: Boolean): Expression{
    var base = if(identifier() != null){
        VarReference(identifier().text, StringType())
    }else if(functionCallExpression()!= null){
        functionCallExpression().toAst(considerPosition)
    }else if(THIS() != null){
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

fun getAccessSuffix(value: List<AccessSuffixContext>, base: Expression): AccessExpression{
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

fun KotlinParser.NavSuffixContext.toAst(considerPosition: Boolean): AccessOperator{
    return if(this  is ElvisNavigationContext){
        ElvisOperator(toPosition(considerPosition))
    }else{
        DotOperator(toPosition(considerPosition))
    }
}
fun KotlinParser.RangeExpressionContext.toAst(considerPosition: Boolean): Expression {
    return RangeExpression(
        leftExpression = this.left.toAst(considerPosition),
        rightExpression = this.right.toAst(considerPosition),
        type = toRangeType(considerPosition),
        position = toPosition(considerPosition),)
}

fun KotlinParser.FunctionCallContext.toAst(considerPosition: Boolean): Expression {
    return FunctionCall(
        name = this.functionCallExpression().name.text,
        parameters = this.functionCallExpression().functionCallParameters().expression().map { it.toAst(considerPosition) },
        position = toPosition(considerPosition)
    )
}

fun KotlinParser.FunctionCallExpressionContext.toAst(considerPosition: Boolean): Expression {
    return FunctionCall(
        name = this.name.text,
        parameters = this.functionCallParameters().expression().map { it.toAst(considerPosition) },
        position = toPosition(considerPosition)
    )
}

fun KotlinParser.ListExpressionContext.toAst(considerPosition: Boolean): Expression {
    return ListExpression(
        // lists can have just one type, take first of type arguments
        itemsType = this.typeArguments().toAst(considerPosition).first(),
        items = this.expression().map { it.toAst(considerPosition) },
        position = toPosition(considerPosition),
    )
}

fun KotlinParser.TypeArgumentsContext.toAst(considerPosition: Boolean): List<Type> {
    return this.type().map { it.toAst(considerPosition) }
}

fun KotlinParser.RangeExpressionContext.toRangeType(considerPosition: Boolean): Type {
    // TODO don't just check left type
    return RangeType(this.left.toAst(considerPosition).type, toPosition(considerPosition))
}

fun KotlinParser.IfExpressionContext.toAst(considerPosition: Boolean): Expression {
    var elseBody = if(this.if_().elseBody != null) this.if_().elseBody.toAst(considerPosition) else null
    return IfExpression(
        condition = this.if_().expression().toAst(considerPosition),
        body = this.if_().body.toAst(considerPosition),
        elseBranch = elseBody,
        position = toPosition(considerPosition)
    )
}

fun KotlinParser.ForExpressionContext.toAst(considerPosition: Boolean): Expression {
    return ForExpression(this.for_().identifier().text, this.for_().expression().toAst(considerPosition), this.for_().body.toAst(considerPosition))

}
fun ControlStructureBodyContext.toAst(considerPosition: Boolean = false): ControlStructureBody {
    if(this.block() != null){
        return this.block().toAst(considerPosition)
    }else if(this.statement() != null){
        return this.statement().toAst(considerPosition)
    }else{
        return Block(listOf(), toPosition(considerPosition))
    }
}

fun BlockContext.toAst(considerPosition: Boolean = false): Block{
    return Block(statement().map { it.toAst(considerPosition) }, toPosition(considerPosition))
}

fun KotlinParser.StringLiteralContext.toAst(considerPosition: Boolean): Expression {
    var valueString = ""
    this.lineStringLiteral().lineStringContent().forEach { valueString = valueString + ""+it.LineStrText().text }
    return StringLit(valueString, toPosition(considerPosition))
}

fun StringLiteralExpressionContext.toAst(considerPosition: Boolean): Expression {
    var valueString = ""
    this.stringLiteral().lineStringLiteral().lineStringContent().forEach { valueString = valueString + ""+it.LineStrText().text }
    return StringLit(valueString, toPosition(considerPosition))
}
fun KotlinParser.TypeContext.toAst(considerPosition: Boolean = false) : Type = when (this) {
    /*is KotlinParser.FunctionCallContext -> FunctionCallType(toPosition(considerPosition))
    is KotlinParser.RangeExpressionContext -> toRangeType(considerPosition)*/
    is KotlinParser.IntegerContext -> IntType(toPosition(considerPosition))
    is KotlinParser.DoubleContext -> DoubleType(toPosition(considerPosition))
    is KotlinParser.StringContext -> StringType(toPosition(considerPosition))
    is KotlinParser.BoolContext -> BoolType(toPosition(considerPosition))
    is KotlinParser.ContentScaleTypeContext -> AspectRatioType(toPosition(considerPosition))
    is KotlinParser.ColorTypeContext -> ColorType(toPosition(considerPosition))
    is KotlinParser.FontWeightTypeContext -> FontWeightType(toPosition(considerPosition))
    is KotlinParser.UserTypeContext -> UserType(this.identifier().text, toPosition(considerPosition))
    is KotlinParser.DpTypeContext -> DpType(toPosition(considerPosition))
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}

fun KotlinParser.BinaryOperationContext.toAst(considerPosition: Boolean = false) : Expression = when (operator.text) {
    "+" -> SumExpression(left.toAst(considerPosition), right.toAst(considerPosition), toPosition(considerPosition))
    "-" -> SubtractionExpression(left.toAst(considerPosition), right.toAst(considerPosition), toPosition(considerPosition))
    "*" -> MultiplicationExpression(left.toAst(considerPosition), right.toAst(considerPosition), toPosition(considerPosition))
    "/" -> DivisionExpression(left.toAst(considerPosition), right.toAst(considerPosition), toPosition(considerPosition))
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}

fun KotlinParser.LogicalOperationContext.toAst(considerPosition: Boolean = false) : Expression = when (operator.text) {
    "==" -> EqualExpression(left.toAst(considerPosition), right.toAst(considerPosition), toPosition(considerPosition))
    "!=" -> NotEqualExpression(left.toAst(considerPosition), right.toAst(considerPosition), toPosition(considerPosition))
    "&&" -> AndExpression(left.toAst(considerPosition), right.toAst(considerPosition), toPosition(considerPosition))
    "||" -> OrExpression(left.toAst(considerPosition), right.toAst(considerPosition), toPosition(considerPosition))
    "<=" -> LTEqualExpression(left.toAst(considerPosition), right.toAst(considerPosition), toPosition(considerPosition))
    ">=" -> GTEqualExpression(left.toAst(considerPosition), right.toAst(considerPosition), toPosition(considerPosition))
    "<" -> LessThanExpression(left.toAst(considerPosition), right.toAst(considerPosition), toPosition(considerPosition))
    ">" -> GreaterThanExpression(left.toAst(considerPosition), right.toAst(considerPosition), toPosition(considerPosition))
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}

