package io.framed.model

import io.framed.PolymorphicSetSerializer
import io.framed.framework.ModelElement
import kotlinx.serialization.Serializable

/**
 * Model class for an uml package (package is a reserved keyword).
 *
 * It contains classes, connections, role types and nested packages.
 */
@Serializable
class Package() : ModelElementMetadata<Package>() {

    constructor(init: (Package) -> Unit) : this() {
        init(this)
    }

    /**
     * Name of this package.
     */
    var name: String = "Unnamed package"

    @Serializable(with = PolymorphicSetSerializer::class)
    var children: Set<ModelElement<*>> = emptySet()

    override fun maxId(): Long = listOf(
            id,
            children.map { it.maxId() }.max() ?: 0
    ).max() ?: id

    override fun copy(): Package = Package { new ->
        new.name = name
        new.children = children.map { it.copy() }.toSet()
    }
}
