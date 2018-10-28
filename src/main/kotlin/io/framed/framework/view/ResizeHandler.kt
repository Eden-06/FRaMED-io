package io.framed.framework.view

import io.framed.framework.util.EventHandler
import org.w3c.dom.HTMLSpanElement

class ResizeHandler(val target: View<*>) : View<HTMLSpanElement>("span") {
    init {
        target.html.append(html)

        dragType = DragType.CUSTOM
        onDrag { event ->
            target.width += event.delta.x / target.dragZoom
            target.height += event.delta.y / target.dragZoom

            onResize.fire(ResizeEvent(target.width, target.height))
        }
    }

    val onResize = EventHandler<ResizeEvent>()
}

data class ResizeEvent(
        val width: Double,
        val height: Double
)

fun View<*>.resizeable(onResize: (ResizeEvent) -> Unit) = ResizeHandler(this).also {
    it.onResize += onResize
}