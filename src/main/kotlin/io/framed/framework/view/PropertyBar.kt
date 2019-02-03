package io.framed.framework.view

import de.westermann.kobserve.EventHandler
import de.westermann.kobserve.ListenerReference
import io.framed.framework.util.async
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.events.MouseEvent
import kotlin.math.max

class PropertyBar : ViewCollection<View<*>, HTMLDivElement>("div") {

    val onResize = EventHandler<Double>()

    lateinit var content: ListView

    init {
        classes += "property-bar"
        listView {
            classes += "property-bar-resizer"

            val listeners: MutableList<ListenerReference<*>> = mutableListOf()

            onMouseDown {
                it.preventDefault()
                Root.classes += "resize-ew"
                Root.onMouseMove.reference { e ->
                    e.preventDefault()
                    val deltaWidth = e.clientX.toDouble() - this@PropertyBar.offsetLeft - clientWidth
                    val currentWidth = this@PropertyBar.clientWidth.toDouble()
                    val width = currentWidth - deltaWidth

                    val newWidth = if (width < if (this@PropertyBar.width <= REDUCED_WIDTH) GROW_WIDTH else SHRINK_WIDTH) {
                        this@PropertyBar.classes += "hide"
                        0.0
                    } else {
                        this@PropertyBar.classes -= "hide"
                        max(width, MIN_WIDTH)
                    }

                    this@PropertyBar.width = newWidth
                    onResize.emit(newWidth)
                }?.let(listeners::add)

                val l = { e: MouseEvent ->
                    e.preventDefault()
                    Root.classes -= "resize-ew"
                    for (l in listeners) {
                        l.remove()
                    }
                    listeners.clear()
                }

                Root.onMouseUp.reference(l)?.let(listeners::add)
                Root.onMouseLeave.reference(l)?.let(listeners::add)
            }
        }
        iconView(MaterialIcon.EXPAND_LESS) {
            var lastWidth = GROW_WIDTH.toDouble()
            onClick {
                this@PropertyBar.classes += "animate"
                async {
                    val w = this@PropertyBar.clientWidth.toDouble()
                    if ("hide" !in this@PropertyBar.classes && w >= GROW_WIDTH) {
                        lastWidth = w
                    }

                    this@PropertyBar.classes.toggle("hide")

                    async {
                        var width = this@PropertyBar.clientWidth.toDouble()
                        if ("hide" !in this@PropertyBar.classes && width < GROW_WIDTH) {
                            width = lastWidth
                        }
                        onResize.emit(width)
                    }

                    async(300) {
                        this@PropertyBar.classes -= "animate"
                    }
                }
            }
        }
        listView {
            content = listView { }
        }
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