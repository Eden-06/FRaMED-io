package io.framed.framework.pictogram

import de.westermann.kobserve.EventHandler
import io.framed.framework.Linker
import io.framed.framework.util.History

/**
 * @author lars
 */
class BorderShape(id: Long?) : Shape(id) {

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
            shape.layerProperty.unbind()
            onRemove.emit(shape)
        }
    }

    fun clear() {
        shapes.forEach(this::remove)
    }

    operator fun Shape.unaryPlus() = add(this)
}

fun BoxShape.borderShape(init: BorderShape.() -> Unit) =
        BorderShape(id).also(init).also(this::add)