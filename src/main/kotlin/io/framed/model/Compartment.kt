package io.framed.model

import io.framed.framework.ModelElement
import kotlinx.serialization.Serializable

/**
 * Model class for an crom compartment.
 *
 * @author Sebastian
 */
@Serializable
class Compartment : ModelElement {

    override val id: Long = ModelElement.lastId++

    /**
     * Name of this class
     */
    var name: String = "Unnamed compartment"

    /**
     * List of class attributes
     */
    var attributes: List<Attribute> = emptyList()

    /**
     * List of classes
     */
    var classes: List<Class> = emptyList()

    /**
     * List of class methods
     */
    var methods: List<Method> = emptyList()
}

/**
 * Create a new class within the current model
 *
 * @param name Name of the new compartment
 * @param init Builder callback for this compartment
 *
 * @return The new class
 */
fun Container.compartment(name: String, init: Compartment.() -> Unit): Compartment {
    val compartment = Compartment()
    compartment.name = name
    compartment.init()
    compartments += compartment
    return compartment
}