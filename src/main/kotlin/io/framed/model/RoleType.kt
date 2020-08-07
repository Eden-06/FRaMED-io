package io.framed.model

import io.framed.exporter.visitor.Visitor
import io.framed.framework.model.ModelElement
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable

/**
 * Model roleType for an uml roleType.
 *
 * @author lars
 */
@Serializable
class RoleType() : ModelElement() {

    constructor(init: (RoleType) -> Unit) : this() {
        init(this)
    }

    /**
     * Name of this roleType
     */
    var name: String = "UnnamedRole"

    /**
     * Occurrence constraint of this roleType
     */
    var occurrenceConstraint: String = ""

    /**
     * List of roleType attributes
     */
    var attributes: List<@Polymorphic Attribute> = emptyList()

    /**
     * List of roleType methods
     */
    var methods: List<@Polymorphic Method> = emptyList()

    override fun maxId(): Long = listOf(
            id,
            attributes.map { it.maxId() }.max() ?: 0,
            methods.map { it.maxId() }.max() ?: 0
    ).max() ?: id

    override fun copy() = RoleType { new ->
        new.name = name
        new.attributes = attributes.map { it.copy() }
        new.methods = methods.map { it.copy() }
    }

    override fun acceptVisitor(visitor: Visitor) {
        visitor.visit(this)
    }
}
