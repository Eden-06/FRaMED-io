package io.framed.framework.render.html

import io.framed.framework.JsPlumbInstance
import io.framed.framework.pictogram.BoxShape
import io.framed.framework.pictogram.TextShape
import io.framed.framework.view.View
import io.framed.framework.view.ViewCollection
import io.framed.framework.view.inputView

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
    }
}
