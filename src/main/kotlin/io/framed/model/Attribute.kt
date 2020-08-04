package io.framed.model

import io.framed.visitor.Visitor
import io.framed.framework.model.ModelElement
import kotlinx.serialization.Serializable

/**
 * io.framed.export.crom.Model class for an uml model.
 *
 * @author lars
 */
@Serializable
class Attribute() : ModelElement() {

    constructor(init: (Attribute) -> Unit) : this() {
        init(this)
    }

    /**
     * Name of this model.
     */
    var name: String = "unnamed"

    /**
     * io.framed.export.crom.Type of this model.
     */
    var type: String = ""

    override fun copy() = Attribute { new ->
        new.name = name
        new.type = type
    }

    override fun <T> acceptVisitor(visitor: Visitor<T>) {
        visitor.visit(this)
    }
}
