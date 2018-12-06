package io.framed.framework.render.html

import io.framed.framework.JsPlumbInstance
import io.framed.framework.pictogram.BoxShape
import io.framed.framework.view.*

class HtmlBoxShape(
        htmlRenderer: HtmlRenderer,
        val shape: BoxShape,
        container: ViewCollection<View<*>, *>,
        val position: BoxShape.Position,
        override val jsPlumbInstance: JsPlumbInstance,
        parent: HtmlShape?
) : HtmlShape(htmlRenderer, container, jsPlumbInstance, parent) {

    override val view: ViewCollection<View<*>, *> = container.listView {
        style(this, shape.style)
        events(this, shape, container)

        if (shape.position == BoxShape.Position.ABSOLUTE) {
            this.html.style.position = "relative"
        }

        if (shape.resizeable) {
            val size = if (htmlRenderer.snapToGrid) NavigationView.gridSize else null
            htmlRenderer.resizerList += this.resizeable(size) { event ->
                jsPlumbInstance.revalidate(html)

                shape.width = event.width
                shape.height = event.height
            }

            shape.width?.let { width = it }
            shape.height?.let { height = it }
        }

        if (position == BoxShape.Position.ABSOLUTE) {
            absolutePosition(this, shape, container, jsPlumbInstance)
        }
    }

    val content = HtmlShapeContainer(htmlRenderer, shape, view, jsPlumbInstance, this)

    override fun remove() {
        super.remove()
        content.remove()
    }
}