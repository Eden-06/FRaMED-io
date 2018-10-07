package io.framed.model

import io.framed.framework.ModelElement
import kotlinx.serialization.Serializable

/**
 * Model role model
 */
@Serializable
class RoleType : ModelElement {
    /**
     * Identification of the instance
     */
    override val id: Long = ModelElement.lastId++
    /**
     * Name of the instance
     */
    var name: String = "Unnamed role"
}

/**
 * Create a new role model within the current model
 *
 * @param name Name of the new roletype
 * @param init Builder callback for this roletype
 *
 * @return The new role model
 */
fun Container.roleType(name: String, init: RoleType.() -> Unit): RoleType {
    val type = RoleType()
    type.name = name
    type.init()
    roleTypes += type
    return type
}