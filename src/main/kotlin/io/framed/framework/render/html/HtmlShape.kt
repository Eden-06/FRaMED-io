package io.framed.framework.render.html

import de.westermann.kobserve.EventHandler
import de.westermann.kobserve.ListenerReference
import io.framed.framework.JsPlumbInstance
import io.framed.framework.pictogram.ContextEvent
import io.framed.framework.pictogram.Shape
import io.framed.framework.pictogram.SidebarEvent
import io.framed.framework.pictogram.Style
import io.framed.framework.util.Point
import io.framed.framework.util.async
import io.framed.framework.util.point
import io.framed.framework.view.ResizeHandler
import io.framed.framework.view.Root
import io.framed.framework.view.View
import io.framed.framework.view.ViewCollection
import org.w3c.dom.HTMLElement
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

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

        for (reference in listeners) {
            reference.remove()
        }
        listeners.clear()
    }

    val listeners = mutableListOf<ListenerReference<*>>()

    var resizer: ResizeHandler? = null

    fun events(view: View<*>, shape: Shape) {
        if (shape.hasContextMenu) {
            view.onContext {
                it.stopPropagation()
                it.preventDefault()
                val diagram = htmlRenderer.snapPoint(htmlRenderer.navigationView.mouseToCanvas(it.point())).point
                htmlRenderer
                shape.onContextMenu.emit(ContextEvent(it.point(), diagram, shape))
            }
        }
        if (shape.hasSidebar) {
            view.onMouseDown {
                if (!it.defaultPrevented) {
                    it.preventDefault()
                    shape.onSidebar.emit(SidebarEvent(shape))
                }
            }
            resizer?.onMouseDown?.addListener {
                shape.onSidebar.emit(SidebarEvent(shape))
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
            jsPlumbView: HTMLElement,
            onMove: EventHandler<Shape>? = null
    ) = with(view) {
        classes += "absolute-view"
        var canDrop: Shape? = null

        left = shape.left
        top = shape.top

        if (shape.autosize) {
            autoWidth()
            autoHeight()

            async {
                shape.width = clientWidth.toDouble()
                shape.height = clientHeight.toDouble()
            }
        } else {
            width = shape.width
            height = shape.height
        }

        shape.leftProperty.onChange.reference {
            left = shape.left

            jsPlumbInstance?.revalidate(jsPlumbView)
            onMove?.emit(shape)
        }?.let(listeners::add)
        shape.topProperty.onChange.reference {
            top = shape.top

            jsPlumbInstance?.revalidate(jsPlumbView)
            onMove?.emit(shape)
        }?.let(listeners::add)
        shape.widthProperty.onChange.reference {
            if (shape.autosize) {
                autoWidth()
            } else {
                width = shape.width
            }

            jsPlumbInstance?.revalidate(jsPlumbView)
            onMove?.emit(shape)
        }?.let(listeners::add)
        shape.heightProperty.onChange.reference {
            if (shape.autosize) {
                autoHeight()
            } else {
                height = shape.height
            }

            jsPlumbInstance?.revalidate(jsPlumbView)
            onMove?.emit(shape)
        }?.let(listeners::add)

        allowDrag = true
        onMouseDown { event ->
            event.stopPropagation()
            htmlRenderer.selectView(this, event.ctrlKey, false)
            container.toForeground(view)

            var markView = true
            async(200) {
                if (markView) {
                    htmlRenderer.directDragView(View.DragEvent(Point.ZERO, true), view, container)
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
                event = htmlRenderer.directDragView(event, view, container)
                canDrop = htmlRenderer.checkDrop(shape)
            }

            val newLeft = left + event.delta.x
            val newTop = top + event.delta.y

            shape.left = minLeft?.let { max(it, newLeft) } ?: newLeft
            shape.top = minTop?.let { max(it, newTop) } ?: newTop

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
                container.toForeground(view)
            }
        }
        htmlRenderer.draggableViews += this

        if (container != htmlRenderer.navigationView.container) {
            minTop = 0.0
            minLeft = 0.0
        }
    }

    fun borderPosition(
            view: View<*>,
            jsPlumbView: HTMLElement,
            parentHtmlBoxShape: HtmlBoxShape
    ) = with(view) {
        classes += "border-view"

        async {
            marginLeft = -clientWidth / 2.0
            marginTop = -clientHeight / 2.0
            onDrag.emit(View.DragEvent(Point.ZERO, false))
            jsPlumbInstance?.revalidate(jsPlumbView)
        }

        left = shape.left
        top = shape.top

        if (shape.autosize) {
            autoWidth()
            autoHeight()

            async {
                shape.width = clientWidth.toDouble()
                shape.height = clientHeight.toDouble()
            }
        } else {
            width = shape.width
            height = shape.height
        }

        shape.leftProperty.onChange.reference {
            left = shape.left

            jsPlumbInstance?.revalidate(jsPlumbView)
        }?.let(listeners::add)
        shape.topProperty.onChange.reference {
            top = shape.top

            jsPlumbInstance?.revalidate(jsPlumbView)
        }?.let(listeners::add)
        shape.widthProperty.onChange.reference {
            if (shape.autosize) {
                autoWidth()
            } else {
                width = shape.width
            }

            jsPlumbInstance?.revalidate(jsPlumbView)
        }?.let(listeners::add)
        shape.heightProperty.onChange.reference {
            if (shape.autosize) {
                autoHeight()
            } else {
                height = shape.height
            }

            jsPlumbInstance?.revalidate(jsPlumbView)
        }?.let(listeners::add)

        val resizer = parentHtmlBoxShape.resizer
        resizer?.onResize?.addListener {
            onDrag.emit(View.DragEvent(Point.ZERO, false))
        }

        allowDrag = true
        onMouseDown { event ->
            event.stopPropagation()

            var markView = true
            async(200) {
                if (markView) {
                    htmlRenderer.directDragView(View.DragEvent(Point.ZERO, true), view, container)
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
            val event = htmlRenderer.directDragView(e, view, container)

            var (newLeft, newTop) = htmlRenderer.snapPoint(Point(
                    left + event.delta.x,
                    top + event.delta.y
            ), parent = parentHtmlBoxShape.container).point

            val parentWidth = container.clientWidth
            val parentHeight = container.clientHeight

            val xDiff = min(abs(newLeft), abs(parentWidth - newLeft))
            val yDiff = min(abs(newTop), abs(parentHeight - newTop))

            if (xDiff < yDiff) {
                newLeft = if (newLeft <= parentWidth / 2) 0.0 else parentWidth.toDouble()
                newTop = min(parentHeight.toDouble(), max(0.0, newTop))
            } else {
                newLeft = min(parentWidth.toDouble(), max(0.0, newLeft))
                newTop = if (newTop <= parentHeight / 2) 0.0 else parentHeight.toDouble()
            }

            shape.left = newLeft
            shape.top = newTop

            var anchor = emptySet<RelationSide>()
            if (newTop <= 0.0) anchor += RelationSide.TOP
            if (newLeft <= 0.0) anchor += RelationSide.LEFT
            if (newTop >= parentHeight) anchor += RelationSide.BOTTOM
            if (newLeft >= parentWidth) anchor += RelationSide.RIGHT
            if (anchor.isEmpty()) anchor = HtmlRelation.ALL_SIDES

            htmlRenderer.htmlConnections.limitSide(view, anchor)
        }

        htmlRenderer.draggableViews += this
    }
}
