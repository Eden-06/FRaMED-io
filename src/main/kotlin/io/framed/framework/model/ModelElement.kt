package io.framed.framework.model

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
