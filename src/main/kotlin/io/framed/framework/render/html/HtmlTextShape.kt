package io.framed.framework.render.html

import de.westermann.kobserve.ListenerReference
import io.framed.framework.JsPlumbInstance
import io.framed.framework.pictogram.TextShape
import io.framed.framework.view.View
import io.framed.framework.view.ViewCollection
import io.framed.framework.view.inputView
import kotlin.browser.document

class HtmlTextShape(
        htmlRenderer: HtmlRenderer,
        override val shape: TextShape,
        parent: HtmlContentShape?,
        parentContainer: HtmlShapeContainer?,
        container: ViewCollection<View<*>, *>,
        override val jsPlumbInstance: JsPlumbInstance
) : HtmlShape(htmlRenderer, shape, parent, parentContainer, container, jsPlumbInstance) {

    override val view: View<*> = container.inputView(shape.property) {
        style(this, shape.style)
        events(this, shape)

        autocomplete = shape.autocomplete

        onMouseDown {
            focus()
        }
        onFocusEnter {
            val downEvent = document.createEvent("MouseEvents")
            downEvent.initEvent("mousedown", true, true);
            html.dispatchEvent(downEvent)
            val upEvent = document.createEvent("MouseEvents")
            upEvent.initEvent("mouseup", true, true);
            html.dispatchEvent(upEvent)
        }
        onFocusLeave {
            value = shape.property.value
        }
    }

    override val viewList: List<View<*>> = listOf(view)

    override fun remove() {
        super.remove()
        reference?.remove()
    }

    var reference: ListenerReference<*>? = null

    init {
        parentContainer?.onParentMove?.reference {
            jsPlumbInstance.revalidate(view.html)
        }?.let { reference = it }
    }
}
