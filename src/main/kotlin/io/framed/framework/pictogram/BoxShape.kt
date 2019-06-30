package io.framed.framework.pictogram

import de.westermann.kobserve.event.EventHandler
import de.westermann.kobserve.event.EventListener
import io.framed.framework.linker.Linker
import kotlin.math.max

/**
 * @author lars
 */
class BoxShape(id: Long?) : Shape(id) {

    enum class Position {
        ABSOLUTE, HORIZONTAL, VERTICAL, BORDER
    }

    var position = Position.VERTICAL

    private var internalShapes: List<Shape> = emptyList()
    private val shapeMap: MutableMap<Shape, List<EventListener<*>>> = mutableMapOf()

    val shapes: List<Shape>
        get() = internalShapes

    val onAdd = EventHandler<Shape>()
    val onRemove = EventHandler<Shape>()
    val onAction = EventHandler<Unit>()

    operator fun plusAssign(shape: Shape) = add(shape)
    fun add(shape: Shape) {
        if (shape !in internalShapes) {
            internalShapes += shape

            shapeMap[shape] = listOf(
                    shape.widthProperty.onChange.reference(this::onChildChanged),
                    shape.heightProperty.onChange.reference(this::onChildChanged),
                    shape.visibleProperty.onChange.reference(this::onChildChanged)
            )

            renderShape(shape)

            onAdd.emit(shape)
        }
    }

    operator fun minusAssign(shape: Shape) = remove(shape)
    fun remove(shape: Shape) {
        if (shape in internalShapes) {
            internalShapes -= shape
            shapeMap.remove(shape)?.forEach { it.detach() }
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

    val onAutoSize = EventHandler<Unit>()
    fun autoSize(allowDownsize: Boolean = false) {
        if (ignoreRender || shapes.any { layer != it.layer }) return

        val verticalPadding = (style.padding?.left ?: 0.0) + (style.padding?.right ?: 0.0)
        val horizontalPadding = (style.padding?.top ?: 0.0) + (style.padding?.bottom ?: 0.0)

        var newHeight: Double
        var newWidth: Double

        when (position) {
            Position.ABSOLUTE -> {
                var maxWidth = 80.0
                var maxHeight = 0.0

                for (shape in shapes) {
                    maxHeight = max(maxHeight, shape.top + shape.height)
                    maxWidth = max(maxWidth, shape.left + shape.width)
                }

                newHeight = maxHeight
                newWidth = maxWidth
            }
            Position.HORIZONTAL -> {
                for (shape in shapes) {
                    if (shape is BoxShape) {
                        shape.autoSize(allowDownsize)
                    }
                }

                newHeight = shapes.filter { it.visible }.maxBy { it.height }?.height ?: 0.0
                newWidth = shapes.filter { it.visible }.sumByDouble { it.width }
            }
            Position.VERTICAL -> {
                for (shape in shapes) {
                    if (shape is BoxShape) {
                        shape.autoSize(allowDownsize)
                    }
                }

                newHeight = shapes.filter { it.visible }.sumByDouble { it.height }
                newWidth = shapes.filter { it.visible }.maxBy { it.width }?.width ?: 0.0
            }
            Position.BORDER -> {
                return
            }
        }

        newHeight += verticalPadding
        newWidth += horizontalPadding

        if (newHeight > height || allowDownsize) {
            height = newHeight
        }
        if (newWidth > width || allowDownsize) {
            width = newWidth
        }
    }

    private fun onChildChanged(@Suppress("UNUSED_PARAMETER") unit: Unit) {
        onAutoSize.emit(Unit)
        if (!resizeable && parent != null) {
            autoSize(true)
        }
    }

    fun leftOffset(shape: Shape): Double = leftOffset + when (position) {
        Position.HORIZONTAL -> {
            var sum = 0.0
            for (s in shapes) {
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
            for (s in shapes) {
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
    val ignore
        get() = ignoreRender

    val onRender = EventHandler<Unit>()

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
        onRender.emit(Unit)

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
