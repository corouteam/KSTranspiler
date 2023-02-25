package it.poliba.KSTranspiler

import com.strumenta.kolasu.model.Node
import com.strumenta.kolasu.traversing.searchByType
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

    // check if used variable is declared before
    this.specificProcess(ControlStructureBody::class.java) { block ->
        if (block is Block) {
            block.searchByType(VarReference::class.java).forEach {
                val name = it.varName
                // check in function scope first
                if (block.body.filterIsInstance(PropertyDeclaration::class.java).none { it.varName == name }) {
                    // not found, check global variables then
                    if (globalVariables.none { it.varName == name }) {
                        errors.add(
                            Error(
                                "A variable named '${name}' is used but never declared",
                                it.position?.start?.asPosition
                            )
                        )
                    }
                }
            }
        }
    }

    // check for list correct types
    this.specificProcess(ListExpression::class.java) {
        val listType = it.itemsType
        val itemTypes = it.items.map { it.type }

        // Check all items are of the same type
        val differentTypes = itemTypes.filter { it.nodeType != listType.nodeType }
        if (differentTypes.isNotEmpty()){
            errors.add(Error("""
                    List can't contain different types.
                    Found ${differentTypes.first().generateCode()} in a list of ${listType.generateCode()}
                    """.trimIndent(), this.position))
        }
    }

    // check if variable type and assignation match
    this.specificProcess(PropertyDeclaration::class.java) {
        if (it.value == null) return@specificProcess

        if (it.type != it.value.type) {
            errors.add(Error("""
                Type mismatch (${it.value.type.generateCode()} assigned to a variable of type ${it.type.generateCode()}).
            """.trimIndent(), this.position))
        }
    }

    // check val is not reassigned
    this.specificProcess(ControlStructureBody::class.java) { block ->
        if (block is Block) {
            block.searchByType(Assignment::class.java).forEach {
                val assignmentName = it.varName

                val declarationsInBlock = block.body.filterIsInstance(PropertyDeclaration::class.java)

                if (declarationsInBlock.isNotEmpty()) {
                    declarationsInBlock.forEach {
                        if (it.varName == assignmentName && !it.mutable) {
                            errors.add(Error("""
                                Final variable ${it.varName} can not be reassigned.
                            """.trimIndent(), this.position
                                ))

                            // match found, no need to iterate anymore
                            return@specificProcess
                        }
                    }
                }

                // If a declaration is not found in scope, search in global variables
                globalVariables.forEach {
                    if (it.varName == assignmentName && !it.mutable) {
                        errors.add(Error("""
                                Final variable ${it.varName} can not be reassigned.
                            """.trimIndent(), this.position
                        ))

                        // match found, no need to iterate anymore
                        return@specificProcess
                    }
                }
            }
        }
    }

    // check if assignment type matches declaration
    this.specificProcess(ControlStructureBody::class.java) { block ->
        if (block is Block) {
            block.searchByType(Assignment::class.java).forEach {
                val assignmentName = it.varName
                val assignmentType = it.value.type

                val declarationsInBlock = block.body.filterIsInstance(PropertyDeclaration::class.java)

                if (declarationsInBlock.isNotEmpty()) {
                    declarationsInBlock.forEach {
                        if (it.varName == assignmentName) {
                            // Search if assignment type matches declaration type
                            if (assignmentType.generateCode() != it.type.generateCode()) {
                                errors.add(
                                    Error(
                                        """
                                Type mismatch (${assignmentType.generateCode()} assigned to a variable of type ${it.type.generateCode()}).
                            """.trimIndent(), this.position
                                    )
                                )
                            }

                            // match found, no need to iterate anymore
                            return@specificProcess
                        }
                    }
                }

                // If a declaration is not found in scope, search in global variables
                globalVariables.forEach {
                    if (it.varName == assignmentName) {
                        // Search if assignment type matches declaration type
                        if (assignmentType.generateCode() != it.type.generateCode()) {
                            errors.add(
                                Error(
                                    """
                                Type mismatch (${assignmentType.generateCode()} assigned to a variable of type ${it.type.generateCode()}).
                            """.trimIndent(), this.position
                                )
                            )
                        }

                        // match found, no need to iterate anymore
                        return@specificProcess
                    }
                }
            }
        }
    }

    // check if function return expression is present
    // if required, and it's same return type
    this.specificProcess(FunctionDeclaration::class.java) { function ->
        if (function.returnType != null) {
            val returnExpressions = function.body.searchByType(ReturnExpression::class.java).toList()

            if (returnExpressions.isEmpty()){
                errors.add(Error("""
                    A return expression is required for the function ${function.id}.
                """.trimIndent(), this.position))
            } else {
                // check if return type matches
                val returnExpressionType = returnExpressions.last().type

                if (function.returnType::class != returnExpressionType::class) {
                    errors.add(
                        Error(
                            """
                    The return type ${returnExpressionType.generateCode()} does not
                    conform to the expected type ${function.returnType.generateCode()}
                    of the function ${function.id}.
                            """.trimIndent(), this.position
                        )
                    )
                }
            }
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