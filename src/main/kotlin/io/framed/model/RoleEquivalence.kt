package io.framed.model

import io.framed.framework.model.ModelConnection
import kotlinx.serialization.Serializable

/**
 * Role equivalence.
 *
 * @author David Oberacker
 */
@Serializable
class RoleEquivalence() : ModelConnection() {
    constructor(sourceId: Long, targetId: Long): this() {
        this.sourceId = sourceId
        this.targetId = targetId
    }

    /**
     * Name of this connection.
     */
    var name: String = ""

    override fun copy() = RoleEquivalence().also { new ->
        new.name = name
    }
}
