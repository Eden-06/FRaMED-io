package io.framed.framework.pictogram

import io.framed.framework.util.EventHandler

/**
 * @author lars
 */
class ViewModel(
        val container: BoxShape
) {
    var layer: Layer
        get() = container.layer
        set(value) {
            container.layer = value
            onLayerChange.fire(Unit)
        }

    var connections: List<Connection> = emptyList()

    val onRelationAdd = EventHandler<Connection>()
    val onRelationRemove = EventHandler<Connection>()

    val onRelationDraw = EventHandler<Pair<Shape, Shape>>()
    val onLayerChange = EventHandler<Unit>()

    operator fun plusAssign(connection: Connection) {
        connections += connection
        onRelationAdd.fire(connection)
    }

    operator fun minusAssign(connection: Connection) {
        connections -= connection
        onRelationRemove.fire(connection)
    }
}