package it.poliba.KSranspiler

import com.strumenta.kolasu.model.Node
import com.strumenta.kolasu.model.Position
import com.sun.jdi.BooleanType
import java.util.*
import kotlin.reflect.KParameter
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor


//
// Kotlin specific part
//

data class KotlinFile(val statements : List<Statement>, override var position: Position? = null) : Node()

sealed class Statement : ControlStructureBody() { }

sealed class Expression(open val type: Type) : Statement() { }

sealed class Type : Node() { }

//
// Types
//

data class IntType(override var position: Position? = null) : Type()

data class DoubleType(override var position: Position? = null) : Type()

data class StringType(override var position: Position? = null) : Type()
data class BoolType(override var position: Position? = null) : Type()

//
// Expressions
//

sealed class BinaryExpression(open val left: Expression, open val right: Expression) : Expression(type = left.type)

data class SumExpression(override val left: Expression, override val right: Expression, override var position: Position? = null) :
    BinaryExpression(left, right)

data class SubtractionExpression(override val left: Expression, override val right: Expression, override var position: Position? = null) :
    BinaryExpression(left, right)

data class MultiplicationExpression(override val left: Expression, override val right: Expression, override var position: Position? = null) :
    BinaryExpression(left, right)

data class DivisionExpression(override val left: Expression, override val right: Expression, override var position: Position? = null) :
    BinaryExpression(left, right)

data class UnaryMinusExpression(val value: Expression, override var position: Position? = null) : Expression(value.type)

data class TypeConversion(val value: Expression, val targetType: Type, override var position: Position? = null) :
    Expression(type = targetType)

data class VarReference(val varName: String, override var type: Type, override var position: Position? = null) : Expression(type)

data class IntLit(val value: String, override var position: Position? = null) : Expression(IntType())

data class DoubleLit(val value: String, override var position: Position? = null) : Expression(DoubleType())
data class StringLit(val value: String, override var position: Position? = null) : Expression(StringType())
data class BoolLit(val value: String, override var position: Position? = null) : Expression(BoolType())

data class IfExpression(val condition: Expression, var body: ControlStructureBody, var elseBranch: ControlStructureBody?): Expression(IntType())

open class ControlStructureBody(): Node()

data class Block(val body: List<Statement>): ControlStructureBody()

//
// Statements
//

data class PropertyDeclaration(val varName: String, val type: Type, val value: Expression, var mutable: Boolean,  override  var position: Position? = null) :
Statement()

data class Assignment(val varName: String, val value: Expression, override var position: Position? = null) : Statement()

data class Print(val value: Expression, override var position: Position? = null) : Statement()