package io.framed.picto

import io.framed.util.Property

/**
 * @author lars
 */
class Relation(
        val source: Property<Shape>,
        val target: Property<Shape>,
        val type: Property<io.framed.model.Relation.Type>
) : Picto() {
    var labels: Map<TextShape, Double> = emptyMap()
}

fun relation(source: Property<Shape>, target: Property<Shape>, type: Property<io.framed.model.Relation.Type>, init: Relation.() -> Unit) =
        Relation(source, target, type).also(init)