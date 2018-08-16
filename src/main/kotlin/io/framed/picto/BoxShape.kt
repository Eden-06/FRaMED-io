package io.framed.picto

import io.framed.util.EventHandler

/**
 * @author lars
 */
class BoxShape() : Shape() {

    enum class Position {
        ABSOLUTE, HORIZONTAL, VERTICAL
    }

    var position = Position.VERTICAL

    var shapes: List<Shape> = emptyList()
        private set

    val onAdd = EventHandler<Shape>()
    val onRemove = EventHandler<Shape>()

    override fun setLayer(layer: Layer) {
        super.setLayer(layer)
        shapes.forEach { it.setLayer(layer) }
    }

    operator fun plusAssign(shape: Shape) = add(shape)
    fun add(shape: Shape) {
        shapes += shape
        shape.setLayer(layer)
        onAdd.fire(shape)
    }

    operator fun minusAssign(shape: Shape) = remove(shape)
    fun remove(shape: Shape) {
        shapes -= shape
        onRemove.fire(shape)
    }

    operator fun Shape.unaryPlus() = add(this)
}

fun boxShape(init: BoxShape.() -> Unit) = BoxShape().also(init)