package io.framed.model

import kotlinx.serialization.Serializable

/**
 * Model role type
 */
@Serializable
class RoleType : Model {
    /**
     * Identification of the instance
     */
    override val id: Long = Model.lastId++
    /**
     * Name of the instance
     */
    var name: String = ""
    /**
     * Metadata of the instance
     */
    override val metadata = Metadata()
}

/**
 * Create a new role type within the current container
 *
 * @param name Name of the new roletype
 * @param init Builder callback for this roletype
 *
 * @return The new role type
 */
fun Container.roleType(name: String, init: RoleType.() -> Unit): RoleType {
    val type = RoleType()
    type.name = name
    type.init()
    roleTypes += type
    return type
}