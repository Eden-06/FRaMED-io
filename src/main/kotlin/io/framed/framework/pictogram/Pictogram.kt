package io.framed.framework.pictogram

import de.westermann.kobserve.EventHandler

/**
 * @author lars
 */
abstract class Pictogram {

    abstract val id: Long?

    private lateinit var internalLayer: Layer
    var layer: Layer
        get() = internalLayer
        set(value) {
            if (!hasLayer || internalLayer != value) {
                internalLayer = value
                onLayerChange.emit(Unit)
            }
        }

    val hasLayer: Boolean
        get() = this::internalLayer.isInitialized

    val onSidebar = EventHandler<SidebarEvent>()
    val onContextMenu = EventHandler<ContextEvent>()
    val onLayerChange = EventHandler<Unit>()

    var hasSidebar = false
    var hasContextMenu = false

    var delete: (() -> Unit)? = null

    override fun toString(): String {
        return "${this::class.simpleName}($id)"
    }
}