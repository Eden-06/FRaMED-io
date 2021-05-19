package io.framed.model

import io.framed.framework.model.ModelConnection
import kotlinx.serialization.Serializable


/**
 * Role implication.
 *
 * @author David Oberacker
 */
@Serializable
class Implication() : ModelConnection() {

    constructor(sourceId: Long, targetId: Long): this() {
        this.sourceId = sourceId
        this.targetId = targetId
    }

    /**
     * Name of this connection.
     */
    var name: String = ""

    override fun copy() = Implication().also { new ->
        new.name = name
    }
}
