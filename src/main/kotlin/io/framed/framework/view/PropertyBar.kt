package io.framed.framework.view

import io.framed.framework.util.EventHandler
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.events.MouseEvent
import kotlin.math.max

class PropertyBar : ViewCollection<View<*>, HTMLDivElement>("div") {

    val onResize = EventHandler<Double>()

    val content: ListView

    init {
        classes += "property-bar"
        listView {
            classes += "property-bar-resizer"

            var up: ((MouseEvent) -> Unit) = {}

            onMouseDown {
                it.preventDefault()
                Root.classes += "resize-ew"
                val move = Root.onMouseMove { e ->
                    e.preventDefault()
                    val deltaWidth = e.clientX.toDouble() - this@PropertyBar.offsetLeft - clientWidth
                    val currentWidth = this@PropertyBar.clientWidth.toDouble()
                    val width = currentWidth - deltaWidth

                    val newWidth = if (width < 50) {
                        this@PropertyBar.classes += "hide"
                        8.0
                    } else {
                        this@PropertyBar.classes -= "hide"
                        max(width, 150.0)
                    }
                    this@PropertyBar.width = newWidth
                    onResize.fire(newWidth)
                }
                up = Root.onMouseUp { e ->
                    e.preventDefault()
                    Root.classes -= "resize-ew"
                    Root.onMouseMove.removeListener(move)
                    Root.onMouseUp.removeListener(up)
                }
            }
        }
        content = listView { }
    }
}

fun ViewCollection<in PropertyBar, *>.propertyBar(init: PropertyBar.() -> Unit) =
        PropertyBar().also(this::append).also(init)