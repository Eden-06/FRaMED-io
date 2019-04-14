package io.framed.framework.render.html

import de.westermann.kobserve.event.EventListener
import io.framed.framework.pictogram.Label
import io.framed.framework.pictogram.Shape
import io.framed.framework.util.Point
import io.framed.framework.util.async
import io.framed.framework.view.InputView
import io.framed.framework.view.Root
import io.framed.framework.view.View
import io.framed.framework.view.ViewCollection

class HtmlLabel(
        private val htmlRenderer: HtmlRenderer,
        val label: Label,
        val parent: ViewCollection<View<*>, *>,
        boundShape: Shape? = null
) {

    val listeners = mutableListOf<EventListener<*>>()

    fun remove() {
        println("Remove label")
        for (reference in listeners) {
            reference.detach()
        }
        listeners.clear()
        parent -= view
    }

    val view: View<*> = InputView(label.textProperty).apply {
        classes += "html-label"
        autocomplete(label.autocomplete)
        sizeMatchText()

        marginLeft = label.leftProperty.value
        marginTop = label.topProperty.value

        listeners += label.leftProperty.onChange.reference {
            marginLeft = label.leftProperty.value
        }
        listeners += label.topProperty.onChange.reference {
            marginTop = label.topProperty.value
        }

        if (boundShape != null) {
            left = boundShape.left
            top = boundShape.top
        }

        allowDrag = true
        onMouseDown { event ->
            event.stopPropagation()
            dragZoom = htmlRenderer.zoom

            var markView = true
            async(200) {
                if (markView) {
                    htmlRenderer.directDragView(View.DragEvent(Point.ZERO, true), this, parent)
                }
            }

            var reference: EventListener<*>? = null
            reference = Root.onMouseUp.reference {
                markView = false
                reference?.detach()
                htmlRenderer.stopDragView()
            }
        }
        onClick { event ->
            event.stopPropagation()
        }
        onDrag { e ->
            val event = htmlRenderer.directDragView(e, this, parent)

            label.leftProperty.value = marginLeft + event.delta.x
            label.topProperty.value = marginTop + event.delta.y
        }
    }

    init {
        println("create label")
        if (boundShape != null) {
            listeners += boundShape.topProperty.onChange.reference {
                view.top = boundShape.top
            }
            listeners += boundShape.leftProperty.onChange.reference {
                view.left = boundShape.left
            }
        }
    }
}
