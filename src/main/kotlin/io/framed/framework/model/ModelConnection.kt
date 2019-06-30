package io.framed.framework.model

import kotlinx.serialization.Serializable

/**
 * Base class for all model connections.
 */
@Serializable
abstract class ModelConnection<M : ModelConnection<M>>() : ModelElement<M>() {

    var sourceId: Long = id
    var targetId: Long = id
}
