package io.framed.framework.model

import io.framed.visitor.Visitor
import kotlinx.serialization.Serializable

/**
 * Base class for all model elements.
 *
 * @author lars
 */
@Serializable
abstract class ModelElement {

    val id: Long = lastId++

    abstract fun copy(): ModelElement

    abstract fun <T> acceptVisitor(visitor: Visitor<T>)

    open fun maxId(): Long = id

    companion object {
        var lastId: Long = 0
    }
}

inline fun <reified T: ModelElement> T.copyGeneric():T {
    val element = copy()
    if (element is T) return element
    throw IllegalStateException()
}
