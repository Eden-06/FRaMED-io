package io.framed.model

import kotlinx.serialization.Serializable

/**
 * Model class for an uml class.
 *
 * @author lars
 */
@Serializable
class Class : Model {

    override val id: Long = Model.lastId++

    /**
     * Name of this class
     */
    var name: String = ""

    /**
     * List of class attributes
     */
    var attributes: List<Attribute> = emptyList()

    /**
     * List of class methods
     */
    var methods: List<Method> = emptyList()

    override val metadata = Metadata()
}

/**
 * Create a new class within the current container
 *
 * @param name Name of the new class
 * @param init Builder callback for this class
 *
 * @return The new class
 */
fun Container.clazz(name: String, init: Class.() -> Unit): Class {
    val clazz = Class()
    clazz.name = name
    clazz.init()
    classes += clazz
    return clazz
}