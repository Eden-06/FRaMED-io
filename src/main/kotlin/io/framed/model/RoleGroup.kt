package io.framed.model

import io.framed.framework.model.ModelElement
import kotlinx.serialization.Polymorphic

class RoleGroup() : ModelElement() {

    constructor(init: (RoleGroup) -> Unit) : this() {
        init(this)
    }

    /**
     * Name of this class
     */
    var name: String = "UnnamedRoleGroup"

    var children: Set<ModelElement> = emptySet()

    override fun maxId(): Long = listOf(
        id,
        children.map { it.maxId() }.maxOrNull() ?: 0
    ).maxOrNull() ?: id

    override fun copy() = RoleGroup { new ->
        new.name = name
        new.children = children.map { it.copy() }.toSet()
    }
}
