package io.framed.model

/**
 * @author lars
 */
class Class:Model {
    var name: String = ""
    var attributes: List<Attribute> = emptyList()
    var methods: List<Method> = emptyList()
}

fun Container.clazz(name: String, init: Class.() -> Unit): Class {
    val clazz = Class()
    clazz.name = name
    clazz.init()
    classes += clazz
    return clazz
}