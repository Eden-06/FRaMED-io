package io.framed.model

/**
 * Model class for an uml package (package is a reserved keyword).
 *
 * It can contains classes, relations and nested containers.
 *
 * @author lars
 */
class Container : Model {

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
     * List of relations within this container.
     * Should this be a set?
     */
    var relations: List<Relation> = emptyList()

    /**
     * List of nested containers.
     * Should this be a set?
     */
    var containers: List<Container> = emptyList()
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