package it.poliba.KSTranspiler

import com.strumenta.kolasu.model.Node
import com.strumenta.kolasu.model.Position
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

//
// Kotlin specific part
//

data class AstScript(val statement: List<Statement>, override var position: Position? = null) : Node()

sealed class Declaration: Statement()

sealed class Statement : ControlStructureBody()

/*
    Improvement: handle expression body differently.
    TODO: error checking
 */
class FunctionDeclaration(val id: String,
                          val parameters: List<FunctionParameter>,
                          val returnType: Type?,
                          val body: ControlStructureBody,
                          override var position: Position? = null) : Declaration()

sealed class Expression(open var type: Type) : Statement()

class ClassDeclaration(
    val name: String,
    val body: List<Declaration>,
    val baseClasses: List<Type>,
    override var position: Position? = null,
): Declaration()

class DataClassDeclaration(
    val name: String,
    val propertyList: List<PropertyDeclaration>,
    val body: List<Declaration>,
    val baseClasses: List<Type>,
    override var position: Position? = null,
): Declaration()

sealed class Type : Node()

class FunctionParameter(val id: String,val  type: Type, override var position: Position? = null): Node()
//
// Types
//

data class FunctionCallType(override var position: Position? = null): Type()
data class RangeType(val type: Type, override var position: Position? = null): Type()
data class IntType(override var position: Position? = null) : Type()
data class DoubleType(override var position: Position? = null) : Type()
data class StringType(override var position: Position? = null) : Type()
data class BoolType(override var position: Position? = null) : Type()
data class ColorType(override var position: Position? = null): Type()
data class FontWeightType(override var position: Position? = null): Type()
data class ResizableType(override var position: Position? = null): Type()
data class AspectRatioType(override var position: Position? = null): Type()
data class VoidType(override var position: Position? = null) : Type()
data class UserType(var name: String, override var position: Position? = null): Type()
data class DpType(override var position: Position? = null): Type()
data class ListType(val itemsType: Type, override var position: Position? = null) : Type()
data class ArrayType(val itemsType: Type, override var position: Position? = null) : Type()
// EXPERIMENTAL COMPOSABLE
sealed class ComposableType: Type()
class ButtonComposableType(override var position: Position? = null): ComposableType()
class TextComposableType(override var position: Position? = null): ComposableType()
class DividerComposableType(override var position: Position? = null): ComposableType()
class SpacerComposableType(override var position: Position? = null): ComposableType()
class ColumnComposableType(override var position: Position? = null): ComposableType()
class ZStackComposableType(override var position: Position? = null): ComposableType()
class HStackComposableType(override var position: Position? = null): ComposableType()
class BoxComposableType(override var position: Position? = null): ComposableType()
class HorizontalAlignmentType(override var position: Position? = null): Type()
class VerticalAlignmentType(override var position: Position? = null): Type()

class ImageComposableType: ComposableType()
// END TEST
//
// Expressions
//
class ReturnExpression(val returnExpression: Expression,
                       override var position: Position? = null
): Expression(returnExpression.type)

sealed class BinaryExpression(open val left: Expression,
                              open val right: Expression,
): Expression(type = left.type)
sealed class LogicalExpression(open val left: Expression,
                               open val right: Expression
) : Expression(type = BoolType())

data class SumExpression(
    override val left: Expression,
    override val right: Expression,
    override var position: Position? = null
): BinaryExpression(left, right)

data class SubtractionExpression(
    override val left: Expression,
    override val right: Expression,
    override var position: Position? = null
): BinaryExpression(left, right)

data class MultiplicationExpression(
    override val left: Expression,
    override val right: Expression,
    override var position: Position? = null
): BinaryExpression(left, right)

data class DivisionExpression(
    override val left: Expression,
    override val right: Expression,
    override var position: Position? = null
): BinaryExpression(left, right)

data class EqualExpression(override val left: Expression, override val right: Expression, override var position: Position? = null) :
    LogicalExpression(left, right)
data class NotEqualExpression(override val left: Expression, override val right: Expression, override var position: Position? = null) :
    LogicalExpression(left, right)
data class GTEqualExpression(override val left: Expression, override val right: Expression, override var position: Position? = null) :
    LogicalExpression(left, right)
