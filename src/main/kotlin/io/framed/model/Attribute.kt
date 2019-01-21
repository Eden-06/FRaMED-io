package io.framed.model

import io.framed.framework.ModelElement
import kotlinx.serialization.Serializable

/**
 * Model class for an uml model.
 *
 * @author lars
 */
@Serializable
class Attribute() : ModelElement<Attribute>() {

    constructor(init: (Attribute) -> Unit) : this() {
        init(this)
    }

    /**
     * Name of this model.
     */
    var name: String = "unnamed"

    /**
     * Type of this model.
     */
    var type: String = ""

    override fun copy() = Attribute { new ->
        new.name = name
        new.type = type
    }
}
