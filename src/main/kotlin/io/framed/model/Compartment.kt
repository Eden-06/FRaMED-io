package io.framed.model

import io.framed.PolymorphicListSerializer
import io.framed.PolymorphicSetSerializer
import io.framed.framework.ModelElement
import kotlinx.serialization.Serializable

/**
 * Model class for an crom compartment.
 *
 * @author Sebastian
 */
@Serializable
class Compartment() : ModelElement<Compartment>() {

    constructor(init: (Compartment) -> Unit) : this() {
        init(this)
    }

    /**
     * Name of this class
     */
    var name: String = "Unnamed compartment"

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


    @Serializable(with = PolymorphicSetSerializer::class)
    var children: Set<ModelElement<*>> = emptySet()

    override fun maxId(): Long = listOf(
            id,
            attributes.map { it.maxId() }.max() ?: 0,
            methods.map { it.maxId() }.max() ?: 0,
            children.map { it.maxId() }.max() ?: 0
            ).max() ?: id

    override fun copy() = Compartment { new ->
        new.name = name
        new.attributes = attributes.map { it.copy() }
        new.methods = methods.map { it.copy() }
        new.children = children.map { it.copy() }.toSet()
    }
}
