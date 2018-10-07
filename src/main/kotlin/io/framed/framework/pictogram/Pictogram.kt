package io.framed.framework.pictogram

import io.framed.framework.util.EventHandler

/**
 * @author lars
 */
abstract class Pictogram {
    var layer: Layer = Layer()
        set(value) {
            field = value
            onLayerChange.fire(Unit)
        }

    val onSidebar = EventHandler<SidebarEvent>()
    val onContext = EventHandler<ContextEvent>()
    val onLayerChange = EventHandler<Unit>()

    var hasSidebar = false
    var hasContextMenu = false
    var acceptRelation = false
}