package io.framed.model

import io.framed.framework.model.ModelElement
import kotlinx.serialization.Serializable

/**
 * Model class for an uml package (package is a reserved keyword).
 *
 * It contains classes, connections, role types and nested packages.
 */
@Serializable
class Package() : ModelElementMetadata() {

    constructor(init: (Package) -> Unit) : this() {
        init(this)
    }

    /**
     * Name of this package.
     */
    var name: String = "UnnamedPackage"

    var children: Set<ModelElement> = emptySet()

    override fun maxId(): Long = listOf(
            id,
            children.map { it.maxId() }.maxOrNull() ?: 0
    ).maxOrNull() ?: id

    override fun copy(): Package = Package { new ->
        new.name = name
        new.children = children.map { it.copy() }.toSet()
    }
}
