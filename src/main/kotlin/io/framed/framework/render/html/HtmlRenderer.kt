package io.framed.framework.render.html

import de.westermann.kobserve.EventHandler
import de.westermann.kobserve.ListenerReference
import de.westermann.kobserve.basic.property
import io.framed.framework.JsPlumbInstance
import io.framed.framework.pictogram.ContextEvent
import io.framed.framework.pictogram.Shape
import io.framed.framework.pictogram.SidebarEvent
import io.framed.framework.pictogram.ViewModel
import io.framed.framework.render.Renderer
import io.framed.framework.util.*
import io.framed.framework.view.*
import org.w3c.dom.get
import org.w3c.dom.set
import kotlin.math.abs
import kotlin.browser.window

/**
 * @author lars
 */
class HtmlRenderer(
        private val workspace: ListView
) : Renderer {
    lateinit var viewModel: ViewModel

    private var layerChangeListener: ListenerReference<Unit>? = null

    val snapToGridProperty = property(window.localStorage["snap-to-grid"]?.toBoolean() ?: true).also { property ->
        property.onChange { _ ->
            incrementSnap = null
            if (property.value) {
                resizerList.forEach { it.stepSize = NavigationView.gridSize }
            } else {
                resizerList.forEach { it.stepSize = null }
            }

            window.localStorage["snap-to-grid"] = property.value.toString()
        }
    }
    var snapToGrid by snapToGridProperty

    val snapToViewProperty = property(window.localStorage["snap-to-view"]?.toBoolean() ?: false).also { property ->
        property.onChange {
            incrementSnap = null
            window.localStorage["snap-to-view"] = property.value.toString()
        }
    }
    var snapToView by snapToViewProperty

    override fun render(viewModel: ViewModel) {
        layerChangeListener?.remove()

        if (this::htmlConnections.isInitialized) {
            htmlConnections.remove()
        }

        this.viewModel = viewModel

        layerChangeListener = this.viewModel.onLayerChange.reference { draw() }
        layerChangeListener?.trigger(Unit)
    }

    lateinit var htmlShape: HtmlShapeContainer
    lateinit var htmlConnections: HtmlConnections

    var resizerList: List<ResizeHandler> = emptyList()
    var draggableViews: List<View<*>> = emptyList()
    val selectedViews: List<View<*>>
        get() = draggableViews.filter { it.selectedView }

    val selectedViewSizeProperty = property(0)

    val navigationView = NavigationView().also { navigationView ->
        workspace += navigationView

        navigationView.onSelect { dimension ->
            if (dimension == null) {
                selectedViews.forEach { it.selectedView = false }
            } else {
                draggableViews.forEach {
                    it.selectedView = (it.dimension in dimension)
                }
            }
            selectedViewSizeProperty.value = selectedViews.size
        }

        navigationView.onZoom { zoom ->
            draggableViews.forEach { it.dragZoom = zoom }
            updateViewBox()
        }
        navigationView.onPan {
            updateViewBox()
        }
    }

    fun selectView(view: View<*>, ctrlKey: Boolean, dblClick: Boolean) {
        if (ctrlKey) {
            view.selectedView = !view.selectedView
        } else {
            if (view.selectedView) {
                if (dblClick) {
                    (selectedViews - view).forEach { it.selectedView = false }
                }
            } else {
                (selectedViews - view).forEach { it.selectedView = false }
                view.selectedView = true
            }
        }
        selectedViewSizeProperty.value = selectedViews.size
    }

    fun selectAll() {
        draggableViews.forEach { it.selectedView = true }
        selectedViewSizeProperty.value = selectedViews.size
    }

    fun deselectAll() {
        selectedViews.forEach { it.selectedView = false }
        selectedViewSizeProperty.value = selectedViews.size
    }

    fun deleteSelected() {
        if (selectedViews.isNotEmpty()) {
            History.group("Delete views") {
                shapeMap.filterValues { it.viewList.any { it in selectedViews } }.keys.forEach {
                    it.delete?.invoke()
                }
            }
        }
    }

    fun stopDragView() {
        navigationView.clearLine()
        draggableViews.forEach { it.classes -= "snap-view" }
    }

    private var incrementSnap: Pair<View<*>, Point>? = null
    fun directDragView(event: View.DragEvent, view: View<*>, parent: ViewCollection<View<*>, *>): View.DragEvent {
        var delta = event.delta

        val drawSnapLine = parent == navigationView.container
        if (drawSnapLine) navigationView.clearLine()
        draggableViews.forEach { it.classes -= "snap-view" }

        if (incrementSnap?.first != view) incrementSnap = null
        val currentCenter = Point(view.left, view.top)

        val center = (incrementSnap?.second ?: currentCenter) + delta
        incrementSnap = view to center

        val gridSize = NavigationView.gridSize

        if (snapToGrid) {

            // Test x
            val tx = center.x % gridSize
            val dx = if (tx <= gridSize / 2) -tx else gridSize - tx

            if (abs(dx) < gridSize) {
                val pos = center.x + dx
                delta = Point(pos - currentCenter.x, delta.y)
            }

            // Test y
            val ty = center.y % gridSize
            val dy = if (ty <= gridSize / 2) -ty else gridSize - ty

            if (abs(dy) < gridSize) {
                val pos = center.y + dy
                delta = Point(delta.x, pos - currentCenter.y)
            }
        } else {
            delta = center - currentCenter
        }

        if (snapToView) {
            val threshold = gridSize / 2
            val otherViews = (draggableViews - view - selectedViews)
                    .filter { it in parent }
                    .flatMap {
                        listOf(
                                it to Point(it.left, it.top),
                                it to Point(it.left + it.width, it.top),
                                it to Point(it.left, it.top + it.height),
                                it to Point(it.left + it.width, it.top + it.height),
                                it to Point(it.left + it.width / 2, it.top + it.height / 2)
                        )
                    }

            // Test x center
            val foundX = otherViews
                    .flatMap {
                        listOf(
                                SnapHelper(it.first, it.second, abs(it.second.x - (center.x)), currentCenter.x),
                                SnapHelper(it.first, it.second, abs(it.second.x - (center.x + view.width / 2)), currentCenter.x + view.width / 2),
                                SnapHelper(it.first, it.second, abs(it.second.x - (center.x + view.width)), currentCenter.x + view.width)
                        )
                    }
                    .filter { it.delta < threshold }
            if (foundX.isNotEmpty()) {
                val (_, min, _, source) = foundX.minBy { it.delta } ?: foundX.first()
                delta = Point(min.x - source, delta.y)

                if (drawSnapLine) navigationView.vLine(min.x)

                foundX.filter {
                    it.targetPoint.x == min.x
                }.forEach {
                    it.view.classes += "snap-view"
                }
            }

            // Test y center
            val foundY = otherViews
                    .flatMap {
                        listOf(
                                SnapHelper(it.first, it.second, abs(it.second.y - (center.y)), currentCenter.y),
                                SnapHelper(it.first, it.second, abs(it.second.y - (center.y + view.height / 2)), currentCenter.y + view.height / 2),
                                SnapHelper(it.first, it.second, abs(it.second.y - (center.y + view.height)), currentCenter.y + view.height)
                        )
                    }
                    .filter { it.delta < threshold }
            if (foundY.isNotEmpty()) {
                val (_, min, _, source) = foundY.minBy { it.delta } ?: foundY.first()
                delta = Point(delta.x, min.y - source)

                if (drawSnapLine) navigationView.hLine(min.y)

                foundY.filter {
                    it.targetPoint.y == min.y
                }.forEach {
                    it.view.classes += "snap-view"
                }
            }
        }

        return event.copy(delta = delta)
    }

    private fun updateViewBox() {
        val box = navigationView.viewBox

        viewModel.container.left = box.left
        viewModel.container.top = box.top
        viewModel.container.width = box.width
        viewModel.container.height = box.height
    }

    private fun loadViewBox() {
        val left = viewModel.container.left
        val top = viewModel.container.top
        val width = viewModel.container.width
        val height = viewModel.container.height

        if (left != null && top != null) {
            navigationView.viewBox = Dimension(left, top, width, height)
        }
    }

    fun draw() {
        loadViewBox()

        viewModel.container.onPositionChange.clearListeners()
        viewModel.container.onPositionChange { force ->
            if (force) loadViewBox()
        }

        navigationView.container.clear()

        navigationView.onContext.clearListeners()
        if (viewModel.container.hasContextMenu) {
            navigationView.onContext {
                viewModel.container.onContextMenu.emit(ContextEvent(it.point(), viewModel.container))
            }
        }
        navigationView.onMouseDown.clearListeners()
        if (viewModel.container.hasSidebar) {
            navigationView.onMouseDown {
                viewModel.container.onSidebar.emit(SidebarEvent(viewModel.container))
            }
        }

        if (this::htmlConnections.isInitialized) {
            htmlConnections.remove()
        }
        if (this::htmlShape.isInitialized) {
            htmlShape.remove()
        }
        draggableViews = emptyList()
        resizerList = emptyList()

        htmlConnections = HtmlConnections(this, viewModel)

        htmlShape = HtmlShapeContainer(
                this,
                viewModel.container,
                navigationView.container
        )

        htmlConnections.init()
    }

    operator fun get(id: Long, jsPlumbInstance: JsPlumbInstance): View<*>? = shapeMap
            .filter { it.key.id == id && it.value.jsPlumbInstance == jsPlumbInstance }.values.firstOrNull()?.view

    fun getShapeById(id: String): Shape? = shapeMap.entries.find { (_, item) ->
        item.view.id == id
    }?.key

    val shapeMap = mutableMapOf<Shape, HtmlShape>()

    fun checkDrop(shape: Shape? = null): Shape? {
        shapeMap.values.filter { "drop-target" in it.view.classes }.forEach {
            it.view.classes -= "drop-target"
        }

        if (shape != null) {
            val mouse = navigationView.mouseToCanvas(Root.mousePosition)
            val targets = shapeMap.filter { (s, view) ->
                mouse in (view.view.dimension + Point(view.view.offsetLeft - workspace.offsetLeft, view.view.offsetTop - workspace.offsetTop)) && shape != s
            }.filterKeys { target ->
                viewModel.handler.canDropShape(shape.id ?: return null, target.id ?: return null)
            }

            if (targets.size == 1) {
                val (target, view) = targets.entries.first()
                view.view.classes += "drop-target"
                return target
            }
        }
        return null
    }

    override var zoom: Double
        get() = navigationView.zoom
        set(value) {
            navigationView.zoomTo(value)
        }

    override val onZoom: EventHandler<Double> = navigationView.onZoom

    override fun panTo(point: Point) {
        navigationView.panTo(point)
    }
}

data class SnapHelper(
        val view: View<*>,
        val targetPoint: Point,
        val delta: Double,
        val sourcePoint: Double
)