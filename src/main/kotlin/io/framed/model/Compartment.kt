package io.framed.model

import io.framed.framework.ModelElement
import kotlinx.serialization.Serializable

/**
 * Model class for an crom compartment.
 *
 * @author Sebastian
 */
@Serializable
class Compartment() : ModelElement<Compartment> {

    constructor(init: (Compartment) -> Unit) : this() {
        init(this)
    }

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
     * List of class methods
     */
    var methods: List<Method> = emptyList()

    /**
     * List of classes
     */
    var classes: Set<Class> = emptySet()

    override fun copy() = Compartment { new ->
        new.name = name
        new.attributes = attributes
        new.classes = classes
        new.methods = methods
    }
}
