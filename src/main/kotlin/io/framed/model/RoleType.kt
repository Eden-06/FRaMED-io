package io.framed.model

import io.framed.framework.ModelElement
import kotlinx.serialization.Serializable

/**
 * Model roleType for an uml roleType.
 *
 * @author lars
 */
@Serializable
class RoleType() : ModelElement<RoleType> {

    constructor(init: (RoleType) -> Unit) : this() {
        init(this)
    }

    override val id: Long = ModelElement.lastId++

    /**
     * Name of this roleType
     */
    var name: String = "Unnamed role"

    /**
     * List of roleType attributes
     */
    var attributes: List<Attribute> = emptyList()

    /**
     * List of roleType methods
     */
    var methods: List<Method> = emptyList()

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
}
