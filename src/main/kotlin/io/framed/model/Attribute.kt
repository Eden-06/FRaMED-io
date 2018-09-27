package io.framed.model

/**
 * Model class for an uml attribute.
 *
 * @author lars
 */
class Attribute: Model() {

    /**
     * Name of this attribute.
     */
    var name: String = ""

    /**
     * Type of this attribute.
     */
    var type: String = ""
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