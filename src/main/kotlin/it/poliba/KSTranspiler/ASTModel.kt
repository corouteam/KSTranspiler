package it.poliba.KSTranspiler

import com.strumenta.kolasu.model.Node
import com.strumenta.kolasu.model.Position


//
// Kotlin specific part
//

data class KSFile(val declarations: List<Declaration>, override var position: Position? = null) : Node()
data class KSScript(val statement: List<Statement>, override var position: Position? = null) : Node()

sealed class Declaration: Statement()

sealed class Statement : ControlStructureBody()

/*
    Improvement: handle expression body differently.
    TODO: error checking
 */
class FunctionDeclaration(val id: String, val parameters: List<FunctionParameter>, val returnType: Type?, val body: ControlStructureBody) : Declaration()

sealed class Expression(open val type: Type) : Statement()

sealed class Type : Node()
class FunctionParameter(val id: String,val  type: Type)
//
// Types
//

data class RangeType(val type: Type, override var position: Position? = null): Type()
data class IntType(override var position: Position? = null) : Type()

data class DoubleType(override var position: Position? = null) : Type()

data class StringType(override var position: Position? = null) : Type()
data class BoolType(override var position: Position? = null) : Type()
data class ColorType(override var position: Position? = null): Type()
data class FontWeightType(override var position: Position? = null): Type()
data class VoidType(override var position: Position? = null) : Type()

data class ListType(val itemsType: Type, override var position: Position? = null) : Type()

// EXPERIMENTAL COMPOSABLE
sealed class ComposableType: Type()
class TextComposableType: ComposableType()
// END TEST
//
// Expressions
//
 class ReturnExpression(val returnExpression: Expression): Expression(returnExpression.type)
sealed class BinaryExpression(open val left: Expression, open val right: Expression) : Expression(type = left.type)

data class SumExpression(override val left: Expression, override val right: Expression, override var position: Position? = null) :
    BinaryExpression(left, right)

data class SubtractionExpression(override val left: Expression, override val right: Expression, override var position: Position? = null) :
    BinaryExpression(left, right)

data class MultiplicationExpression(override val left: Expression, override val right: Expression, override var position: Position? = null) :
    BinaryExpression(left, right)

data class DivisionExpression(override val left: Expression, override val right: Expression, override var position: Position? = null) :
    BinaryExpression(left, right)

data class ListExpression(
    val itemsType: Type,
    val items: List<Expression>,
    override var position: Position? = null
): Expression(ListType(itemsType))

data class UnaryMinusExpression(val value: Expression, override var position: Position? = null) : Expression(value.type)

data class TypeConversion(val value: Expression, val targetType: Type, override var position: Position? = null) :
    Expression(type = targetType)

class VarReference(val varName: String, type: Type, override var position: Position? = null) : Expression(type)

data class IntLit(val value: String, override var position: Position? = null) : Expression(IntType())

data class DoubleLit(val value: String, override var position: Position? = null) : Expression(DoubleType())
data class StringLit(val value: String, override var position: Position? = null) : Expression(StringType())
data class BoolLit(val value: String, override var position: Position? = null) : Expression(BoolType())

data class IfExpression(val condition: Expression, var body: ControlStructureBody, var elseBranch: ControlStructureBody?): Expression(
    IntType()
)

open class ControlStructureBody : Node()

data class Block(val body: List<Statement>): ControlStructureBody()

//
// Statements
//

data class PropertyDeclaration(val varName: String, val type: Type, val value: Expression, var mutable: Boolean, override  var position: Position? = null) :
Declaration()

data class Assignment(val varName: String, val value: Expression, override var position: Position? = null) : Statement()

data class Print(val value: Expression, override var position: Position? = null) : Statement()

data class RangeExpression(val leftExpression: Expression,
                           val rightExpression: Expression,
                           override val type: Type
): Expression(type)

sealed class FunctionCall(type: Type = VoidType()): Expression(type = type)
sealed class ComposableCall(type: ComposableType): FunctionCall(type = type)

class TextComposableCall(
    val value: Expression,
    val color: Expression?,
    val fontWeight: Expression?
): ComposableCall(TextComposableType())
sealed class ColorLit: Expression(ColorType())
class CustomColor(val hex: StringLit): ColorLit()
class ColorBlue: ColorLit()
sealed class FontWeightLit: Expression(FontWeightType())

class CustomFontWeight(val value: IntLit): FontWeightLit()
class FontWeightBold: FontWeightLit()

class WidgetDeclaration(val id: String, val parameters: List<FunctionParameter>, val body: ControlStructureBody): Declaration()