package io.framed.framework.pictogram

import de.westermann.kobserve.EventHandler
import io.framed.framework.Dagre
import io.framed.framework.DagreGraph
import io.framed.framework.Linker
import io.framed.framework.util.History

/**
 * @author lars
 */
class BoxShape(id: Long?) : Shape(id) {

    enum class Position {
        ABSOLUTE, HORIZONTAL, VERTICAL, BORDER
    }

    var position = Position.VERTICAL
    var resizeable = false

    val shapes: List<Shape>
        get() = internalShapes

    private var internalShapes: List<Shape> = emptyList()

    val onAdd = EventHandler<Shape>()
    val onRemove = EventHandler<Shape>()

    operator fun plusAssign(shape: Shape) = add(shape)
    fun add(shape: Shape) {
        if (shape !in internalShapes) {
            internalShapes += shape
            shape.layerProperty.bind(layerProperty)
            onAdd.emit(shape)
        }
    }

    operator fun minusAssign(shape: Shape) = remove(shape)
    fun remove(shape: Shape) {
        if (shape in internalShapes) {
            internalShapes -= shape
            if (shape.layerProperty.isBound) {
                shape.layerProperty.unbind()
            }
            onRemove.emit(shape)
        }
    }

    fun clear() {
        shapes.forEach(this::remove)
    }

    operator fun Shape.unaryPlus() = add(this)
}

fun Linker<*, *>.boxShape(position: BoxShape.Position = BoxShape.Position.VERTICAL, id: Long = this.id, init: BoxShape.() -> Unit) =
        BoxShape(id).also(init).also { it.position = position }

fun BoxShape.boxShape(position: BoxShape.Position = BoxShape.Position.VERTICAL, init: BoxShape.() -> Unit) =
        BoxShape(null).also(init).also(this::add).also { it.position = position }