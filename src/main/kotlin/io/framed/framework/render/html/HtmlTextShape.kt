package io.framed.framework.render.html

import io.framed.framework.JsPlumbInstance
import io.framed.framework.pictogram.TextShape
import io.framed.framework.util.async
import io.framed.framework.view.InputView
import io.framed.framework.view.View
import io.framed.framework.view.ViewCollection
import io.framed.framework.view.inputView
import kotlinx.browser.document
import org.w3c.dom.Text
import kotlin.math.max

class HtmlTextShape(
        htmlRenderer: HtmlRenderer,
        override val shape: TextShape,
        parent: HtmlContentShape?,
        parentContainer: HtmlShapeContainer,
        container: ViewCollection<View<*>, *>,
        override val jsPlumbInstance: JsPlumbInstance
) : HtmlShape(htmlRenderer, shape, parent, parentContainer, container, jsPlumbInstance) {

    fun alignment(view: InputView, alignment: TextShape.TextAlignment) {
        when (alignment) {
            TextShape.TextAlignment.LEFT -> view.input.html.style.textAlign = "left"
            TextShape.TextAlignment.RIGHT ->  view.input.html.style.textAlign = "right"
            TextShape.TextAlignment.CENTER ->  view.input.html.style.textAlign = "center"
        }
    }
    override val view: View<*> = container.inputView(shape.property, shape.surround) {
        style(this, shape.style)
        events(this, shape)

        alignment(this, shape.alignment)

        autocomplete(shape.autocomplete)

        onMouseDown {
            focus()
        }

        onFocusEnter {
            if(View.disableDrag){
                val downEvent = document.createEvent("MouseEvents")
                downEvent.initEvent("mousedown", bubbles = true, cancelable = true);
                html.dispatchEvent(downEvent)
                val upEvent = document.createEvent("MouseEvents")
                upEvent.initEvent("mouseup", bubbles = true, cancelable = true);
                html.dispatchEvent(upEvent)
            } else {
                it.preventDefault()
                it.stopPropagation()
                blur()
            }
        }

        onFocusLeave {
            value = shape.property.value
        }

        onChange {
            updateSize()
        }
    }

    private fun updateSize() {
        //TODO The current formula definitely depends on the dpi settings
        val width = max(60, shape.property.value.length * 11 + 24)
        view.html.style.minWidth = "${width}px"
        async {
            shape.width = width.toDouble()
            shape.height = view.clientHeight.toDouble()
        }
    }

    override val positionView: View<*> = view

    override val viewList: List<View<*>> = listOf(view)

    init {
        init()
        updateSize()
    }
}
