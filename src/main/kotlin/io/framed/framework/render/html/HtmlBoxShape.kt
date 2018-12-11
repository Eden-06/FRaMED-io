package io.framed.framework.render.html

import io.framed.framework.JsPlumbInstance
import io.framed.framework.pictogram.BoxShape
import io.framed.framework.view.*

class HtmlBoxShape(
        htmlRenderer: HtmlRenderer,
        override val shape: BoxShape,
        container: ViewCollection<View<*>, *>,
        val position: BoxShape.Position,
        override val jsPlumbInstance: JsPlumbInstance,
        parent: HtmlShape?
) : HtmlShape(shape, htmlRenderer, container, jsPlumbInstance, parent) {

    var resizer: ResizeHandler? = null

    val positionView = container.listView {
        if (shape.resizeable) {
            val size = if (htmlRenderer.snapToGrid) NavigationView.gridSize else null
            htmlRenderer.resizerList += this.resizeable(size) { event ->
                jsPlumbInstance.revalidate(html)

                shape.width = event.width
                shape.height = event.height
            }.also { resizer = it }

            shape.width?.let { width = it }
            shape.height?.let { height = it }
        }

        if (position == BoxShape.Position.ABSOLUTE) {
            absolutePosition(this, shape, container, jsPlumbInstance)
        } else if (position == BoxShape.Position.BORDER) {
            borderPosition(this, shape, container, jsPlumbInstance, parent?.parent!! as HtmlBoxShape)
        }

        if (shape.position == BoxShape.Position.ABSOLUTE) {
            this.html.style.height = "100%"
        }
    }

    override val view: ViewCollection<View<*>, *> = positionView.listView {
        classes += "content-view"
        style(this, shape.style)
        events(this, shape)

        if (shape.position == BoxShape.Position.ABSOLUTE) {
            this.html.style.position = "relative"
        }
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
        container -= positionView
    }
}