package io.framed.model

import io.framed.PolymorphicListSerializer
import io.framed.framework.ModelElement
import kotlinx.serialization.Serializable

/**
 * Model roleType for an uml roleType.
 *
 * @author lars
 */
@Serializable
class RoleType() : ModelElement<RoleType>() {

    constructor(init: (RoleType) -> Unit) : this() {
        init(this)
    }

    /**
     * Name of this roleType
     */
    var name: String = "UnnamedRole"

    /**
     * List of roleType attributes
     */
    @Serializable(with = PolymorphicListSerializer::class)
    var attributes: List<Attribute> = emptyList()

    /**
     * List of roleType methods
     */
    @Serializable(with = PolymorphicListSerializer::class)
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
