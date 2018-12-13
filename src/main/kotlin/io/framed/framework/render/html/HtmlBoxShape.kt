package io.framed.framework.render.html

import de.westermann.kobserve.ListenerReference
import io.framed.framework.JsPlumbInstance
import io.framed.framework.pictogram.BoxShape
import io.framed.framework.view.*

class HtmlBoxShape(
        htmlRenderer: HtmlRenderer,
        override val shape: BoxShape,
        parent: HtmlContentShape?,
        parentContainer: HtmlShapeContainer?,
        container: ViewCollection<View<*>, *>,
        val position: BoxShape.Position,
        override val jsPlumbInstance: JsPlumbInstance
) : HtmlContentShape(htmlRenderer, shape, parent, parentContainer, container, jsPlumbInstance) {

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

    override val content = HtmlShapeContainer(
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
        reference?.remove()
    }

    var reference: ListenerReference<*>? = null

    override val viewList: List<View<*>> = listOf(view, positionView)

    init {
        if (position == BoxShape.Position.ABSOLUTE) {
            absolutePosition(positionView, container, jsPlumbInstance, view.html, content.onParentMove)
        } else if (position == BoxShape.Position.BORDER) {
            borderPosition(positionView, container, jsPlumbInstance, view.html, parent?.parent as HtmlBoxShape)

            parentContainer?.onParentMove?.reference {
                jsPlumbInstance.revalidate(view.html)
            }?.let { reference = it }
        }
    }
}