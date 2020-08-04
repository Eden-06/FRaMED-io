package io.framed.model

import io.framed.exporter.visitor.Visitor
import io.framed.framework.model.ModelElement
import kotlinx.serialization.Serializable

/**
 * io.framed.exporter.crom.crom.Model class for an uml package (package is a reserved keyword).
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
            children.map { it.maxId() }.max() ?: 0
    ).max() ?: id

    override fun copy(): Package = Package { new ->
        new.name = name
        new.children = children.map { it.copy() }.toSet()
    }

    override fun <T> acceptVisitor(visitor: Visitor<T>) {
        visitor.visit(this)
    }
}
