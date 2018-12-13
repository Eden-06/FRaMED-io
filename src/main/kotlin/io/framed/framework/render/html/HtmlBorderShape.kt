package io.framed.framework.render.html

import io.framed.framework.JsPlumbInstance
import io.framed.framework.pictogram.Box
import io.framed.framework.pictogram.BoxShape
import io.framed.framework.util.async
import io.framed.framework.view.*

class HtmlBorderShape(
        htmlRenderer: HtmlRenderer,
        override val shape: BoxShape,
        parent: HtmlContentShape?,
        parentContainer: HtmlShapeContainer?,
        container: ViewCollection<View<*>, *>,
        val position: BoxShape.Position,
        override val jsPlumbInstance: JsPlumbInstance
) : HtmlContentShape(htmlRenderer, shape, parent, parentContainer, container, jsPlumbInstance) {

    val realContainer: ViewCollection<View<*>, *> = (parent as HtmlBoxShape).positionView

    override val view: ViewCollection<View<*>, *> = realContainer.listView {
        classes += "border-box"
        realContainer.toBackground(this)
    }

    override val content = HtmlShapeContainer(
            htmlRenderer,
            shape,
            view,
            jsPlumbInstance,
            this
    )

    override val viewList: List<View<*>> = listOf(view)

    override fun remove() {
        super.remove()
        content.remove()
        realContainer -= view
    }
}