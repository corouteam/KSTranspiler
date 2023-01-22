package it.poliba.KSTranspiler

import com.strumenta.kolasu.model.Node
import com.strumenta.kolasu.model.Point
import com.strumenta.kolasu.model.Position
import it.poliba.KSTranspiler.KotlinParser.ControlStructureBodyContext
import it.poliba.KSTranspiler.KotlinParser.FunctionCallContext
import it.poliba.KSTranspiler.KotlinParser.PropertyDeclarationContext
import it.poliba.KSTranspiler.KotlinParser.PropertyDeclarationStatementContext
import it.poliba.KSTranspiler.KotlinParser.StringLiteralExpressionContext
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.Token

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
        this.functionDeclaration().toAst()
    }else if(this.propertyDeclaration() != null){
        this.propertyDeclaration().toAst()
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
        return this.toWidgetAst()
    }else{
        return this.toNormalAst()
    }
}


fun KotlinParser.FunctionDeclarationContext.toNormalAst(considerPosition: Boolean = false): FunctionDeclaration {
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

fun KotlinParser.FunctionValueParameterContext.toAst(): FunctionParameter {
    return FunctionParameter( this.parameter().ID().text, this.parameter().type().toAst())
}
fun KotlinParser.StatementContext.toAst(considerPosition: Boolean = false) : Statement = when (this) {
    is PropertyDeclarationStatementContext -> this.propertyDeclaration().toAst(considerPosition)
    is KotlinParser.PrintStatementContext -> Print(print().expression().toAst(considerPosition), toPosition(considerPosition))
    is KotlinParser.AssignmentStatementContext -> Assignment(assignment().ID().text, assignment().expression().toAst(considerPosition), toPosition(considerPosition))
    is KotlinParser.ExpressionStatementContext -> expression().toAst()

    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}

fun PropertyDeclarationContext.toAst(considerPosition: Boolean = false): PropertyDeclaration {
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
    is KotlinParser.BoolLiteralContext -> BoolLit(text, toPosition(considerPosition))
    is KotlinParser.DpLiteralContext -> DpLit(this.INT_LIT().text, toPosition(considerPosition))
    is StringLiteralExpressionContext -> toAst(considerPosition)
    is KotlinParser.VarReferenceContext -> VarReference(text, type = StringType(),  toPosition(considerPosition))
    is KotlinParser.BinaryOperationContext -> toAst(considerPosition)
    is KotlinParser.DoubleLiteralContext-> DoubleLit(text, toPosition(considerPosition))
    is KotlinParser.IfExpressionContext-> toAst(considerPosition)
    is KotlinParser.RangeExpressionContext -> toAst(considerPosition)
    is KotlinParser.FunctionCallContext -> toAst(considerPosition)
    is KotlinParser.ListExpressionContext -> toAst(considerPosition)
    is KotlinParser.ReturnExpressionContext -> ReturnExpression(expression().toAst())
    is KotlinParser.ComposableCallExpressionContext -> toAst()
    is KotlinParser.ArrangementExpressionContext -> toAst()
    is KotlinParser.HorizhontalAlignmentExpressionContext -> toAst()

    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}

fun KotlinParser.RangeExpressionContext.toAst(considerPosition: Boolean): Expression {
    return RangeExpression(
        leftExpression = this.left.toAst(),
        rightExpression = this.right.toAst(),
        type = getRangeType(this.left.toAst().type, this.right.toAst().type))
}

fun KotlinParser.FunctionCallContext.toAst(considerPosition: Boolean): Expression {
    return FunctionCall(
        name = this.name.text,
        parameters = this.functionCallParameters().expression().map { it.toAst() }
    )
}

fun KotlinParser.ListExpressionContext.toAst(considerPosition: Boolean): Expression {
    return ListExpression(
        // lists can have just one type, take first of type arguments
        itemsType = this.typeArguments().toAst(considerPosition).first(),
        items = this.expression().map { it.toAst() }
    )
}

fun KotlinParser.TypeArgumentsContext.toAst(considerPosition: Boolean): List<Type> {
    return this.type().map { it.toAst() }
}

fun getRangeType(leftType: Type, rightType: Type): Type {
    // TODO don't just check left type
    return RangeType(leftType)
}

fun KotlinParser.IfExpressionContext.toAst(considerPosition: Boolean): Expression {
    var elseBody = if(this.if_().elseBody != null) this.if_().elseBody.toAst() else null
    return IfExpression(this.if_().expression().toAst(), this.if_().body.toAst(), elseBody )
}


fun ControlStructureBodyContext.toAst(considerPosition: Boolean = false): ControlStructureBody {
    if(this.block() != null){
        return Block(this.block().statement().map { it.toAst(considerPosition) })
    }else if(this.statement() != null){
        return this.statement().toAst()
    }else{
        return Block(listOf())
    }
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
    is KotlinParser.StringContext -> StringType(toPosition(considerPosition))
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
