package io.framed.model

import io.framed.framework.ModelElement
import kotlinx.serialization.Serializable

/**
 * Model role model
 */
@Serializable
class RoleType() : ModelElement<RoleType> {

    constructor(init: (RoleType) -> Unit) : this() {
        init(this)
    }

    /**
     * Identification of the instance
     */
    override val id: Long = ModelElement.lastId++
    /**
     * Name of the instance
     */
    var name: String = "Unnamed role"

    override fun copy() = RoleType { new ->
        new.name = name
    }
}
