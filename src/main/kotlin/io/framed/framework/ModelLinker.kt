package io.framed.framework

import de.westermann.kobserve.Property
import io.framed.framework.pictogram.BoxShape
import io.framed.framework.pictogram.Shape

/**
 * @author lars
 */
interface ModelLinker<M : ModelElement<M>, P : Shape, R : Shape> : PreviewLinker<M, P, R> {
    val nameProperty: Property<String>
    val name: String

    val shapeLinkers: Set<ShapeLinker<*, *>>

    val connectionManager: ConnectionManager

    val container: BoxShape

    fun getLinkerById(id: Long): ShapeLinker<*, *>? =
            shapeLinkers.find { it.id == id }

    fun redraw(linker: ShapeLinker<*, *>)

    fun canDropShape(element: Long, target: Long): Boolean {
        val shapeLinker = shapeLinkers.find { it.id == element } ?: return false
        val targetLinker = shapeLinkers.find { it.id == target } ?: return false

        return LinkerManager.linkerItemList.find { shapeLinker in it }?.canCreate(targetLinker) ?: false
    }

    fun dropShape(element: Long, target: Long)


    fun delete(shapes: List<Long>) {
        shapeLinkers.filter { it.id in shapes }.forEach { it.delete() }
    }
    fun copy(shapes: List<Long>) {
        throw NotImplementedError("The current container does not support copy!")
    }
    fun cut(shapes: List<Long>) {
        throw NotImplementedError("The current container does not support cut!")
    }
    fun paste(target: Long) {
        throw NotImplementedError("The current container does not support paste!")
    }
}