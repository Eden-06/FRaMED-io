package io.framed.model

import kotlinx.serialization.Serializable

/**
 * Model class for an uml attribute.
 *
 * @author lars
 */
@Serializable
class Attribute : Model {

    override val id: Long = Model.lastId++

    /**
     * Name of this attribute.
     */
    var name: String = ""

    /**
     * Type of this attribute.
     */
    var type: String = ""

    override val metadata = Metadata()
}

/**
 * Create a new attribute within the current class.
 *
 * @param name Name of the new attribute.
 * @param type Optional type of the new attribute.
 *
 * @return The new attribute.
 */
fun Class.attr(name: String, type: String = ""): Attribute {
    val attribute = Attribute()
    attributes += attribute
    attribute.name = name
    attribute.type = type
    return attribute
}