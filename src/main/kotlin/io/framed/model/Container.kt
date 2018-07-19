package io.framed.model

/**
 * @author lars
 */
class Container:Model {
    var name: String = ""
    var classes: List<Class> = emptyList()
    var relations: List<Relation> = emptyList()
    var containers: List<Container> = emptyList()
}

fun Container.container(init: Container.() -> Unit): Container {
    val c = Container()
    c.init()
    containers += c
    return c
}

fun container(init: Container.() -> Unit): Container {
    val c = Container()
    c.init()
    return c
}