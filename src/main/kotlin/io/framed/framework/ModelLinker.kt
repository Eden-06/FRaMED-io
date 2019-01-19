package io.framed.framework

import de.westermann.kobserve.Property
import io.framed.framework.pictogram.BoxShape
import io.framed.framework.pictogram.Shape

/**
 * @author lars
 */
interface ModelLinker<M : ModelElement<M>, P : Shape, R : Shape> : PreviewLinker<M, P, R> {

    override val nameProperty: Property<String>

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
        for (linker in shapeLinkers) {
            if (linker.id in shapes) {
                linker.delete()
            } else if (linker is ModelLinker<*, *, *>) {
                linker.delete(shapes)
            }
        }
    }

    fun copy(shapes: List<Long>): List<ModelElement<*>> {
        var elements = emptyList<ModelElement<*>>()
        for (linker in shapeLinkers) {
            if (linker.id in shapes) {
                elements += linker.model.copy()
            } else if (linker is ModelLinker<*, *, *>) {
                elements += linker.copy(shapes)
            }
        }
        return elements
    }

    fun cut(shapes: List<Long>): List<ModelElement<*>> {
        val elements = copy(shapes)
        delete(shapes)
        return elements
    }

    fun paste(target: Long?, elements: List<ModelElement<*>>) {
        if (target == null || id == target) {
            for (element in elements) {
                add(element)
            }
        } else {
            for (linker in shapeLinkers) {
                if (linker is ModelLinker<*, *, *>) {
                    linker.paste(target, elements)
                } else if (linker.id == target) {
                    for (element in elements) {
                        add(element)
                    }
                    break
                }
            }
        }
    }
}