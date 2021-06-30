package io.framed.framework.render.html

import io.framed.framework.JsPlumbInstance
import io.framed.framework.pictogram.BoxShape
import io.framed.framework.util.Point
import io.framed.framework.util.async
import io.framed.framework.view.View
import io.framed.framework.view.ViewCollection
import io.framed.framework.view.listView
import io.framed.framework.view.resizeable
import kotlin.math.max

class HtmlBoxShape(
        htmlRenderer: HtmlRenderer,
        override val shape: BoxShape,
        parent: HtmlContentShape?,
        parentContainer: HtmlShapeContainer,
        container: ViewCollection<View<*>, *>,
        val position: BoxShape.Position,
        override val jsPlumbInstance: JsPlumbInstance
) : HtmlContentShape(htmlRenderer, shape, parent, parentContainer, container, jsPlumbInstance) {

    override val positionView = container.listView {
        if (shape.style.notch) {
            classes += "notch"
        }
        if (shape.style.stretchHeight) {
            html.style.height = "100%"
        }

        events(this, shape)
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
                                    listOf(this@HtmlBoxShape)
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
                            listOf(this@HtmlBoxShape)
                    )
                    val snappedSize = snap.point - event.position

                    shape.autosize = false

                    val width = max(snappedSize.x, 100.0)
                    val height = max(snappedSize.y, 40.0)

                    shape.width = width
                    shape.height = height
                    this@listView.width = width
                    this@listView.height = height
                    revalidate()

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

            if (shape.resizeable) {
                width = shape.width
                height = shape.height
            }
        }

        onDblClick {
            if (!shape.onAction.isEmpty()) {
                shape.onAction.emit(Unit)
                it.stopPropagation()
            }
        }
    }

    override val view: ViewCollection<View<*>, *> = positionView.listView {
        classes += "content-view"
        style(this, shape.style)
        events(this, shape)

        if (shape.style.flex) {
            classes += "flex-row"
        }

        if (shape.style.overflow) {
            classes += "popup-overflow"
        }

        if (shape.position == BoxShape.Position.ABSOLUTE) {
            html.style.position = "relative"
            html.style.overflowX = "visible"
            html.style.overflowY = "visible"
        }else if (shape.position == BoxShape.Position.HORIZONTAL) {
            classes += "horizontal-view"
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
    }

    override val viewList: List<View<*>> = listOf(view, positionView)

    init {
        if (position == BoxShape.Position.ABSOLUTE) {
            absolutePosition(positionView, content.onParentMove)
        } else if (position == BoxShape.Position.BORDER) {
            borderPosition(positionView, parent?.parent as HtmlBoxShape)
        }

        init()
    }
}
