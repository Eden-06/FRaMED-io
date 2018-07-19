package io.framed.model

/**
 * @author lars
 */
class Attribute: Model {
    var name: String = ""
    var type: String = ""

    override fun toString(): String =
            name + type.let {
                if (type.isBlank()) "" else ": $it"
            }.trim()
}

fun Class.attr(name: String, type: String = ""): Attribute {
    val attribute = Attribute()
    attributes += attribute
    attribute.name = name
    attribute.type = type
    return attribute
}