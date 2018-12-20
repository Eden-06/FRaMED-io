package io.framed.framework.render.html

import de.westermann.kobserve.ListenerReference
import io.framed.framework.JsPlumbInstance
import io.framed.framework.pictogram.BoxShape
import io.framed.framework.util.async
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

    fun updateAutosize() {
        if (shape.autosize) {
            positionView.autoWidth()
            positionView.autoHeight()

            async {
                shape.width = positionView.clientWidth.toDouble()
                shape.height = positionView.clientHeight.toDouble()
            }
        }
    }

    val positionView = container.listView {
        if (shape.resizeable) {
            val size = if (htmlRenderer.snapToGrid) NavigationView.gridSize else null
            htmlRenderer.resizerList += this.resizeable(size) { event ->
                jsPlumbInstance.revalidate(view.html)

                shape.autosize = false
                shape.width = event.width
                shape.height = event.height
            }.also { resizer = it }

            shape.autosizeProperty.onChange {
                if (shape.autosize) {
                    autoWidth()
                    autoHeight()

                    async {
                        shape.width = clientWidth.toDouble()
                        shape.height = clientHeight.toDouble()
                    }
                } else {
                    shape.width = clientWidth.toDouble()
                    shape.height = clientHeight.toDouble()
                    width = shape.width
                    height = shape.height
                }
            }

            if (!shape.autosize) {
                width = shape.width
                height = shape.height
            }
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
            absolutePosition(positionView, view.html, content.onParentMove)
        } else if (position == BoxShape.Position.BORDER) {
            borderPosition(positionView, view.html, parent?.parent as HtmlBoxShape)

            parentContainer?.onParentMove?.reference {
                jsPlumbInstance.revalidate(view.html)
            }?.let { reference = it }
        }
    }
}