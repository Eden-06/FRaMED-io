package io.framed.picto

import io.framed.util.EventHandler
import io.framed.util.Property

/**
 * @author lars
 */
class Connection(
        val source: Property<Shape>,
        val target: Property<Shape>
) : Picto() {
    var labels: Map<TextShape, Double> = emptyMap()
        set(value) {
            field = value
            onStyleChange.fire(Unit)
        }

    var lines: List<ConnectionLine> = emptyList()
        set(value) {
            field = value
            onStyleChange.fire(Unit)
        }

    var sourceStyle: ConnectionEnd? = null
        set(value) {
            field = value
            onStyleChange.fire(Unit)
        }

    var targetStyle: ConnectionEnd? = null
        set(value) {
            field = value
            onStyleChange.fire(Unit)
        }

    val onStyleChange = EventHandler<Unit>()
}

fun connection(source: Property<Shape>, target: Property<Shape>, init: Connection.() -> Unit) =
        Connection(source, target).also(init)
