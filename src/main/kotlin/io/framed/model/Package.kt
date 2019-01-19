package io.framed.model

import io.framed.framework.ModelElement
import kotlinx.serialization.Serializable

/**
 * Model class for an uml package (package is a reserved keyword).
 *
 * It contains classes, connections, role types and nested packages.
 */
@Serializable
class Package() : ModelElementMetadata<Package> {

    constructor(init: (Package) -> Unit) : this() {
        init(this)
    }

    /**
     * Identification of the instance
     */
    override val id: Long = ModelElement.lastId++

    /**
     * Name of this package.
     */
    var name: String = "Unnamed package"

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
     * List of nested packages.
     * Should this be a set?
     */
    var packages: Set<Package> = emptySet()

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
            packages.map { it.maxId() }.max() ?: 0,
            compartments.map { it.maxId() }.max() ?: 0,
            classes.map { it.maxId() }.max() ?: 0,
            roleTypes.map { it.maxId() }.max() ?: 0,
            events.map { it.maxId() }.max() ?: 0
    ).max() ?: id

    override fun copy(): Package = Package { new ->
        new.name = name
        new.classes = classes.map { it.copy() }.toSet()
        new.compartments = compartments.map { it.copy() }.toSet()
        new.packages = packages.map { it.copy() }.toSet()
        new.roleTypes = roleTypes.map { it.copy() }.toSet()
        new.events = events.map { it.copy() }.toSet()
    }
}
