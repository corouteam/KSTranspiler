package it.poliba.KSTranspiler

import com.strumenta.kolasu.model.Node
import com.strumenta.kolasu.model.Point
import com.strumenta.kolasu.model.Position
import com.strumenta.kolasu.model.pos
import io.ktor.util.*
import it.poliba.KSTranspiler.KotlinParser.AccessSuffixContext
import it.poliba.KSTranspiler.KotlinParser.BlockContext
import it.poliba.KSTranspiler.KotlinParser.ControlStructureBodyContext
import it.poliba.KSTranspiler.KotlinParser.ElvisNavigationContext
import it.poliba.KSTranspiler.KotlinParser.FunctionCallContext
import it.poliba.KSTranspiler.KotlinParser.PropertyDeclarationContext
import it.poliba.KSTranspiler.KotlinParser.PropertyDeclarationStatementContext
import it.poliba.KSTranspiler.KotlinParser.StringLiteralExpressionContext
import org.antlr.v4.codegen.model.decl.Decl
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.Token
import kotlin.jvm.internal.Intrinsics.Kotlin

interface ParseTreeToAstMapper<in PTN : ParserRuleContext, out ASTN : Node> {
    fun map(parseTreeNode: PTN) : ASTN
}

fun KotlinParser.KotlinScriptContext.toAst(considerPosition: Boolean = false) : AstScript {
    return AstScript(this.line().map { it.statement().toAst(considerPosition) }, toPosition(considerPosition))
}
fun KotlinParser.KotlinFileContext.toAst(considerPosition: Boolean = false) : AstFile {
    return AstFile(this.declaration().map { it.toAst(considerPosition) }, toPosition(considerPosition))
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
        annotation = annotation().ID().text
    }
    if(annotation == "Composable"){
        return this.toWidgetAst(considerPosition)
    }else{
        return this.toNormalAst(considerPosition)
    }
}

fun KotlinParser.ClassDeclarationContext.toAst(considerPosition: Boolean = false): Declaration {
    var constProp = listOf<FunctionParameter>()
    var constructor: PrimaryConstructor? = null

    if(primaryConstructor() != null && primaryConstructor().classParameter() != null){
        constProp = primaryConstructor().classParameter().map {
            if(it.VAR() != null || it.VAL() != null){
                //TODO ADD PROPERTY AND ASSIGNMENT
            }
            FunctionParameter(it.ID().text, it.type().toAst(considerPosition))
        }
    }
    var body = arrayListOf<Declaration>()
    if (classBody() != null && classBody().declaration() != null){
        body = ArrayList(classBody().declaration().map { it.toAst(considerPosition) })
    }
    val baseClasses = extendedClasses().type().map { it.toAst(considerPosition) }
    var constructorBody: ControlStructureBody = Block(listOf())
    if(classBody() != null && classBody().constructor() != null && classBody().constructor().isNotEmpty()){
        constructorBody = classBody().constructor().first().functionBody().block().toAst(considerPosition)
    }
    if(constProp.isNotEmpty()){
       constructor = PrimaryConstructor(constProp, constructorBody, toPosition(considerPosition))
    }
    constructor?.let {
        body.add(it)
    }

    return ClassDeclaration(ID().text,  body, baseClasses, toPosition(considerPosition))
}


fun KotlinParser.FunctionDeclarationContext.toNormalAst(considerPosition: Boolean = false): FunctionDeclaration {
    val id = this.ID().text
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
        PropertyDeclaration(varDeclaration().ID().text, type, expression,
            mutable = true,
            position = toPosition(considerPosition))
    }else{
        var expression: Expression? = null

        if(expression() != null){
            expression = expression().toAst(considerPosition)
        }
        val type = if(valDeclaration().type() != null) valDeclaration().type().toAst(considerPosition) else expression?.type ?: throw Exception("Type missing")

        PropertyDeclaration(valDeclaration().ID().text, type, expression,
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
    is KotlinParser.DoubleLiteralContext-> DoubleLit(text, toPosition(considerPosition))
    is KotlinParser.IfExpressionContext-> toAst(considerPosition)
    is KotlinParser.RangeExpressionContext -> toAst(considerPosition)
    is KotlinParser.FunctionCallContext -> toAst(considerPosition)
    is KotlinParser.ListExpressionContext -> toAst(considerPosition)
    is KotlinParser.ReturnExpressionContext -> ReturnExpression(expression().toAst(considerPosition), toPosition(considerPosition))
    is KotlinParser.ComposableCallExpressionContext -> toAst(considerPosition)
    is KotlinParser.ArrangementExpressionContext -> toAst(considerPosition)
    is KotlinParser.HorizhontalAlignmentExpressionContext -> toAst(considerPosition)
    is KotlinParser.VerticalAlignmentExpressionContext -> toAst(considerPosition)
    is KotlinParser.ComplexExpressionContext -> toAst(considerPosition)
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}

fun KotlinParser.ComplexExpressionContext.toAst(considerPosition: Boolean): Expression{
    var base = if(ID() != null){
        VarReference(ID().text, StringType())
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
    is KotlinParser.IntegerContext -> IntType(toPosition(considerPosition))
    is KotlinParser.DoubleContext -> DoubleType(toPosition(considerPosition))
    is KotlinParser.UserTypeContext -> UserType(this.ID().text, toPosition(considerPosition))
    is KotlinParser.StringContext -> StringType(toPosition(considerPosition))
    is KotlinParser.BoolContext -> BoolType(toPosition(considerPosition))
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}

fun KotlinParser.BinaryOperationContext.toAst(considerPosition: Boolean = false) : Expression = when (operator.text) {
    "+" -> SumExpression(left.toAst(considerPosition), right.toAst(considerPosition), toPosition(considerPosition))
    "-" -> SubtractionExpression(left.toAst(considerPosition), right.toAst(considerPosition), toPosition(considerPosition))
    "*" -> MultiplicationExpression(left.toAst(considerPosition), right.toAst(considerPosition), toPosition(considerPosition))
    "/" -> DivisionExpression(left.toAst(considerPosition), right.toAst(considerPosition), toPosition(considerPosition))
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}

class KotlinParseTreeToAstMapper : ParseTreeToAstMapper<KotlinParser.KotlinFileContext, AstFile> {
    override fun map(parseTreeNode: KotlinParser.KotlinFileContext): AstFile = parseTreeNode.toAst()
}