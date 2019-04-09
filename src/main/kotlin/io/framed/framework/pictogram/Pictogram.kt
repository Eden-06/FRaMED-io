package io.framed.framework.pictogram

import de.westermann.kobserve.event.EventHandler
import de.westermann.kobserve.property.flatMapBinding
import de.westermann.kobserve.property.property

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

    val labelsProperty = layerProperty.flatMapBinding { it[id, this].labelsProperty }
    var labels by labelsProperty

    fun data(name: String) = layerProperty.flatMapBinding { it[id, this].data(name) }
    fun import(layerData: LayerData) = layer[id,this].import(layerData)
    fun export() = layer[id,this].export()

    override fun toString(): String {
        return "${this::class.simpleName}($id)"
    }
}