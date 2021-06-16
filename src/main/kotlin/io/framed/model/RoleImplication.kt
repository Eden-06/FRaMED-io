package io.framed.model

import io.framed.framework.model.ModelConnection
import kotlinx.serialization.Serializable


/**
 * Role implication.
 *
 * @author David Oberacker
 */
@Serializable
class RoleImplication() : ModelConnection() {

    constructor(sourceId: Long, targetId: Long): this() {
        this.sourceId = sourceId
        this.targetId = targetId
    }

    /**
     * Name of this connection.
     */
    var name: String = ""

    override fun copy() = RoleImplication().also { new ->
        new.name = name
    }
}
