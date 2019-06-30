package io.framed.framework.pictogram

import de.westermann.kobserve.Property
import de.westermann.kobserve.event.EventHandler
import io.framed.framework.linker.ConnectionLinker

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
