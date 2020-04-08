package io.framed.framework.model

import kotlinx.serialization.Serializable

/**
 * Base class for all model connections.
 */
@Serializable
abstract class ModelConnection : ModelElement() {

    abstract override fun copy(): ModelConnection

    var sourceId: Long = id
    var targetId: Long = id
}
