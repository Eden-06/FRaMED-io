package io.framed.picto

import io.framed.util.EventHandler

/**
 * @author lars
 */
class BoxShape : Shape() {

    enum class Position {
        ABSOLUTE, HORIZONTAL, VERTICAL
    }

    var position = Position.VERTICAL

    var shapes: List<Shape> = emptyList()
        private set

    val onAdd = EventHandler<Shape>()
    val onRemove = EventHandler<Shape>()

    operator fun plusAssign(shape: Shape) = add(shape)
    fun add(shape: Shape) {
        shape.layer = layer
        shapes += shape
        onAdd.fire(shape)
    }

    operator fun minusAssign(shape: Shape) = remove(shape)
    fun remove(shape: Shape) {
        shapes -= shape
        onRemove.fire(shape)
    }

    operator fun Shape.unaryPlus() = add(this)

    init {
        onLayerChange { _ ->
            shapes.forEach { shape ->
                shape.layer = layer
            }
        }
    }
}

fun boxShape(init: BoxShape.() -> Unit) = BoxShape().also(init)
fun BoxShape.boxShape(init: BoxShape.() -> Unit) = BoxShape().also(init).also { this.add(it) }