data class LTEqualExpression(override val left: Expression, override val right: Expression, override var position: Position? = null) :
    LogicalExpression(left, right)
data class GreaterThanExpression(override val left: Expression, override val right: Expression, override var position: Position? = null) :
    LogicalExpression(left, right)
data class LessThanExpression(override val left: Expression, override val right: Expression, override var position: Position? = null) :
    LogicalExpression(left, right)
data class AndExpression(override val left: Expression, override val right: Expression, override var position: Position? = null) :
    LogicalExpression(left, right)
data class OrExpression(override val left: Expression, override val right: Expression, override var position: Position? = null) :
    LogicalExpression(left, right)


data class ListExpression(
    val itemsType: Type,
    val items: List<Expression>,
    override var position: Position? = null
): Expression(ListType(itemsType, position))

data class UnaryMinusExpression(
    val value: Expression,
    override var position: Position? = null
) : Expression(value.type)

data class TypeConversion(
    val value: Expression,
    val targetType: Type,
    override var position: Position? = null
): Expression(type = targetType)

class VarReference(
    val varName: String,
    type: Type,
    override var position: Position? = null
) : Expression(type)

data class IntLit(val value: String, override var position: Position? = null) : Expression(IntType(position))
data class DoubleLit(val value: String, override var position: Position? = null) : Expression(DoubleType(position))
data class StringLit(val value: String, override var position: Position? = null) : Expression(StringType(position))
data class BoolLit(val value: String, override var position: Position? = null) : Expression(BoolType(position))

data class DpLit(val value: String, override var position: Position? = null) : Expression(DpType(position))
data class IfExpression(
    val condition: Expression,
    var body: ControlStructureBody,
    var elseBranch: ControlStructureBody?,
    override var position: Position? = null
): Expression(IntType(position))

data class ForExpression(val varName: String, val range: Expression, var body: ControlStructureBody): Expression(IntType())

open class ControlStructureBody : Node()

data class Block(val body: List<Statement>, override var position: Position? = null): ControlStructureBody()

//
// Statements
//

data class PropertyDeclaration(
    val varName: String,
    val type: Type,
    val value: Expression?,
    var getter: Block? = null,
    var mutable: Boolean,
    override var position: Position? = null,
) : Declaration()

data class Assignment(
    val variable: Expression,
    val value: Expression,
    override var position: Position? = null
) : Statement()

data class Print(val value: Expression, override var position: Position? = null) : Statement()

data class RangeExpression(val leftExpression: Expression,
                           val rightExpression: Expression,
                           override var type: Type,
                           override var position: Position? = null,
): Expression(type)

data class FunctionCall(val name: String,
                        val parameters: List<Expression>,
                        override var position: Position? = null,
): Expression(FunctionCallType(position)) // FunctionCallType not right

class PrimaryConstructor(
    val parameters: List<FunctionParameter>,
    val body: ControlStructureBody,
    override var position: Position?
): Declaration()
sealed class ComposableCall(
    open var zIndex: Expression?,
    type: ComposableType): Expression(type)

class TextComposableCall(
    val value: Expression,
    val color: Expression?,
    val fontWeight: Expression?,
    zIndex: Expression? = null,
    override var position: Position? = null,
): ComposableCall( zIndex, TextComposableType(position))

sealed class ColorLit(): Expression(ColorType())
class CustomColor(val hex: StringLit, override var position: Position? = null): ColorLit()
class ColorBlack(override var position: Position? = null): ColorLit()
class ColorBlue(override var position: Position? = null): ColorLit()
class ColorCyan(override var position: Position? = null): ColorLit()
class ColorGray(override var position: Position? = null): ColorLit()
class ColorGreen(override var position: Position? = null): ColorLit()
class ColorRed(override var position: Position? = null): ColorLit()
class ColorWhite(override var position: Position? = null): ColorLit()
class ColorYellow(override var position: Position? = null): ColorLit()
sealed class FontWeightLit(): Expression(FontWeightType())

