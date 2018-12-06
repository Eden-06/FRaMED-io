package io.framed.framework.view

import de.westermann.kobserve.EventHandler
import io.framed.framework.util.Point
import org.w3c.dom.HTMLSpanElement
import kotlin.math.roundToInt

class ResizeHandler(
        val target: View<*>,
        var stepSize: Int?
) : View<HTMLSpanElement>("span") {

    private var position: Point = Point.ZERO

    init {
        target.html.append(html)

        dragType = DragType.CUSTOM
        onDrag { event ->
            position += event.delta / target.dragZoom

            val size = stepSize
            if (size == null) {
                target.width = position.x
                target.height = position.y
            } else {
                target.width = ((position.x + size / 2) / size).roundToInt() * size.toDouble()
                target.height = ((position.y + size / 2) / size).roundToInt() * size.toDouble()
            }

            onResize.emit(ResizeEvent(target.width, target.height))
        }
        onMouseDown {
            position = Point(target.width, target.height)
        }
    }

    val onResize = EventHandler<ResizeEvent>()
}

data class ResizeEvent(
        val width: Double,
        val height: Double
)

fun View<*>.resizeable(stepSize: Int? = null, onResize: (ResizeEvent) -> Unit) = ResizeHandler(this, stepSize).also {
    it.onResize += onResize
}