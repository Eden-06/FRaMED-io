package io.framed.framework

import io.framed.framework.pictogram.BoxShape
import io.framed.framework.pictogram.Shape
import io.framed.framework.util.EventHandler
import io.framed.framework.util.Property

/**
 * @author lars
 */
interface ModelLinker<M : ModelElement<M>, P : Shape, R : Shape> : PreviewLinker<M, P, R> {
    val nameProperty: Property<String>
    val name: String

    val shapeLinkers: Set<ShapeLinker<*, *>>

    val connectionManager: ConnectionManager

    val container: BoxShape

    fun getShapeById(id: Long): Shape? =
            shapeLinkers.find { it.id == id }?.pictogram

    fun getIdByShape(shape: Shape): Long? =
            getLinkerByShape(shape)?.id

    fun getLinkerByShape(shape: Shape): ShapeLinker<*, *>? =
            shapeLinkers.find { it.pictogram == shape }

    val setPosition: EventHandler<SetPosition>

    fun redraw(linker: ShapeLinker<*, *>)

    fun canDropShape(shape: Shape, target: Shape): Boolean {
        val shapeLinker = shapeLinkers.find { it.pictogram == shape } ?: return false
        val targetLinker = shapeLinkers.find { it.pictogram == target } ?: return false

        return LinkerManager.linkerItemList.find { shapeLinker in it }?.canCreate(targetLinker) ?: false
    }

    fun dropShape(shape: Shape, target: Shape)
}