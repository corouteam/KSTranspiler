package it.poliba.KSTranspiler

import com.strumenta.kolasu.model.Node
import com.strumenta.kolasu.model.Position
import com.strumenta.kolasu.model.pos
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible


//
// Kotlin specific part
//

data class AstFile(val declarations: List<Declaration>, override var position: Position? = null) : Node()
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

sealed class Expression(open val type: Type) : Statement()

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


data class VoidType(override var position: Position? = null) : Type()
data class UserType(var name: String, override var position: Position? = null): Type()
data class DpType(override var position: Position? = null): Type()
data class ListType(val itemsType: Type, override var position: Position? = null) : Type()


// EXPERIMENTAL COMPOSABLE
sealed class ComposableType: Type()
class TextComposableType(override var position: Position? = null): ComposableType()
class SpacerComposableType: ComposableType()
class DividerComposableType(override var position: Position? = null): ComposableType()
class SpacerComposableType(override var position: Position? = null): ComposableType()
class ColumnComposableType(override var position: Position? = null): ComposableType()

class HorizontalAlignmentType(override var position: Position? = null): Type()
class VerticalAlignmentType(override var position: Position? = null): Type()
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
    val varName: String,
    val value: Expression,
    override var position: Position? = null
) : Statement()

data class Print(val value: Expression, override var position: Position? = null) : Statement()

data class RangeExpression(val leftExpression: Expression,
                           val rightExpression: Expression,
                           override val type: Type,
                           override var position: Position? = null,
): Expression(type)

data class FunctionCall(val name: String,
                        val parameters: List<Expression>,
                        override var position: Position? = null,
): Expression(FunctionCallType(position))

sealed class ComposableCall(type: ComposableType): Expression(type)

class TextComposableCall(
    val value: Expression,
    val color: Expression?,
    val fontWeight: Expression?,
    override var position: Position? = null,
): ComposableCall(TextComposableType(position))

sealed class ColorLit(): Expression(ColorType())
class CustomColor(val hex: StringLit, override var position: Position? = null): ColorLit()
class ColorBlue(override var position: Position? = null): ColorLit()
sealed class FontWeightLit(): Expression(FontWeightType())

class CustomFontWeight(val value: IntLit, override var position: Position? = null): FontWeightLit()
class FontWeightBold(override var position: Position? = null): FontWeightLit()

class DividerComposableCall(val frame: Frame?,
                            override var position: Position? = null
): ComposableCall(DividerComposableType(position))

class SpacerComposableCall(val size: Frame?,
                           override var position: Position? = null
): ComposableCall(SpacerComposableType(position))

class DividerComposableCall(val frame: Frame?): ComposableCall(DividerComposableType())

class SpacerComposableCall(val size: Frame?): ComposableCall(SpacerComposableType())

class Size(val width: Expression, val height: Expression): Expression(FrameType())

class Frame(val width: Expression, val height: Expression, override var position: Position? = null): Node()

data class ColumnComposableCall(
    val spacing: Expression?,
    val horizontalAlignment: Expression?,
    val scrollable: Boolean,
    val body: ControlStructureBody,
    override var position: Position? = null,
): ComposableCall(ColumnComposableType(position))

data class RowComposableCall(
    val spacing: Expression?,
    val verticalAlignment: Expression?,
    val scrollable: Boolean,
    val body: ControlStructureBody,
    override var position: Position? = null
): ComposableCall(ColumnComposableType(position))

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

data class Error(override val message: String, val position: Position?): Throwable(message)

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