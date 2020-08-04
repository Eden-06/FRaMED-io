package io.framed.model

import io.framed.visitor.Visitor
import io.framed.framework.model.ModelElement
import kotlinx.serialization.Serializable

/**
 * io.framed.export.crom.Model class for an uml parameter.
 *
 * @author lars
 */
@Serializable
class Parameter() : ModelElement() {

    constructor(init: (Parameter) -> Unit) : this() {
        init(this)
    }

    /**
     * Name of this parameter.
     */
    var name: String = "unnamed"

    /**
     * io.framed.export.crom.Type of this parameter.
     */
    var type: String = ""

    override fun toString(): String =
            name + type.let {
                if (type.isBlank()) "" else ": $it"
            }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Parameter) return false

        if (name != other.name) return false
        if (type != other.type) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + type.hashCode()
        return result
    }

    override fun copy() = Parameter { new ->
        new.name = name
        new.type = type
    }

    override fun <T> acceptVisitor(visitor: Visitor<T>) {
        visitor.visit(this)
    }
}
