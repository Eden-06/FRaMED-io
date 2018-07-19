package io.framed.model

/**
 * @author lars
 */
class Relation(
        var source: Class,
        var target: Class
) {
    var name: String = ""
}
fun Container.relation(source: Class, target: Class, name: String = ""): Relation {
    val relation = Relation(source, target)
    relation.name = name
    relations += relation
    return relation
}