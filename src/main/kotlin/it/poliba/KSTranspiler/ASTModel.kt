package it.poliba.KSranspiler

import com.strumenta.kolasu.model.Node
import com.strumenta.kolasu.model.Position
import java.util.*
import kotlin.reflect.KParameter
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

//
// Generic part: valid for all languages
//
/*
interface Node {
    val position: Position?
}

fun Node.isBefore(other: Node) : Boolean = position!!.start.isBefore(other.position!!.start)

fun Point.isBefore(other: Point) : Boolean = line < other.line || (line == other.line && column < other.column)

data class Point(val line: Int, val column: Int) {
    override fun toString() = "Line $line, Column $column"
}

data class Position(val start: Point, val end: Point)

fun pos(startLine:Int, startCol:Int, endLine:Int, endCol:Int) = Position(
    Point(startLine,startCol),
    Point(endLine,endCol)
)

fun Node.process(operation: (Node) -> Unit) {
    operation(this)
    this.javaClass.kotlin.memberProperties.forEach { p ->
        val v = p.get(this)
        when (v) {
            is Node -> v.process(operation)
            is Collection<*> -> v.forEach { if (it is Node) it.process(operation) }
        }
    }
}

fun <T: Node> Node.specificProcess(klass: Class<T>, operation: (T) -> Unit) {
    process { if (klass.isInstance(it)) { operation(it as T) } }
}

fun Node.transform(operation: (Node) -> Node) : Node {
    operation(this)
    val changes = HashMap<String, Any>()
    this.javaClass.kotlin.memberProperties.forEach { p ->
        val v = p.get(this)
        when (v) {
            is Node -> {
                val newValue = v.transform(operation)
                if (newValue != v) changes[p.name] = newValue
            }
            is Collection<*> -> {
                val newValue = v.map { if (it is Node) it.transform(operation) else it }
                if (newValue != v) changes[p.name] = newValue
            }
        }
    }
    var instanceToTransform = this
    if (!changes.isEmpty()) {
        val constructor = this.javaClass.kotlin.primaryConstructor!!
        val params = HashMap<KParameter, Any?>()
        constructor.parameters.forEach { param ->
            if (changes.containsKey(param.name)) {
                params[param] = changes[param.name]
            } else {
                params[param] = this.javaClass.kotlin.memberProperties.find { param.name == it.name }!!.get(this)
            }
        }
        instanceToTransform = constructor.callBy(params)
    }
    return operation(instanceToTransform)
}

//
// Kotlin specific part
//
*/
data class KotlinFile(val statements : List<Statement>, override var position: Position? = null) : Node()

sealed class Statement : Node() { }

sealed class Expression(open val type: Type) : Node() { }

sealed class Type : Node() { }

//
// Types
//

data class IntType(override var position: Position? = null) : Type()

data class DecimalType(override var position: Position? = null) : Type()

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

data class DecLit(val value: String, override var position: Position? = null) : Expression(DecimalType())

//
// Statements
//

data class PropertyDeclaration(val varName: String, val type: Type, val value: Expression, var mutable: Boolean,  override  var position: Position? = null) :
Statement()
data class VarDeclaration(val varName: String, val value: Expression, override var position: Position? = null) :
    Statement()

data class ReadOnlyVarDeclaration(val varName: String, val value: Expression, override var position: Position? = null) :
    Statement()

data class Assignment(val varName: String, val value: Expression, override var position: Position? = null) : Statement()

data class Print(val value: Expression, override var position: Position? = null) : Statement()