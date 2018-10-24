package io.framed.model

import io.framed.framework.ModelElement
import kotlinx.serialization.Serializable

/**
 * Model class for an uml package (package is a reserved keyword).
 *
 * It contains classes, connections, role types and nested containers.
 */
@Serializable
class Container() : ModelElementMetadata<Container> {

    constructor(init: (Container) -> Unit) : this() {
        init(this)
    }

    /**
     * Identification of the instance
     */
    override val id: Long = ModelElement.lastId++

    /**
     * Name of this package.
     */
    var name: String = "Unnamed container"

    /**
     * List of nested classes.
     * Should this be a set?
     */
    var classes: Set<Class> = emptySet()

    /**
     * List of related compartments
     */
    var compartments: Set<Compartment> = emptySet()

    /**
     * List of nested containers.
     * Should this be a set?
     */
    var containers: Set<Container> = emptySet()

    /**
     * List of related role types
     */
    var roleTypes: Set<RoleType> = emptySet()

    /**
     * List of all events of the current application
     */
    var events: Set<Event> = emptySet()

    /**
     * Stores the metadata of the current model
     */
    override val metadata = Metadata()

    override fun maxId(): Long = listOf(
            id,
            containers.map { it.maxId() }.max() ?: 0,
            compartments.map { it.maxId() }.max() ?: 0,
            classes.map { it.maxId() }.max() ?: 0,
            roleTypes.map { it.maxId() }.max() ?: 0,
            events.map { it.maxId() }.max() ?: 0
    ).max() ?: id

    override fun copy() = Container {new ->
        new.name = name
        new.classes = classes
        new.compartments = compartments
        new.containers = containers
        new.roleTypes = roleTypes
        new.events = events
    }
}
