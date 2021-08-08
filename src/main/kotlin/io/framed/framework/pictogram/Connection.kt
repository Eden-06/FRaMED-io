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

    /**
     * Toggle if the connection source should target the port representation.
     *
     * FIXME: Implement proper solution to switch between port targeting and regular targeting.
     */
    var useSourcePortEndpoint: Boolean = false
    /**
     * Toggle if the sconnection target should target the port representation.
     *
     * FIXME: Implement proper solution to switch between port targeting and regular targeting.
     */
    var useTargetPortEndpoint: Boolean = false

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
