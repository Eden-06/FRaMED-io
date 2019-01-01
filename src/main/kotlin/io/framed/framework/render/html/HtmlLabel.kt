package io.framed.framework.render.html

import de.westermann.kobserve.ListenerReference
import io.framed.framework.pictogram.TextShape
import io.framed.framework.util.Point
import io.framed.framework.util.async
import io.framed.framework.view.*

class HtmlLabel(
        private val htmlRenderer: HtmlRenderer,
        val shape: TextShape,
        val parent: ViewCollection<View<*>, *>
) {

    val listeners = mutableListOf<ListenerReference<*>>()

    fun remove() {
        for (reference in listeners) {
            reference.remove()
        }
        listeners.clear()
    }

    val view: View<*> = InputView(shape.property).apply {
        classes += "html-label"
        autocomplete = shape.autocomplete
        sizeMatchText()

        marginLeft = shape.left
        marginTop = shape.top

        shape.leftProperty.onChange.reference {
            marginLeft = shape.left
        }?.let(listeners::add)
        shape.topProperty.onChange.reference {
            marginTop = shape.top
        }?.let(listeners::add)

        allowDrag = true
        onMouseDown { event ->
            event.stopPropagation()
            htmlRenderer.deselectAll()
            parent.toForeground(this)
            dragZoom = htmlRenderer.zoom

            var markView = true
            async(200) {
                if (markView) {
                    htmlRenderer.directDragView(View.DragEvent(Point.ZERO, true), this, parent)
                }
            }

            var reference: ListenerReference<*>? = null
            reference = Root.onMouseUp.reference {
                markView = false
                reference?.remove()
                htmlRenderer.stopDragView()
            }
        }
        onClick { event ->
            event.stopPropagation()
        }
        onDblClick { event ->
            event.stopPropagation()
            htmlRenderer.selectView(this, event.ctrlKey, true)
        }
        onDrag { e ->
            val event = htmlRenderer.directDragView(e, this, parent)

            shape.left = marginLeft + event.delta.x
            shape.top = marginTop + event.delta.y

            if (event.direct) {
                (htmlRenderer.selectedViews - this).forEach {
                    it.performDrag(event.indirect)
                }
            }
        }
    }
}
