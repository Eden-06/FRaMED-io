package io.framed.model

/**
 * @author lars
 */
class Diagram {
    var compartments: List<CompartmentType> = emptyList()
    var relations: List<Relation> = emptyList()
}

fun diagram(init: Diagram.() -> Unit): Diagram {
    val d = Diagram()
    d.init()
    return d
}