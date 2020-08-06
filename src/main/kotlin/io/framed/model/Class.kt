package io.framed.model

import io.framed.exporter.visitor.Visitor
import io.framed.framework.model.ModelElement
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable

/**
 * Model class for an uml class.
 *
 * @author lars
 */
@Serializable
class Class() : ModelElement() {

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
    var attributes: List<@Polymorphic Attribute> = emptyList()

    /**
     * List of class methods
     */
    var methods: List<@Polymorphic Method> = emptyList()

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

    override fun <T> acceptVisitor(visitor: Visitor<T>) {
        visitor.visit(this)
    }
}
