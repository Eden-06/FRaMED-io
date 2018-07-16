package io.framed.model

/**
 * @author lars
 */
class CompartmentType() {
    var name: String = ""
    var attributes: List<Attribute> = emptyList()
    var methods: List<Method> = emptyList()
}

fun Diagram.compartmentType(name: String, init: CompartmentType.() -> Unit): CompartmentType {
    val compartmentType = CompartmentType()
    compartmentType.name = name
    compartmentType.init()
    compartments += compartmentType
    return compartmentType
}