package io.framed.framework

import kotlinx.serialization.Serializable

/**
 * Base model interface for easier access.
 *
 * @author lars
 */
@Serializable
abstract class ModelElement<M : ModelElement<M>> {

    val id: Long = lastId ++

    abstract fun copy(): M
    open fun maxId(): Long = id

    companion object {
        var lastId: Long = 0
    }
}
