package io.framed.model

/**
 * @author lars
 */
class Relation(
    var source: CompartmentType,
    var target: CompartmentType
) {
    var name: String = ""
}
fun Diagram.relation(source: CompartmentType, target: CompartmentType, name: String = ""): Relation {
    val relation = Relation(source, target)
    relation.name = name
    relations += relation
    return relation
}