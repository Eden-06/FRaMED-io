package io.framed.framework.render.html

import de.westermann.kobserve.event.EventHandler
import de.westermann.kobserve.event.EventListener
import io.framed.framework.pictogram.Shape
import io.framed.framework.util.Point
import io.framed.framework.util.async
import io.framed.framework.view.Root
import io.framed.framework.view.View
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.reflect.typeOf


fun HtmlShape.absolutePosition(
        view: View<*>,
        onMove: EventHandler<Shape>? = null
) {
    val htmlShape = this
    with(view) {
        classes += "absolute-view"
        var canDrop: Shape? = null

        left = shape.left
        top = shape.top

        if (shape.resizeable) {
            width = shape.width
            height = shape.height
        } else {
            autoWidth()
            autoHeight()

            async {
                shape.width = clientWidth.toDouble()
                shape.height = clientHeight.toDouble()
            }
        }

        listeners += shape.leftProperty.onChange.reference {
            left = shape.left

            revalidate()
            onMove?.emit(shape)
        }
        listeners += shape.topProperty.onChange.reference {
            top = shape.top

            revalidate()
            onMove?.emit(shape)
        }
        listeners += shape.widthProperty.onChange.reference {
            if (shape.resizeable) {
                width = shape.width
                revalidate()
                onMove?.emit(shape)
            }

        }
        listeners += shape.heightProperty.onChange.reference {
            if (shape.resizeable) {
                height = shape.height
                revalidate()
                onMove?.emit(shape)
            }
        }

        allowDrag = true
        disableDrag = false

        onMouseDown { event ->
            if(!event.target?.asDynamic().nodeName.equals("INPUT")){
                allowDrag = true
                event.stopPropagation()
                htmlRenderer.selectView(htmlShape, event.ctrlKey, false)
                container.toForeground(view)

                var markView = true
                async(200) {
                    if (markView) {
                        htmlRenderer.directDragView(View.DragEvent(Point.ZERO, true), view, container)
                    }
                }

                var reference: EventListener<*>? = null
                reference = Root.onMouseUp.reference {
                    markView = false
                    reference?.detach()
                    htmlRenderer.stopDragView()
                }
            } else {
                allowDrag = false
            }
        }
        onClick { event ->
            event.stopPropagation()
        }
        onDblClick { event ->
            event.stopPropagation()
            htmlRenderer.selectView(htmlShape, event.ctrlKey, true)
        }
        onDrag { e ->
            if(allowDrag){
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
                    (htmlRenderer.selected - htmlShape).forEach {
                        if (it.isDraggable) it.drag(event.delta)
                    }
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
            shape.id?.let { id ->
                val hid = -abs(id)
                val v = htmlRenderer.selectable.find { it.id == hid }
                v?.positionView?.highlightedView = selectedView
            }
        }
        htmlRenderer.selectable += htmlShape
        setZoom(htmlRenderer.zoom)
        assignedShape = shape
        html.setAttribute("data-id", shape.id?.toString() ?: "NULL")

        if (container != htmlRenderer.navigationView.container) {
            minTop = 0.0
            minLeft = 0.0
        }
    }
}

fun HtmlShape.borderPosition(
        view: View<*>,
        parentHtmlBoxShape: HtmlBoxShape
) {
    val htmlShape = this
    with(view) {
        classes += "border-view"

        async {
            marginLeft = -clientWidth / 2.0
            marginTop = -clientHeight / 2.0
            onDrag.emit(View.DragEvent(Point.ZERO, false))

            revalidate()
        }

        left = shape.left
        top = shape.top

        if (shape.resizeable) {
            width = shape.width
            height = shape.height
        } else {
            autoWidth()
            autoHeight()

            async {
                shape.width = clientWidth.toDouble()
                shape.height = clientHeight.toDouble()
            }
        }

        listeners += shape.leftProperty.onChange.reference {
            left = shape.left

            var anchor = emptySet<RelationSide>()
            if (shape.top <= 0.0) anchor += RelationSide.TOP
            if (shape.left <= 0.0) anchor += RelationSide.LEFT
            if (shape.top >= container.clientHeight) anchor += RelationSide.BOTTOM
            if (shape.left >= container.clientWidth) anchor += RelationSide.RIGHT
            if (anchor.isEmpty()) anchor = HtmlRelation.ALL_SIDES
            htmlRenderer.htmlConnections.limitSide(view, anchor)

            revalidate()
        }
        listeners += shape.topProperty.onChange.reference {
            top = shape.top

            var anchor = emptySet<RelationSide>()
            if (shape.top <= 0.0) anchor += RelationSide.TOP
            if (shape.left <= 0.0) anchor += RelationSide.LEFT
            if (shape.top >= container.clientHeight) anchor += RelationSide.BOTTOM
            if (shape.left >= container.clientWidth) anchor += RelationSide.RIGHT
            if (anchor.isEmpty()) anchor = HtmlRelation.ALL_SIDES
            htmlRenderer.htmlConnections.limitSide(view, anchor)

            revalidate()
        }
        listeners += shape.widthProperty.onChange.reference {
            if (shape.resizeable) {
                width = shape.width
                revalidate()
            }
        }
        listeners += shape.heightProperty.onChange.reference {
            if (shape.resizeable) {
                height = shape.height
                revalidate()
            }
        }

        parentHtmlBoxShape.shape.widthProperty.onChange {
            onDrag.emit(View.DragEvent(Point.ZERO, false))
            async {
                onDrag.emit(View.DragEvent(Point.ZERO, false))
            }
        }
        parentHtmlBoxShape.shape.heightProperty.onChange {
            onDrag.emit(View.DragEvent(Point.ZERO, false))
            async {
                onDrag.emit(View.DragEvent(Point.ZERO, false))
            }
        }

        allowDrag = true
        if (!shape.hasContextMenu) {
            onContext { event ->
                shape.id?.let { id ->
                    val hid = abs(id)
                    val v = htmlRenderer.selectable.find { it.id == hid }
                    v?.positionView?.onContext?.emit(event)
                }
            }
        }
        onMouseDown { event ->
            event.stopPropagation()
            htmlRenderer.selectView(htmlShape, event.ctrlKey, false)
            shape.id?.let { id ->
                val hid = abs(id)
                val v = htmlRenderer.selectable.find { it.id == hid }
                v?.select()
            }

            var markView = true
            async(200) {
                if (markView) {
                    htmlRenderer.directDragView(View.DragEvent(Point.ZERO, true), view, container)
                }
            }

            var reference: EventListener<*>? = null
            reference = Root.onMouseUp.reference {
                markView = false
                reference?.detach()
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

            var newLeft = shape.left + event.delta.x
            var newTop = shape.top + event.delta.y

            val parentWidth = container.clientWidth
            val parentHeight = container.clientHeight

            if (parentWidth <= 0 || parentHeight <= 0) {
                return@onDrag
            }

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
            if (shape.top <= 0.0) anchor += RelationSide.TOP
            if (shape.left <= 0.0) anchor += RelationSide.LEFT
            if (shape.top >= container.clientHeight) anchor += RelationSide.BOTTOM
            if (shape.left >= container.clientWidth) anchor += RelationSide.RIGHT
            if (anchor.isEmpty()) anchor = HtmlRelation.ALL_SIDES
            htmlRenderer.htmlConnections.limitSide(view, anchor)
        }

        htmlRenderer.selectable += htmlShape
        setZoom(htmlRenderer.zoom)
        assignedShape = shape
        html.setAttribute("data-id", shape.id?.toString() ?: "NULL")
    }
}
