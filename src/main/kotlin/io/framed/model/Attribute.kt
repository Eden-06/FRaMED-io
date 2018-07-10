package io.framed.model

/**
 * @author lars
 */
class Attribute() {
    var name: String = ""
    var type: String = ""
    var visibility: Visibility = Visibility.NONE

    override fun toString(): String =
            "${visibility.symbol} $name" + type.let {
                if (type.isBlank()) "" else ": $it"
            }
}

fun CompartmentType.attr(name: String, type: String = "", visibility: Visibility = Visibility.NONE): Attribute {
    val attribute = Attribute()
    attributes += attribute
    attribute.name = name
    attribute.type = type
    attribute.visibility = visibility
    return attribute
}