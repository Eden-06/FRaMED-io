package io.framed.model

import io.framed.PolymorphicListSerializer
import io.framed.framework.model.ModelElement
import kotlinx.serialization.Serializable

/**
 * Model class for an uml class.
 *
 * @author lars
 */
@Serializable
class Class() : ModelElement<Class>() {

    constructor(init: (Class) -> Unit) : this() {
        init(this)
    }

    /**
     * Name of this class
     */
    var name: String = "UnnamedClass"

    /**
     * List of class attributes
     */
    @Serializable(with = PolymorphicListSerializer::class)
    var attributes: List<Attribute> = emptyList()

    /**
     * List of class methods
     */
    @Serializable(with = PolymorphicListSerializer::class)
    var methods: List<Method> = emptyList()

    override fun maxId(): Long = listOf(
            id,
            attributes.map { it.maxId() }.max() ?: 0,
            methods.map { it.maxId() }.max() ?: 0
    ).max() ?: id

    override fun copy() = Class { new ->
        new.name = name
        new.attributes = attributes.map { it.copy() }
        new.methods = methods.map { it.copy() }
    }
}
