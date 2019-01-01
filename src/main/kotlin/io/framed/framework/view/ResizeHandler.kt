package io.framed.framework.view

import de.westermann.kobserve.EventHandler
import io.framed.framework.util.Point
import org.w3c.dom.HTMLSpanElement
import org.w3c.dom.events.MouseEvent
import kotlin.math.max

class ResizeHandler(
        val target: View<*>
) : View<HTMLSpanElement>("span") {

    private var position: Point = Point.ZERO
    private var size: Point = Point.ZERO

    @Suppress("UNUSED_PARAMETER")
    private fun dragStopListener(event: MouseEvent) {
        Root.onMouseUp.removeListener(this::dragStopListener)
        Root.onMouseLeave.removeListener(this::dragStopListener)
        onResizeStop.emit(Unit)
    }

    init {
        target.html.append(html)

        allowDrag = true
        onDrag { event ->
            size += event.delta / target.dragZoom
            size = Point(max(size.x, 0.0), max(size.y, 0.0))
            onResize.emit(ResizeEvent(position, size, ResizeOrientation.BOTTOM_RIGHT))
        }

        onMouseDown {
            position = Point(target.left, target.top)
            size = Point(target.width, target.height)

            Root.onMouseUp.addListener(this::dragStopListener)
            Root.onMouseLeave.addListener(this::dragStopListener)
            onResizeStart.emit(Unit)
        }
    }

    val onResize = EventHandler<ResizeEvent>()
    val onResizeStart = EventHandler<Unit>()
    val onResizeStop = EventHandler<Unit>()
}

enum class ResizeOrientation(
        val horizontal: Boolean,
        val vertical: Boolean
) {
    TOP(false, true),
    TOP_RIGHT(true, true),
    RIGHT(true, false),
    BOTTOM_RIGHT(true, true),
    BOTTOM(false, true),
    BOTTOM_LEFT(true, true),
    LEFT(true, false),
    TOP_LEFT(true, true)
}

data class ResizeEvent(
        val position: Point,
        val size: Point,
        val orientation: ResizeOrientation
)

fun View<*>.resizeable(init: ResizeHandler.() -> Unit) = ResizeHandler(this).also(init)