package io.framed.model

import io.framed.framework.ModelElement
import kotlinx.serialization.Serializable

/**
 * Model class for an uml class.
 *
 * @author lars
 */
@Serializable
class Class() : ModelElement<Class> {

    constructor(init: (Class) -> Unit) : this() {
        init(this)
    }

    override val id: Long = ModelElement.lastId++

    /**
     * Name of this class
     */
    var name: String = "Unnamed class"

    /**
     * List of class attributes
     */
    var attributes: List<Attribute> = emptyList()

    /**
     * List of class methods
     */
    var methods: List<Method> = emptyList()

    override fun copy() = Class { new ->
        new.name = name
        new.attributes = attributes
        new.methods = methods
    }
}
