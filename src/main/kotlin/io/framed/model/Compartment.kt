package io.framed.model

import io.framed.framework.ModelElement
import kotlinx.serialization.Serializable

/**
 * Model class for an crom compartment.
 *
 * @author Sebastian
 */
@Serializable
class Compartment() : ModelElement<Compartment> {

    constructor(init: (Compartment) -> Unit) : this() {
        init(this)
    }

    override val id: Long = ModelElement.lastId++

    /**
     * Name of this class
     */
    var name: String = "Unnamed compartment"

    /**
     * List of class attributes
     */
    var attributes: List<Attribute> = emptyList()

    /**
     * List of class methods
     */
    var methods: List<Method> = emptyList()

    /**
     * List of classes
     */
    var classes: Set<Class> = emptySet()

    /**
     * List of related role types
     */
    var roleTypes: Set<RoleType> = emptySet()

    /**
     * List of all events of the current application
     */
    var events: Set<Event> = emptySet()

    override fun maxId(): Long = listOf(
            id,
            attributes.map { it.maxId() }.max() ?: 0,
            methods.map { it.maxId() }.max() ?: 0,
            classes.map { it.maxId() }.max() ?: 0,
            roleTypes.map { it.maxId() }.max() ?: 0,
            events.map { it.maxId() }.max() ?: 0
    ).max() ?: id

    override fun copy() = Compartment { new ->
        new.name = name
        new.attributes = attributes.map { it.copy() }
        new.methods = methods.map { it.copy() }
        new.classes = classes.map { it.copy() }.toSet()
        new.roleTypes = roleTypes.map { it.copy() }.toSet()
        new.events = events.map { it.copy() }.toSet()
    }
}