class CustomFontWeight(val value: Expression, override var position: Position? = null): FontWeightLit()
class FontWeightBlack(override var position: Position? = null): FontWeightLit()
class FontWeightExtraBold(override var position: Position? = null): FontWeightLit()
class FontWeightBold(override var position: Position? = null): FontWeightLit()
class FontWeightSemiBold(override var position: Position? = null): FontWeightLit()
class FontWeightMedium(override var position: Position? = null): FontWeightLit()
class FontWeightNormal(override var position: Position? = null): FontWeightLit()
class FontWeightLight(override var position: Position? = null): FontWeightLit()
class FontWeightExtraLight(override var position: Position? = null): FontWeightLit()
class FontWeightThin(override var position: Position? = null): FontWeightLit()

class DividerComposableCall(
    val frame: Frame?,
    val color: Expression?,
    zIndex: Expression? = null,
    override var position: Position? = null): ComposableCall(zIndex, DividerComposableType(position))

class SpacerComposableCall(
    val size: Frame?,
    zIndex: Expression? = null,
    override var position: Position? = null,
    ): ComposableCall(zIndex, SpacerComposableType(position))

class ZStackComposableCall(
    val body: ControlStructureBody,
    zIndex: Expression? = null,
    override var position: Position? = null,
): ComposableCall(zIndex, ZStackComposableType(position))


class Frame(val width: Expression?, val height: Expression?, override var position: Position? = null): Node()

data class ColumnComposableCall(
    val spacing: Expression?,
    val horizontalAlignment: Expression?,
    val scrollable: Boolean,
    val body: ControlStructureBody,
    override var zIndex: Expression? = null,
    override var position: Position? = null,
): ComposableCall(zIndex, ColumnComposableType(position))

data class RowComposableCall(
    val spacing: Expression?,
    val verticalAlignment: Expression?,
    val scrollable: Boolean,
    val body: ControlStructureBody,
    override var zIndex: Expression? = null,
    override var position: Position? = null
): ComposableCall(zIndex, ColumnComposableType(position))

sealed class HorizontalAlignment: Expression(HorizontalAlignmentType())
sealed class VerticalAlignment: Expression(VerticalAlignmentType())

data class StartAlignment(override var position: Position? = null): HorizontalAlignment()
data class EndAlignment(override var position: Position? = null): HorizontalAlignment()
data class CenterHorizAlignment(override var position: Position? = null): HorizontalAlignment()
data class TopAlignment(override var position: Position? = null): VerticalAlignment()
data class BottomAlignment(override var position: Position? = null): VerticalAlignment()
data class CenterVerticallyAlignment(override var position: Position? = null): VerticalAlignment()

class WidgetDeclaration(val id: String,
                        val parameters: List<FunctionParameter>,
                        val body: ControlStructureBody,
                        override var position: Position? = null,
): Declaration()

class ButtonComposableCall(
    val action: Block,
    val body: Block,
    zIndex: Expression? = null,
    override var position: Position? = null,
    ): ComposableCall(zIndex, ButtonComposableType())
data class Error(override val message: String, val position: Position?): Throwable(message)

class AccessExpression(val child: Expression,val prefix: Expression,val accessOperator: AccessOperator): Expression(StringType())

sealed class AccessOperator(position: Position?)

class DotOperator(position: Position?): AccessOperator(position)

class ElvisOperator(position: Position?): AccessOperator(position)

class ThisExpression(position: Position?): Expression(StringType())






class ImageComposableCall(
    val value: Expression,
    val resizable: Boolean,
    val aspectRatio: Expression?,
    zIndex: Expression? = null,
    override var position: Position? = null,
): ComposableCall(zIndex, ImageComposableType())

sealed class ResizableLit: Expression(ResizableType())

sealed class AspectRatioLit: Expression(AspectRatioType())
class ContentFit: AspectRatioLit()
class ContentFill: AspectRatioLit()

/**
 * Define a function for each node;
 * Reflection will automatically add process to all
 * properties of this class
 */
fun Node.process(operation: (Node) -> Unit) {
    operation(this)
    this.javaClass.kotlin.memberProperties.forEach { p ->
        p.isAccessible = true
        val v = p.get(this)
        when (v) {
            is Node -> v.process(operation)
            is Collection<*> -> v.forEach { if (it is Node) it.process(operation) }
        }
    }
}

/**
 * Run processing on just a class
 */
fun <T: Node> Node.specificProcess(klass: Class<T>, operation: (T) -> Unit) {
    process { if (klass.isInstance(it)) { operation(it as T) } }
}