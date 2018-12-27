package io.framed.framework.view

import de.westermann.kobserve.EventHandler
import de.westermann.kobserve.ListenerReference
import org.w3c.dom.HTMLDivElement
import kotlin.math.max

class PropertyBar : ViewCollection<View<*>, HTMLDivElement>("div") {

    val onResize = EventHandler<Double>()

    val content: ListView

    init {
        classes += "property-bar"
        listView {
            classes += "property-bar-resizer"

            var up: ListenerReference<*>? = null

            onMouseDown {
                it.preventDefault()
                Root.classes += "resize-ew"
                val move = Root.onMouseMove.reference { e ->
                    e.preventDefault()
                    val deltaWidth = e.clientX.toDouble() - this@PropertyBar.offsetLeft - clientWidth
                    val currentWidth = this@PropertyBar.clientWidth.toDouble()
                    val width = currentWidth - deltaWidth

                    val newWidth = if (width < if (this@PropertyBar.width <= REDUCED_WIDTH) GROW_WIDTH else SHRINK_WIDTH) {
                        this@PropertyBar.classes += "hide"
                        REDUCED_WIDTH
                    } else {
                        this@PropertyBar.classes -= "hide"
                        max(width, MIN_WIDTH)
                    }

                    this@PropertyBar.width = newWidth
                    onResize.emit(newWidth)
                }!!

                up = Root.onMouseUp.reference { e ->
                    e.preventDefault()
                    Root.classes -= "resize-ew"
                    move.remove()
                    up?.remove()
                }!!
            }
        }
        content = listView { }
    }

    companion object {
        const val MIN_WIDTH = 150.0
        const val REDUCED_WIDTH = 8.0

        const val SHRINK_WIDTH = 50
        const val GROW_WIDTH = 60
    }
}

fun ViewCollection<in PropertyBar, *>.propertyBar(init: PropertyBar.() -> Unit) =
        PropertyBar().also(this::append).also(init)