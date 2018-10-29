package io.framed.framework.pictogram

import io.framed.framework.util.EventHandler

/**
 * @author lars
 */
abstract class Pictogram {

    abstract val id: Long?

    var layer: Layer = Layer()
        set(value) {
            field = value
            onLayerChange.fire(Unit)
        }

    val onSidebar = EventHandler<SidebarEvent>()
    val onContextMenu = EventHandler<ContextEvent>()
    val onLayerChange = EventHandler<Unit>()

    var hasSidebar = false
    var hasContextMenu = false

    override fun toString(): String {
        return "${this::class.simpleName}($id)"
    }
}