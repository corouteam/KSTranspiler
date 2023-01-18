package it.poliba.KSTranspiler

import com.strumenta.kolasu.model.Node
import com.strumenta.kolasu.model.children
import com.strumenta.kolasu.traversing.findAncestorOfType
import com.strumenta.kolasu.traversing.walk
import com.strumenta.kolasu.traversing.walkAncestors
import it.poliba.KSTranspiler.SwiftParser.VarDeclarationContext
import java.util.*

fun Node.commonValidation(): LinkedList<Error> {
    val errors = LinkedList<Error>()

    this.specificProcess(ControlStructureBody::class.java) {
        val varsByName = HashMap<String, PropertyDeclaration>()

        if (it is Block) {
            it.body
                .filterIsInstance<PropertyDeclaration>()
                .forEach {
                    if (varsByName.containsKey(it.varName)) {
                        it.walkAncestors().forEach {
                            println("test ${it.nodeType}")
                        }
                        errors.add(
                            Error(
                                "A variable named '${it.varName}' has been already declared at ${varsByName[it.varName]?.position?.start}",
                                it.position?.start?.asPosition
                            )
                        )
                    } else {
                        varsByName[it.varName] = it
                    }
                }
        }
    }

    // check for list correct types
    this.specificProcess(ListExpression::class.java) {
        val listType = it.itemsType
        val itemTypes = it.items.map { it.type }

        // Check all items are of the same type
        val differentTypes = itemTypes.filter { it != listType }
        if (differentTypes.isNotEmpty()){
            errors.add(Error("""
                    List can't contain different types.
                    Found ${differentTypes.first().nodeType} in a list of ${listType.nodeType}
                    """.trimIndent(), this.position))
        }
    }

    return errors
}

fun AstFile.validate(): List<Error> {
    val errors = LinkedList<Error>()

    errors.addAll(commonValidation())
    return errors
}

fun AstScript.validate(): List<Error> {
    val errors = LinkedList<Error>()

    errors.addAll(commonValidation())
    return errors
}