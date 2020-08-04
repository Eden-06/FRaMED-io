package io.framed.model

import io.framed.visitor.Visitor
import io.framed.framework.model.ModelElement
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable

/**
 * io.framed.export.crom.Model class for an crom compartment.
 *
 * @author Sebastian
 */
@Serializable
class Compartment() : ModelElement() {

    constructor(init: (Compartment) -> Unit) : this() {
        init(this)
    }

    /**
     * Name of this class
     */
    var name: String = "UnnamedCompartment"

    /**
     * List of class attributes
     */
    var attributes: List<@Polymorphic Attribute> = emptyList()

    /**
     * List of class methods
     */
    var methods: List<@Polymorphic Method> = emptyList()


    var children: Set<ModelElement> = emptySet()

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

    override fun <T> acceptVisitor(visitor: Visitor<T>) {
        visitor.visit(this)
    }
}
