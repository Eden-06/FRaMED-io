package io.framed.framework.pictogram

import de.westermann.kobserve.EventHandler
import de.westermann.kobserve.Property
import io.framed.framework.ConnectionLinker

/**
 * @author lars
 */
class Connection(
        val source: Property<Long>,
        val target: Property<Long>,
        id: Long
) : Pictogram(id) {

    var lines: List<ConnectionLine> = emptyList()
        set(value) {
            field = value
            onStyleChange.emit(Unit)
        }

    var sourceStyle: ConnectionEnd? = null
        set(value) {
            field = value
            onStyleChange.emit(Unit)
        }

    var targetStyle: ConnectionEnd? = null
        set(value) {
            field = value
            onStyleChange.emit(Unit)
        }

    val onStyleChange = EventHandler<Unit>()
}

fun ConnectionLinker<*>.connection(source: Property<Long>, target: Property<Long>, init: Connection.() -> Unit) =
        Connection(source, target, id).also(init)
