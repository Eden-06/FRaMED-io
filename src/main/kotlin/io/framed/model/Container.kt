package io.framed.model

import kotlinx.serialization.Serializable

/**
 * Model class for an uml package (package is a reserved keyword).
 *
 * It contains classes, connections, role types and nested containers.
 */
@Serializable
class Container : Model {

    /**
     * Identification of the instance
     */
    override val id: Long = Model.lastId++

    /**
     * Name of this package.
     */
    var name: String = ""

    /**
     * List of nested classes.
     * Should this be a set?
     */
    var classes: List<Class> = emptyList()

    /**
     * List of connections within this container.
     * Should this be a set?
     */
    var relations: List<Relation> = emptyList()

    /**
     * List of nested containers.
     * Should this be a set?
     */
    var containers: List<Container> = emptyList()

    /**
     * List of related role types
     */
    var roleTypes: List<RoleType> = emptyList()

    /**
     * List of all events of the current application
     */
    var events: List<Event> = emptyList()

    /**
     * Stores the metadata of the current model
     */
    override val metadata = Metadata()
}

/**
 * Create a new nested container within current container.
 *
 * @param init Builder callback for this container.
 *
 * @return The new container.
 */
fun Container.container(init: Container.() -> Unit): Container {
    val c = Container()
    c.init()
    containers += c
    return c
}

/**
 * Create a new standalone container.
 *
 * @param init Builder callback for this container.
 *
 * @return The new container.
 */
fun container(init: Container.() -> Unit): Container {
    val c = Container()
    c.init()
    return c
}