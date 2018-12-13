package io.framed.framework.render.html

import de.westermann.kobserve.EventHandler
import de.westermann.kobserve.ListenerReference
import io.framed.framework.JsPlumbInstance
import io.framed.framework.pictogram.*
import io.framed.framework.util.Point
import io.framed.framework.util.async
import io.framed.framework.util.point
import io.framed.framework.view.NavigationView
import io.framed.framework.view.Root
import io.framed.framework.view.View
import io.framed.framework.view.ViewCollection
import org.w3c.dom.HTMLElement
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

abstract class HtmlShape(
        val htmlRenderer: HtmlRenderer,
        open val shape: Shape,
        val parent: HtmlContentShape?,
        val parentContainer: HtmlShapeContainer?,
        val container: ViewCollection<View<*>, *>,
        open val jsPlumbInstance: JsPlumbInstance?
) {
    abstract val view: View<*>
    abstract val viewList: List<View<*>>

    open fun remove() {
        container -= view

        listeners.forEach { it.remove() }
        listeners.clear()
    }

    val listeners = mutableListOf<ListenerReference<*>>()

    fun events(view: View<*>, shape: Shape) {
        if (shape.hasContextMenu) {
            view.onContext {
                it.stopPropagation()
                it.preventDefault()
                shape.onContextMenu.emit(ContextEvent(it.point(), shape))
            }
        }
        if (shape.hasSidebar) {
            view.onMouseDown {
                if (!it.defaultPrevented) {
                    it.preventDefault()
                    shape.onSidebar.emit(SidebarEvent(shape))
                }
            }
            view.onClick { it.stopPropagation() }
        }
    }

    fun style(view: View<*>, style: Style) {
        style.background?.let { bg ->
            view.html.style.background = bg.toCss()
        }
        style.border?.let { border ->
            view.html.style.borderStyle = border.style.toString()
            view.html.style.borderWidth = border.width.toCss("px")
            view.html.style.borderColor = border.color.toCss()
            border.radius?.let { radius ->
                view.html.style.borderRadius = radius.toCss("px")
            }
        }
        style.padding?.let { padding ->
            view.html.style.padding = padding.toCss("px")
        }
    }

    fun absolutePosition(
            view: View<*>,
            parent: ViewCollection<View<*>, *>,
            jsPlumbInstance: JsPlumbInstance,
            jsPlumbView: HTMLElement,
            onMove: EventHandler<Shape>? = null
    ) = with(view) {
        left = shape.left ?: 0.0
        top = shape.top ?: 0.0
        classes += "absolute-view"
        var canDrop: Shape? = null

        shape.onPositionChange.reference { force ->
            if (force) async {
                left = shape.left ?: 0.0
                top = shape.top ?: 0.0
                shape.width?.let { width = it } ?: autoWidth()
                shape.height?.let { height = it } ?: autoHeight()

                jsPlumbInstance.revalidate(jsPlumbView)
                onMove?.emit(shape)
            }

        }?.let(listeners::add)

        dragType = View.DragType.CUSTOM
        onMouseDown { event ->
            event.stopPropagation()
            htmlRenderer.selectView(this, event.ctrlKey, false)
            parent.toForeground(view)

            var markView = true
            async(200) {
                if (markView) {
                    htmlRenderer.directDragView(View.DragEvent(Point.ZERO, true), view, parent)
                }
            }

            var reference: ListenerReference<*>? = null
            reference = Root.onMouseUp.reference {
                markView = false
                reference?.remove()
                htmlRenderer.stopDragView()
            }
        }
        onClick { event ->
            event.stopPropagation()
        }
        onDblClick { event ->
            event.stopPropagation()
            htmlRenderer.selectView(this, event.ctrlKey, true)
        }
        onDrag { e ->
            var event = e
            if (event.direct) {
                event = htmlRenderer.directDragView(event, view, parent)
                canDrop = htmlRenderer.checkDrop(shape)
            }

            val newLeft = left + event.delta.x
            val newTop = top + event.delta.y

            left = minLeft?.let { max(it, newLeft) } ?: newLeft
            top = minTop?.let { max(it, newTop) } ?: newTop

            jsPlumbInstance.revalidate(jsPlumbView)
            onMove?.emit(shape)

            shape.left = left
            shape.top = top

            if (event.direct) {
                (htmlRenderer.selectedViews - this).forEach {
                    it.performDrag(event.indirect)
                }
            }

        }
        onMouseUp {
            canDrop?.let { target ->
                htmlRenderer.viewModel.handler.dropShape(shape.id ?: return@onMouseUp, target.id ?: return@onMouseUp)
            }
            htmlRenderer.checkDrop()
        }
        selectedViewProperty.onChange {
            if (selectedView) {
                parent.toForeground(view)
            }
        }
        htmlRenderer.draggableViews += this

        if (parent != htmlRenderer.navigationView.container) {
            minTop = 0.0
            minLeft = 0.0
        }
    }

    fun borderPosition(
            view: View<*>,
            parent: ViewCollection<View<*>, *>,
            jsPlumbInstance: JsPlumbInstance,
            jsPlumbView: HTMLElement,
            parentHtmlBoxShape: HtmlBoxShape
    ) = with(view) {
        left = shape.left ?: 0.0
        top = shape.top ?: 0.0
        classes += "border-view"

        async {
            marginLeft = -clientWidth / 2.0
            marginTop = -clientHeight / 2.0
            onDrag.emit(View.DragEvent(Point.ZERO, false))
        }

        shape.onPositionChange.reference { force ->
            if (force) async {
                left = shape.left ?: 0.0
                top = shape.top ?: 0.0
                shape.width?.let { width = it } ?: autoWidth()
                shape.height?.let { height = it } ?: autoHeight()

                jsPlumbInstance.revalidate(jsPlumbView)
            }

        }?.let(listeners::add)

        val resizer = parentHtmlBoxShape.resizer
        if (resizer != null) {
            resizer.onResize {
                onDrag.emit(View.DragEvent(Point.ZERO, false))
            }
        }

        dragType = View.DragType.CUSTOM
        onMouseDown { event ->
            event.stopPropagation()

            var markView = true
            async(200) {
                if (markView) {
                    htmlRenderer.directDragView(View.DragEvent(Point.ZERO, true), view, parent)
                }
            }

            var reference: ListenerReference<*>? = null
            reference = Root.onMouseUp.reference {
                markView = false
                reference?.remove()
                htmlRenderer.stopDragView()
            }
        }
        onClick { event ->
            event.stopPropagation()
        }
        onDblClick { event ->
            event.stopPropagation()
        }
        onDrag { e ->
            val event = htmlRenderer.directDragView(e, view, parent)
            var newLeft = left + event.delta.x
            var newTop = top + event.delta.y

            val size = if (htmlRenderer.snapToGrid) NavigationView.gridSize else null
            if (size != null) {
                newLeft = ((newLeft + size / 2) / size).roundToInt() * size.toDouble()
                newTop = ((newTop + size / 2) / size).roundToInt() * size.toDouble()
            }

            val parentWidth = parent.clientWidth
            val parentHeight = parent.clientHeight

            val xDiff = min(abs(newLeft), abs(parentWidth - newLeft))
            val yDiff = min(abs(newTop), abs(parentHeight - newTop))

            if (xDiff < yDiff) {
                newLeft = if (newLeft <= parentWidth / 2) 0.0 else parentWidth.toDouble()
                newTop = min(parentHeight.toDouble(), max(0.0, newTop))
            } else {
                newLeft = min(parentWidth.toDouble(), max(0.0, newLeft))
                newTop = if (newTop <= parentHeight / 2) 0.0 else parentHeight.toDouble()
            }

            left = newLeft
            top = newTop

            jsPlumbInstance.revalidate(jsPlumbView)

            shape.left = left
            shape.top = top
        }
    }
}
