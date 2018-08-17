package io.framed.picto

import io.framed.util.EventHandler
import io.framed.util.Point

/**
 * @author lars
 */
abstract class Picto {
    var layer: Layer = Layer()
        private set

    open fun setLayer(layer: Layer) {
        this.layer = layer
    }

    var hasSidebar: Boolean = false
    var hasContext: Boolean = false

    val onSidebar = EventHandler<SidebarEvent>()
    val onContext = EventHandler<ContextEvent>()
}