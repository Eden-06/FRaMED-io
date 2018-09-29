package io.framed.picto

import io.framed.util.EventHandler

/**
 * @author lars
 */
abstract class Picto {
    var layer: Layer = NoneLayer
        set(value) {
            field = value
            onLayerChange.fire(Unit)
        }

    val id = lastId++

    var hasSidebar: Boolean = false
    var hasContext: Boolean = false

    val onSidebar = EventHandler<SidebarEvent>()
    val onContext = EventHandler<ContextEvent>()
    val onLayerChange = EventHandler<Unit>()

    companion object {
        private var lastId = 0
    }
}