package io.framed.model

import io.framed.framework.ModelElement
import kotlinx.serialization.Serializable

/**
 * Model class for an uml package (package is a reserved keyword).
 *
 * It contains classes, connections, role types and nested containers.
 */
@Serializable
class Container : ModelElementMetadata {

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
    var classes: List<Class> = emptyList()

    /**
     * List of connections within this model.
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
 * Create a new nested model within current model.
 *
 * @param init Builder callback for this model.
 *
 * @return The new model.
 */
fun Container.container(init: Container.() -> Unit): Container {
    val c = Container()
    c.init()
    containers += c
    return c
}

/**
 * Create a new standalone model.
 *
 * @param init Builder callback for this model.
 *
 * @return The new model.
 */
fun container(init: Container.() -> Unit): Container {
    val c = Container()
    c.init()
    return c
}