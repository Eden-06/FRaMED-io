package io.framed.framework.pictogram

import de.westermann.kobserve.EventHandler
import de.westermann.kobserve.basic.property

/**
 * @author lars
 */
abstract class Pictogram(
        val id: Long?
) {

    val layerProperty = property(Layer())
    var layer by layerProperty

    val onSidebar = EventHandler<SidebarEvent>()
    val onContextMenu = EventHandler<ContextEvent>()

    var hasSidebar = false
    var hasContextMenu = false

    override fun toString(): String {
        return "${this::class.simpleName}($id)"
    }
}