package io.framed.framework.pictogram

import io.framed.framework.util.EventHandler
import io.framed.framework.util.Point

/**
 * @author lars
 */
class ViewModel(
        val container: BoxShape,
        private val canConnectionStartListener: (source: Shape) -> List<ConnectionInfo>,
        private val canConnectionCreateListener: (source: Shape, target: Shape) -> List<ConnectionInfo>,
        private val createConnectionListener: (source: Shape, target: Shape) -> Unit
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

    val onLayerChange = EventHandler<Unit>()

    operator fun plusAssign(connection: Connection) {
        connections += connection
        onRelationAdd.fire(connection)
    }

    operator fun minusAssign(connection: Connection) {
        connections -= connection
        onRelationRemove.fire(connection)
    }

    fun canConnectionStart(source: Shape): Boolean =
            canConnectionStartListener(source).isNotEmpty()

    fun canConnectionCreate(source: Shape, target: Shape): Boolean =
            canConnectionCreateListener(source, target).isNotEmpty()

    fun createConnection(source: Shape, target: Shape) = createConnectionListener(source, target)

}