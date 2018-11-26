package io.framed.framework.pictogram

import de.westermann.kobserve.EventHandler

/**
 * @author lars
 */
class ViewModel(
        val container: BoxShape,
        val handler: ViewModelHandler
) {
    var layer: Layer
        get() = container.layer
        set(value) {
            container.layer = value
            onLayerChange.emit(Unit)
        }

    var connections: Set<Connection> = emptySet()

    val onConnectionAdd = EventHandler<Connection>()
    val onConnectionRemove = EventHandler<Connection>()

    val onLayerChange = EventHandler<Unit>()
    
    operator fun plusAssign(connection: Connection) {
        connections += connection
        onConnectionAdd.emit(connection)
    }

    operator fun minusAssign(connection: Connection) {
        connections -= connection
        onConnectionRemove.emit(connection)
    }
}