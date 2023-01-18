package it.poliba.KSTranspiler

import com.strumenta.kolasu.model.Node
import com.strumenta.kolasu.traversing.walkAncestors
import java.util.*
import kotlin.collections.ArrayList

fun Node.commonValidation(): LinkedList<Error> {
    val errors = LinkedList<Error>()

    val allVariables = ArrayList<PropertyDeclaration>()
    this.specificProcess(PropertyDeclaration::class.java) {
        allVariables.add(it)
    }

    // check for variables duplicates in functions
    val variablesInFunctions = HashMap<String, PropertyDeclaration>()
    this.specificProcess(ControlStructureBody::class.java) {
        val varsByName = HashMap<String, PropertyDeclaration>()
        if (it is Block) {
            it.body
                .filterIsInstance<PropertyDeclaration>()
                .forEach {
                    variablesInFunctions[it.varName] = it
                    if (varsByName.containsKey(it.varName)) {
                        errors.add(
                            Error(
                                "A variable named '${it.varName}' has been already declared",
                                it.position?.start?.asPosition
                            )
                        )
                    } else {
                        varsByName[it.varName] = it
                    }
                }
        }
    }

    // check for duplicates in global variables
    val globalVariables = allVariables.filterNot { variablesInFunctions.containsKey(it.varName) }
    globalVariables
        .groupBy { it.varName }
        .forEach {
            if (it.value.size > 1) {
                // variable with same name found
                errors.add(
                    Error(
                        "A variable named '${it.key}' has been already declared",
                        it.value.first().position?.start?.asPosition
                    )
                )
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