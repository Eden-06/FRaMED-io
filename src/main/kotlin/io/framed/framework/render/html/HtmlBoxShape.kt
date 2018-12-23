package io.framed.framework.render.html

import de.westermann.kobserve.ListenerReference
import io.framed.framework.JsPlumbInstance
import io.framed.framework.pictogram.BoxShape
import io.framed.framework.util.Point
import io.framed.framework.util.async
import io.framed.framework.view.View
import io.framed.framework.view.ViewCollection
import io.framed.framework.view.listView
import io.framed.framework.view.resizeable

class HtmlBoxShape(
        htmlRenderer: HtmlRenderer,
        override val shape: BoxShape,
        parent: HtmlContentShape?,
        parentContainer: HtmlShapeContainer?,
        container: ViewCollection<View<*>, *>,
        val position: BoxShape.Position,
        override val jsPlumbInstance: JsPlumbInstance
) : HtmlContentShape(htmlRenderer, shape, parent, parentContainer, container, jsPlumbInstance) {

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
            resizer = resizeable {
                var markView: Boolean
                onResizeStart {
                    markView = true
                    async(200) {
                        if (markView) {
                            val snap = htmlRenderer.snapPoint(
                                    Point(this@listView.left + this@listView.width, this@listView.top + this@listView.height),
                                    SnapDirection.BOTH,
                                    container,
                                    listOf(this@listView)
                            )

                            if (htmlRenderer.snapToView) {
                                htmlRenderer.navigationView.clearLines()
                                if (snap.snapToViewX) {
                                    htmlRenderer.navigationView.vLines(setOf(snap.point.x))
                                }
                                if (snap.snapToViewY) {
                                    htmlRenderer.navigationView.hLines(setOf(snap.point.y))
                                }
                            }
                        }
                    }
                }
                onResize { event ->
                    val snap = htmlRenderer.snapPoint(
                            event.position + event.size,
                            SnapDirection.BOTH,
                            container,
                            listOf(this@listView)
                    )
                    val snappedSize = snap.point - event.position

                    shape.autosize = false
                    shape.width = snappedSize.x
                    shape.height = snappedSize.y
                    this@listView.width = snappedSize.x
                    this@listView.height = snappedSize.y
                    jsPlumbInstance.revalidate(view.html)

                    if (htmlRenderer.snapToView) {
                        htmlRenderer.navigationView.clearLines()
                        if (snap.snapToViewX) {
                            htmlRenderer.navigationView.vLines(setOf(snap.point.x))
                        }
                        if (snap.snapToViewY) {
                            htmlRenderer.navigationView.hLines(setOf(snap.point.y))
                        }
                    }
                }
                onResizeStop {
                    markView = false
                    htmlRenderer.navigationView.clearLines()
                }
            }

            shape.autosizeProperty.onChange {
                if (shape.autosize) {
                    autoWidth()
                    autoHeight()

                    async {
                        shape.width = clientWidth.toDouble()
                        shape.height = clientHeight.toDouble()
                        jsPlumbInstance.revalidate(view.html)
                    }
                } else {
                    shape.width = clientWidth.toDouble()
                    shape.height = clientHeight.toDouble()
                    width = shape.width
                    height = shape.height
                    jsPlumbInstance.revalidate(view.html)
                }
            }

            if (!shape.autosize) {
                width = shape.width
                height = shape.height
            }
        }

        if (shape.position == BoxShape.Position.ABSOLUTE) {
            html.style.height = "100%"
        }
    }

    override val view: ViewCollection<View<*>, *> = positionView.listView {
        classes += "content-view"
        style(this, shape.style)
        events(this, shape)

        if (shape.position == BoxShape.Position.ABSOLUTE) {
            html.style.position = "relative"
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