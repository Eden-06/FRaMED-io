package io.framed.model

import kotlinx.serialization.Serializable

/**
 * Model class for an uml class.
 *
 * @author lars
 */
@Serializable
class RoleType : Model {

    override val id: Long = Model.lastId++

    /**
     * Name of this class
     */
    var name: String = ""

    override val metadata = Metadata()
}

/**
 * Create a new roletype within the current container
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
    roletypes += type
    return type
}