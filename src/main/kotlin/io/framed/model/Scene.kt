package io.framed.model

import io.framed.visitor.Visitor
import io.framed.framework.model.ModelElement
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable

/**
 * io.framed.export.crom.Model class for a bros scene.
 */
@Serializable
class Scene() : ModelElement() {

    constructor(init: (Scene) -> Unit) : this() {
        init(this)
    }

    /**
     * Name of this scene
     */
    var name: String = "UnnamedScene"

    /**
     * List of scene attributes
     */
    var attributes: List<@Polymorphic Attribute> = emptyList()


    var children: Set<ModelElement> = emptySet()

    override fun maxId(): Long = listOf(
            id,
            attributes.map { it.maxId() }.max() ?: 0,
            children.map { it.maxId() }.max() ?: 0
            ).max() ?: id

    override fun copy() = Scene { new ->
        new.name = name
        new.attributes = attributes.map { it.copy() }
        new.children = children.map { it.copy() }.toSet()
    }

    override fun <T> acceptVisitor(visitor: Visitor<T>) {
        visitor.visit(this)
    }
}
