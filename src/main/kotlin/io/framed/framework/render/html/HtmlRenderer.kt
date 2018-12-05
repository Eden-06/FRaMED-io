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
import io.framed.framework.util.Dimension
import io.framed.framework.util.Point
import io.framed.framework.util.point
import io.framed.framework.view.*
import kotlin.math.abs

/**
 * @author lars
 */
class HtmlRenderer(
        val workspace: ListView
) : Renderer {
    lateinit var viewModel: ViewModel

    var layerChangeListener: ListenerReference<Unit>? = null

    val snapTypeProperty = property(SnapType.GRID).also {
        it.onChange {
            incrementSnap = null
        }
    }
    var snapType by snapTypeProperty

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

    var draggableViews: List<View<*>> = emptyList()
    val selectedViews: List<View<*>>
        get() = draggableViews.filter { it.selectedView }

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

        when (snapType) {
            SnapType.GRID -> {
                if (incrementSnap?.first != view) incrementSnap = null
                val currentCenter = Point(view.left, view.top)

                var (_, center) = incrementSnap ?: view to currentCenter
                center += delta

                val gridSize = NavigationView.gridSize

                // Test x
                val tx = center.x % gridSize
                val dx = if (tx <= gridSize / 2) -tx else gridSize - tx

                if (abs(dx) < 8) {
                    val pos = center.x + dx
                    delta = Point(pos - currentCenter.x, delta.y)

                    if (drawSnapLine) navigationView.vLine(pos)
                }

                // Test y
                val ty = center.y % gridSize
                val dy = if (ty <= gridSize / 2) -ty else gridSize - ty

                if (abs(dy) < 8) {
                    val pos = center.y + dy
                    delta = Point(delta.x, pos - currentCenter.y)

                    if (drawSnapLine) navigationView.hLine(pos)
                }

                incrementSnap = view to center
            }
            SnapType.CENTER -> {
                if (incrementSnap?.first != view) incrementSnap = null
                val currentCenter = Point(view.left + view.width / 2, view.top + view.height / 2)

                var (_, center) = incrementSnap ?: view to currentCenter
                center += delta

                val otherViews = (draggableViews - view - selectedViews).associate { it to Point(it.left + it.width / 2, it.top + it.height / 2) }

                // Test x center
                val foundX = otherViews.filterValues { abs(it.x - center.x) < 16 }
                if (foundX.isNotEmpty()) {
                    val (_, min) = foundX.minBy { abs(it.value.x - center.x) } ?: foundX.entries.first()
                    delta = Point(min.x - currentCenter.x, delta.y)

                    if (drawSnapLine) navigationView.vLine(min.x)

                    foundX.filterValues {
                        it.x == min.x
                    }.keys.forEach {
                        it.classes += "snap-view"
                    }
                }

                // Test y center
                val foundY = otherViews.filterValues { abs(it.y - center.y) < 16 }
                if (foundY.isNotEmpty()) {
                    val (_, min) = foundY.minBy { abs(it.value.y - center.y) } ?: foundY.entries.first()
                    delta = Point(delta.x, min.y - currentCenter.y)

                    if (drawSnapLine) navigationView.hLine(min.y)

                    foundY.filterValues {
                        it.y == min.y
                    }.keys.forEach {
                        it.classes += "snap-view"
                    }
                }
                incrementSnap = view to center
            }

            SnapType.NONE -> {
                incrementSnap = null
            }
        }


        return event.copy(delta = delta)
    }

    fun updateViewBox() {
        val box = navigationView.viewBox

        viewModel.container.left = box.left
        viewModel.container.top = box.top
        viewModel.container.width = box.width
        viewModel.container.height = box.height
    }

    fun loadViewBox() {
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

        htmlConnections = HtmlConnections(this, viewModel)

        htmlShape = HtmlShapeContainer(
                this,
                viewModel.container,
                navigationView.container
        )

        htmlConnections.init()
    }

    operator fun get(id: Long, jsPlumbInstance: JsPlumbInstance, findParent: Boolean = false): View<*>? = shapeMap
            .filter { it.key.id == id }.values.firstOrNull()?.let {
        if (findParent) {
            var item: HtmlShape? = it
            while (item != null) {
                if (item.jsPlumbInstance == jsPlumbInstance) {
                    return@let item
                }
                item = item.parent
            }
            null
        } else {
            if (it.jsPlumbInstance == jsPlumbInstance) {
                it
            } else null
        }
    }?.view

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
                mouse in view.view.dimension && shape != s
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

enum class SnapType {
    NONE, GRID, CENTER
}