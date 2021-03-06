package io.framed.model

import io.framed.framework.model.ModelElement
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable

/**
 * Model class for a bros scene.
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
            attributes.map { it.maxId() }.maxOrNull() ?: 0,
            children.map { it.maxId() }.maxOrNull() ?: 0
            ).maxOrNull() ?: id

    override fun copy() = Scene { new ->
        new.name = name
        new.attributes = attributes.map { it.copy() }
        new.children = children.map { it.copy() }.toSet()
    }
}
