package io.framed.model

import io.framed.framework.ModelConnection
import kotlinx.serialization.Serializable

/**
 * Model class for an BROS destroy relation
 *
 * @author Sebastian
 */
@Serializable
class DestroyRelationship() : ModelConnection<DestroyRelationship>() {

    constructor(sourceId: Long, targetId: Long): this() {
        this.sourceId = sourceId
        this.targetId = targetId
    }

    /**
     * Name of this connection.
     */
    var name: String = ""

    override fun copy() = DestroyRelationship().also { new ->
        new.name = name
    }
}
