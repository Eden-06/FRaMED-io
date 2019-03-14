package io.framed.model

import io.framed.framework.ModelConnection
import kotlinx.serialization.Serializable

/**
 * Model class for an BROS create relation
 *
 * @author Sebastian
 */
@Serializable
class CreateRelationship() : ModelConnection<CreateRelationship>() {

    constructor(sourceId: Long, targetId: Long): this() {
        this.sourceId = sourceId
        this.targetId = targetId
    }

    /**
     * Name of this connection.
     */
    var name: String = ""

    /**
     * Cardinality for the source side of this connection.
     */
    var sourceCardinality: String = "*"

    /**
     * Cardinality for the target side of this connection.
     */
    var targetCardinality: String = "*"

    override fun copy() = CreateRelationship().also { new ->
        new.name = name
        new.sourceCardinality = sourceCardinality
        new.targetCardinality = targetCardinality
    }
}
