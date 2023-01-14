package it.poliba.KSTranspiler

import com.strumenta.kolasu.model.Node
import java.util.*

fun Node.commonValidation(): LinkedList<Error> {
    val errors = LinkedList<Error>()

    // check a variable is not duplicated
    this.specificProcess(ListExpression::class.java) {
        val listType = it.itemsType
        val itemTypes = it.items.map { it.type }

        // Check all items are of the same type
        val differentTypes = itemTypes.filter { it != listType }
        if (differentTypes.isNotEmpty()){
            errors.add(Error("""
                    List can't contain different types.
                    Found ${differentTypes.first()} in a list of $listType
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