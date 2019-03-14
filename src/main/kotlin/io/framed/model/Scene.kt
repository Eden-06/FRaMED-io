package io.framed.model

import io.framed.PolymorphicListSerializer
import io.framed.PolymorphicSetSerializer
import io.framed.framework.ModelElement
import kotlinx.serialization.Serializable

/**
 * Model class for a bros scene.
 */
@Serializable
class Scene() : ModelElement<Scene>() {

    constructor(init: (Scene) -> Unit) : this() {
        init(this)
    }

    /**
     * Name of this scene
     */
    var name: String = "Unnamed scene"

    /**
     * List of scene attributes
     */
    @Serializable(with = PolymorphicListSerializer::class)
    var attributes: List<Attribute> = emptyList()


    @Serializable(with = PolymorphicSetSerializer::class)
    var children: Set<ModelElement<*>> = emptySet()

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
}
