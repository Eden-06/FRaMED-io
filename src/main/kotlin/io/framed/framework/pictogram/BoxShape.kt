package io.framed.framework.pictogram

import io.framed.framework.Linker
import io.framed.framework.util.EventHandler
import io.framed.framework.util.History

/**
 * @author lars
 */
class BoxShape(
        override val id: Long
) : Shape() {

    enum class Position {
        ABSOLUTE, HORIZONTAL, VERTICAL
    }

    var position = Position.VERTICAL
    var resizeable = false

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

    fun autoLayout() {
        if (position == Position.ABSOLUTE) {
            History.group("Auto layout shape $id") {
                var currentTop = 0.0
                shapes.forEach {
                    it.top = currentTop
                    it.left = 100.00
                    println("SET (${it.top}/${it.left})")
                    it.onPositionChange.fire(true)
                    currentTop += it.height ?: 50.0
                }
            }
        }
    }

    init {
        onLayerChange { _ ->
            shapes.forEach { shape ->
                shape.layer = layer
            }
        }
    }
}

fun Linker<*, *>.boxShape(position: BoxShape.Position = BoxShape.Position.VERTICAL, init: BoxShape.() -> Unit) =
        BoxShape(id).also(init).also { it.position = position }

fun BoxShape.boxShape(position: BoxShape.Position = BoxShape.Position.VERTICAL, init: BoxShape.() -> Unit) =
        BoxShape(id).also(init).also(this::add).also { it.position = position }