package io.framed.framework.render.html

import de.westermann.kobserve.ListenerReference
import io.framed.framework.JsPlumbInstance
import io.framed.framework.pictogram.*
import io.framed.framework.util.async
import io.framed.framework.util.point
import io.framed.framework.view.View
import io.framed.framework.view.ViewCollection

abstract class HtmlShape(
        private val htmlRenderer: HtmlRenderer,
        val container: ViewCollection<View<*>, *>,
        open val jsPlumbInstance: JsPlumbInstance?,
        val parent: HtmlShape?
) {
    abstract val view: View<*>
    open fun remove() {
        container -= view

        listeners.forEach { it.remove() }
        listeners.clear()
    }

    val listeners = mutableListOf<ListenerReference<*>>()

    fun events(view: View<*>, shape: Shape, parent: ViewCollection<View<*>, *>) {
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

    fun absolutePosition(view: View<*>, shape: Shape, parent: ViewCollection<View<*>, *>, jsPlumbInstance: JsPlumbInstance) = with(view) {
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

                jsPlumbInstance.revalidate(html)
            }

        }?.let(listeners::add)

        dragType = View.DragType.ABSOLUTE
        onMouseDown { event ->
            event.stopPropagation()
            htmlRenderer.selectView(this, event.ctrlKey, false)
            parent.toForeground(view)
        }
        onClick { event ->
            event.stopPropagation()
        }
        onDblClick { event ->
            event.stopPropagation()
            htmlRenderer.selectView(this, event.ctrlKey, true)
        }
        onDrag { event ->
            if (event.direct) {
                (htmlRenderer.selectedViews - this).forEach {
                    it.performDrag(event.indirect)
                }
            }
            jsPlumbInstance.revalidate(html)

            shape.left = event.newPosition.x
            shape.top = event.newPosition.y

            canDrop = htmlRenderer.checkDrop(shape)
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

    companion object {
        fun create(
                htmlRenderer: HtmlRenderer,
                shape: Shape,
                container: ViewCollection<View<*>, *>,
                position: BoxShape.Position,
                jsPlumbInstance: JsPlumbInstance,
                parent: HtmlShape?
        ): HtmlShape = when (shape) {
            is BoxShape -> HtmlBoxShape(htmlRenderer, shape, container, position, jsPlumbInstance, parent)
            is TextShape -> HtmlTextShape(htmlRenderer, shape, container, parent)
            is IconShape -> HtmlIconShape(htmlRenderer, shape, container, position, jsPlumbInstance, parent)
            else -> throw UnsupportedOperationException()
        }
    }
}
