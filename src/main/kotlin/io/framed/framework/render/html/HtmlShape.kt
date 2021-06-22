package io.framed.framework.render.html

import de.westermann.kobserve.event.EventListener
import io.framed.framework.JsPlumbInstance
import io.framed.framework.pictogram.*
import io.framed.framework.util.Dimension
import io.framed.framework.util.Point
import io.framed.framework.util.async
import io.framed.framework.util.point
import io.framed.framework.view.ResizeHandler
import io.framed.framework.view.View
import io.framed.framework.view.ViewCollection

abstract class HtmlShape(
        val htmlRenderer: HtmlRenderer,
        open val shape: Shape,
        val parent: HtmlContentShape?,
        parentContainer: HtmlShapeContainer,
        val container: ViewCollection<View<*>, *>,
        open val jsPlumbInstance: JsPlumbInstance?
) : Selectable {
    abstract val view: View<*>

    abstract val viewList: List<View<*>>

    val listeners = mutableListOf<EventListener<*>>()

    var resizer: ResizeHandler? = null

    fun events(view: View<*>, shape: Shape) {
        if (shape.hasContextMenu) {
            view.onContext {
                val diagram = htmlRenderer.snapPoint(htmlRenderer.navigationView.mouseToCanvas(it.point())).point
                shape.onContextMenu.emit(ContextEvent(it.point(), diagram, shape))
            }
        }
        if (shape.hasSidebar) {
            view.onMouseDown {
                shape.onSidebar.emit(SidebarEvent(shape))
            }
            resizer?.onMouseDown?.addListener {
                shape.onSidebar.emit(SidebarEvent(shape))
            }
            view.onClick { it.stopPropagation() }
        }
    }

    fun style(view: View<*>, style: Style) {
        if (!style.topNotch) {
            style.background?.let { bg ->
                view.html.style.background = bg.toCss()
            }
        }

        val parentStyle = shape.parent?.style
        if (parentStyle != null && parentStyle.topNotch) {
            parentStyle.background?.let { bg ->
                view.html.style.background = bg.toCss()
            }
        }

        if (style.text_center) {
            view.html.style.textAlign = "center"
        }


        style.border?.let { border ->
            view.html.style.borderStyle = border.style.toString()
            view.html.style.borderWidth = border.width.toCss("px")
            view.html.style.borderColor = border.color.toCss()
            border.radius?.let { radius ->
                view.html.style.borderRadius = radius.toCss("px")
            }
            view.classes["double-border"] = border.double
            view.classes["left-double-border"] = border.leftDoubleBar
        }
        style.padding?.let { padding ->
            view.html.style.padding = padding.toCss("px")
        }
    }

    fun revalidate() {
        async {
            htmlRenderer.htmlConnections.revalidate(view.html)
        }
    }

    open fun remove() {
        container -= view

        for (reference in listeners) {
            reference.detach()
        }
        listeners.clear()

        for (label in labels) {
            label.remove()
        }
        labels = emptySet()

        reference?.detach()
    }

    var labels: Set<HtmlLabel> = emptySet()

    private fun initLabels() {
        for (label in labels) {
            label.remove()
        }
        if (shape.ignoreLabels) {
            labels = emptySet()
            return
        }
        labels = shape.labels.map { label ->
            val htmlLabel = HtmlLabel(htmlRenderer, label, container, shape)
            container += htmlLabel.view

            htmlLabel.focusProperty.bind(positionView.selectedViewProperty)

            htmlLabel
        }.toSet()
    }

    private var reference: EventListener<*>? = null

    abstract override val positionView: View<*>

    override val id: Long
        get() = shape.id!!
    override val pictogram: Pictogram
        get() = shape
    override val left: Double
        get() = shape.leftOffset
    override val top: Double
        get() = shape.topOffset
    override val width: Double
        get() = shape.width
    override val height: Double
        get() = shape.height

    override fun select() {
        positionView.selectedView = true
        shape.onSidebar.emit(SidebarEvent(shape))
    }

    override fun unselect() {
        positionView.selectedView = false
    }

    override fun selectArea(area: Dimension) {
        positionView.selectedView = positionView.dimension in area
    }

    override val isSelected: Boolean
        get() = positionView.selectedView
    override val isDraggable: Boolean = true

    override fun drag(delta: Point) {
        positionView.performDrag(View.DragEvent(delta, false))
    }

    override fun setZoom(zoom: Double) {
        positionView.dragZoom = zoom
    }

    override fun highlightSnap() {
        positionView.classes["snap-view"] = true
    }

    override fun unhighlightSnap() {
        positionView.classes["snap-view"] = false
    }

    override fun isChildOf(container: ViewCollection<View<*>, *>): Boolean {
        return positionView in container
    }

    fun init() {
        listeners += shape.visibleProperty.onChange.reference {
            view.display = shape.visible
        }
        view.display = shape.visible

        shape.layerProperty.onChange {
            initLabels()
        }
        shape.labelsProperty.onChange {
            initLabels()
        }
        initLabels()
    }

    init {
        reference = parentContainer.onParentMove.reference {
            revalidate()
        }
    }
}
