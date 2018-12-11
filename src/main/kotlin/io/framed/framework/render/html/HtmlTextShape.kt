package io.framed.framework.render.html

import io.framed.framework.pictogram.TextShape
import io.framed.framework.view.View
import io.framed.framework.view.ViewCollection
import io.framed.framework.view.inputView

class HtmlTextShape(
        htmlRenderer: HtmlRenderer,
        override val shape: TextShape,
        container: ViewCollection<View<*>, *>,
        parent: HtmlShape?
) : HtmlShape(shape, htmlRenderer, container, null, parent) {

    override val view: View<*> = container.inputView(shape.property) {
        style(this, shape.style)
        events(this, shape)

        autocomplete = shape.autocomplete

        onMouseDown {
            focus()
        }
    }
}
