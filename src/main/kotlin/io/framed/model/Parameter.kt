package io.framed.model

import io.framed.framework.ModelElement
import kotlinx.serialization.Serializable

/**
 * Model class for an uml parameter.
 *
 * @author lars
 */
@Serializable
class Parameter : ModelElement {

    override val id: Long = ModelElement.lastId++

    /**
     * Name of this parameter.
     */
    var name: String = "unnamed"

    /**
     * Type of this parameter.
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
}

/**
 * Create a new parameter within the current model.
 *
 * @param name Name of the new parameter.
 * @param type Optional model of the new parameter.
 *
 * @return The new parameter.
 */
fun Method.param(name: String, type: String = ""): Parameter {
    val parameter = Parameter()
    parameters += parameter
    parameter.name = name
    parameter.type = type
    return parameter
}