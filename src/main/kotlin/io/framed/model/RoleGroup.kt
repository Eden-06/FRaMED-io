package io.framed.model

import io.framed.framework.model.ModelElement
import io.framed.init
import kotlinx.serialization.Polymorphic

class RoleGroup() : ModelElement() {

    constructor(init: (RoleGroup) -> Unit) : this() {
        init(this)
    }

    constructor(cardinality: String) : this() {
        this.cardinality = cardinality
    }

    /**
     * Name of this class
     */
    var name: String = "UnnamedRoleGroup"

    /**
     * Cardinality for the role group.
     */
    var cardinality: String = "*"


    var children: Set<ModelElement> = emptySet()

    override fun maxId(): Long = listOf(
        id,
        children.map { it.maxId() }.maxOrNull() ?: 0
    ).maxOrNull() ?: id

    override fun copy() = RoleGroup { new ->
        new.name = name
        new.cardinality = cardinality
        new.children = children.map { it.copy() }.toSet()
    }
}
