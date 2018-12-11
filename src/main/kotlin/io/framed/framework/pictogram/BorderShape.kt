package io.framed.framework.pictogram

import de.westermann.kobserve.EventHandler
import io.framed.framework.Linker
import io.framed.framework.util.History

/**
 * @author lars
 */
class BorderShape(
        override val id: Long?
) : Shape() {

    val shapes: List<Shape>
        get() = internalShapes

    private var internalShapes: List<Shape> = emptyList()

    val onAdd = EventHandler<Shape>()
    val onRemove = EventHandler<Shape>()

    operator fun plusAssign(shape: Shape) = add(shape)
    fun add(shape: Shape) {
        if (shape !in internalShapes) {
            internalShapes += shape

            if (hasLayer) {
                shape.layer = layer
            }

            onAdd.emit(shape)
        }
    }

    operator fun minusAssign(shape: Shape) = remove(shape)
    fun remove(shape: Shape) {
        if (shape in internalShapes) {
            internalShapes -= shape
            onRemove.emit(shape)
        }
    }

    fun clear() {
        shapes.forEach(this::remove)
    }

    operator fun Shape.unaryPlus() = add(this)

    init {
        onLayerChange {
            shapes.forEach { shape ->
                shape.layer = layer
            }
        }
    }
}

fun BoxShape.borderShape(init: BorderShape.() -> Unit) =
        BorderShape(id).also(init).also(this::add)