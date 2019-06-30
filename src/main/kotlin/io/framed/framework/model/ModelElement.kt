package io.framed.framework.model

import kotlinx.serialization.Serializable

/**
 * Base class for all model elements.
 *
 * @author lars
 */
@Serializable
abstract class ModelElement<M : ModelElement<M>> {

    val id: Long = lastId++

    abstract fun copy(): M
    open fun maxId(): Long = id

    companion object {
        var lastId: Long = 0
    }
}
