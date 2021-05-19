package io.framed.model

import io.framed.framework.model.ModelConnection
import kotlinx.serialization.Serializable

/**
 * Prohibition role constraint for role types.
 *
 * @author David Oberacker
 */
@Serializable
class Prohibition() : ModelConnection() {

    constructor(sourceId: Long, targetId: Long): this() {
        this.sourceId = sourceId
        this.targetId = targetId
    }

    /**
     * Name of this connection.
     */
    var name: String = ""

    override fun copy() = Prohibition().also {
            new -> new.name = name
    }
}
