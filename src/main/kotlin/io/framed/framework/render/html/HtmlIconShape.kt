package io.framed.framework.render.html

import io.framed.framework.JsPlumbInstance
import io.framed.framework.pictogram.BoxShape
import io.framed.framework.pictogram.IconShape
import io.framed.framework.view.View
import io.framed.framework.view.ViewCollection
import io.framed.framework.view.iconView

class HtmlIconShape(
        htmlRenderer: HtmlRenderer,
        override val shape: IconShape,
        container: ViewCollection<View<*>, *>,
        val position: BoxShape.Position,
        override val jsPlumbInstance: JsPlumbInstance,
        parent: HtmlShape?
) : HtmlShape(shape, htmlRenderer, container, jsPlumbInstance, parent) {

    override val view: View<*> = container.iconView(shape.property) {
        style(this, shape.style)
        events(this, shape)

        if (position == BoxShape.Position.ABSOLUTE) {
            absolutePosition(this, shape, container, jsPlumbInstance)
        } else if (position == BoxShape.Position.BORDER) {
            borderPosition(this, shape, container, jsPlumbInstance, parent?.parent!! as HtmlBoxShape)
        }
    }
}