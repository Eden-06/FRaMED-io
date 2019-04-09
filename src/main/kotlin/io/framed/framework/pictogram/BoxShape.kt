package io.framed.framework.pictogram

import de.westermann.kobserve.EventHandler
import io.framed.framework.Linker

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

            renderShape(shape)

            onAdd.emit(shape)
        }
    }

    operator fun minusAssign(shape: Shape) = remove(shape)
    fun remove(shape: Shape) {
        if (shape in internalShapes) {
            internalShapes -= shape
            shape.parent = null
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

    fun leftOffset(shape: Shape): Double = leftOffset + when (position) {
        Position.HORIZONTAL -> {
            var sum = 0.0
            for (s in internalShapes) {
                if (s == shape) {
                    break
                }
                sum += s.width
            }
            sum
        }
        else -> 0.0
    }

    fun topOffset(shape: Shape): Double = topOffset + when (position) {
        Position.VERTICAL -> {
            var sum = 0.0
            for (s in internalShapes) {
                if (s == shape) {
                    break
                }
                sum += s.height
            }
            sum
        }
        else -> 0.0
    }

    private var ignoreRender = true
    fun stopRender() {
        ignoreRender = true

        for (shape in shapes) {
            if (shape is BoxShape) {
                shape.stopRender()
            }
        }
    }

    fun render() {
        ignoreRender = false

        for (shape in shapes) {
            renderShape(shape)
        }
    }

    private fun renderShape(shape: Shape) {
        if (ignoreRender) return

        shape.parent = this

        if (shape.layerProperty.isBound) {
            shape.layerProperty.unbind()
        }
        shape.layerProperty.bind(layerProperty)

        if (shape is BoxShape) {
            shape.render()
        }
    }
}

fun Linker<*, *>.boxShape(position: BoxShape.Position = BoxShape.Position.VERTICAL, id: Long = this.id, init: BoxShape.() -> Unit) =
        BoxShape(id).also(init).also { it.position = position }

fun BoxShape.boxShape(position: BoxShape.Position = BoxShape.Position.VERTICAL, init: BoxShape.() -> Unit) =
        BoxShape(null).also(init).also(this::add).also { it.position = position }
