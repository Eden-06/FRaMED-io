package io.framed.framework.pictogram

import io.framed.framework.util.EventHandler
import io.framed.framework.util.Property

/**
 * @author lars
 */
class Connection(
        val source: Property<Shape>,
        val target: Property<Shape>
) : Pictogram() {
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
