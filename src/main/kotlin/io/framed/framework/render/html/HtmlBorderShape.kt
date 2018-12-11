package io.framed.framework.render.html

import io.framed.framework.JsPlumbInstance
import io.framed.framework.pictogram.Box
import io.framed.framework.pictogram.BoxShape
import io.framed.framework.util.async
import io.framed.framework.view.*

class HtmlBorderShape(
        htmlRenderer: HtmlRenderer,
        override val shape: BoxShape,
        container: ViewCollection<View<*>, *>,
        val position: BoxShape.Position,
        override val jsPlumbInstance: JsPlumbInstance,
        parent: HtmlShape?
) : HtmlShape(shape, htmlRenderer, container, jsPlumbInstance, parent) {

    val realContainer: ViewCollection<View<*>, *> = (parent as HtmlBoxShape).positionView

    override val view: ViewCollection<View<*>, *> = realContainer.listView {
        classes += "border-box"
        realContainer.toBackground(this)
    }

    val content = HtmlShapeContainer(
            htmlRenderer,
            shape,
            view,
            jsPlumbInstance,
            this
    )

    override fun remove() {
        super.remove()
        content.remove()
        realContainer -= view
    }
}