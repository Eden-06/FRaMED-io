package io.framed.model

import io.framed.framework.ModelElement
import kotlinx.serialization.Serializable

/**
 * Model class for an uml model.
 *
 * @author lars
 */
@Serializable
class Attribute : ModelElement {

    override val id: Long = ModelElement.lastId++

    /**
     * Name of this model.
     */
    var name: String = "unnamed"

    /**
     * Type of this model.
     */
    var type: String = ""
}

/**
 * Create a new model within the current class.
 *
 * @param name Name of the new model.
 * @param type Optional model of the new model.
 *
 * @return The new model.
 */
fun Class.attr(name: String, type: String = ""): Attribute {
    val attribute = Attribute()
    attributes += attribute
    attribute.name = name
    attribute.type = type
    return attribute
}