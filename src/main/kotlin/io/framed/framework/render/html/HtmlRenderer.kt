package io.framed.framework.render.html

import de.westermann.kobserve.event.EventHandler
import de.westermann.kobserve.event.EventListener
import de.westermann.kobserve.property.property
import io.framed.framework.JsPlumbInstance
import io.framed.framework.pictogram.ContextEvent
import io.framed.framework.pictogram.Shape
import io.framed.framework.pictogram.SidebarEvent
import io.framed.framework.pictogram.ViewModel
import io.framed.framework.render.Renderer
import io.framed.framework.util.Dimension
import io.framed.framework.util.History
import io.framed.framework.util.Point
import io.framed.framework.util.point
import io.framed.framework.view.*
import org.w3c.dom.get
import org.w3c.dom.set
import kotlin.browser.window
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * @author lars
 */
class HtmlRenderer(
        private val workspace: ListView
) : Renderer {
    lateinit var viewModel: ViewModel

    private var layerChangeListener: EventListener<Unit>? = null

    val snapToGridProperty = property(window.localStorage["snap-to-grid"]?.toBoolean() ?: true).also { property ->
        property.onChange { _ ->
            incrementSnap = null
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
        layerChangeListener?.detach()
        this.viewModel = viewModel

        viewModel.container.render()

        layerChangeListener = this.viewModel.onLayerChange.reference { draw() }
        layerChangeListener?.emit(Unit)
    }

    lateinit var htmlShape: HtmlShapeContainer
    lateinit var htmlConnections: HtmlConnections

    var selectable: List<Selectable> = emptyList()
    val selected: List<Selectable>
        get() = selectable.filter { it.isSelected }
    val unselected: List<Selectable>
        get() = selectable.filter { !it.isSelected }
    val draggable: List<Selectable>
        get() = selectable.filter { it.isDraggable }

    val selectedViewSizeProperty = property(0)

    val navigationView = NavigationView().also { navigationView ->
        workspace += navigationView

        navigationView.onSelect { dimension ->
            if (dimension == null) {
                selected.forEach { it.unselect() }
            } else {
                selectable.forEach {
                    it.selectArea(dimension)
                }
            }
            selectedViewSizeProperty.value = selectable.size
        }

        navigationView.onZoom { zoom ->
            selectable.forEach { it.setZoom(zoom) }
            updateViewBox()
        }
        navigationView.onPan {
            updateViewBox()
        }
    }

    fun selectView(view: Selectable, ctrlKey: Boolean, dblClick: Boolean) {
        if (ctrlKey) {
            if (view.isSelected) view.unselect() else view.select()
        } else {
            if (view.isSelected) {
                if (dblClick) {
                    (selectable - view).forEach { it.unselect() }
                }
            } else {
                (selectable - view).forEach { it.unselect() }
                view.select()
            }
        }
        selectedViewSizeProperty.value = selected.size
    }

    fun select(idList: List<Long>) {
        for (element in selectable) {
            if (element.id in idList) {
                element.select()
            } else {
                element.unselect()
            }
        }
        selectedViewSizeProperty.value = selected.size
    }

    fun selectAll() {
        selectable.forEach { it.select() }
        selectedViewSizeProperty.value = selected.size
    }

    fun deselectAll() {
        selectable.forEach { it.unselect() }
        selectedViewSizeProperty.value = selected.size
    }

    fun deleteSelected() {
        if (selected.isNotEmpty()) {
            History.group("Delete") {
                viewModel.handler.delete(selected.map { it.id }.distinct())
            }
        }
    }

    fun copySelected() {
        if (selected.isNotEmpty()) {
            viewModel.handler.copy(selected.map { it.id }.distinct())
        }
    }

    fun cutSelected() {
        if (selected.isNotEmpty()) {
            History.group("Cut") {
                viewModel.handler.cut(selected.map { it.id }.distinct())
            }
        }
    }

    fun paste() {
        val selected = selected.let {
            if (it.size == 1) it.first() else null
        }

        History.group("Paste") {
            val idList = viewModel.handler.paste(selected?.id, selected?.pictogram)
            select(idList)
        }
    }

    fun stopDragView() {
        navigationView.clearLines()
        for (it in selectable) {
            it.unhighlightSnap()
        }
    }

    private var incrementSnap: Pair<View<*>, Point>? = null
    fun directDragView(event: View.DragEvent, view: View<*>, parent: ViewCollection<View<*>, *>): View.DragEvent {
        var delta = event.delta

        for (it in selectable) {
            it.unhighlightSnap()
        }

        if (incrementSnap?.first != view) incrementSnap = null
        val currentTopLeft = Point(view.left + view.marginLeft, view.top + view.marginTop)

        val topLeft = (incrementSnap?.second ?: currentTopLeft) + delta
        incrementSnap = view to topLeft

        val visible = navigationView.viewBox.let { it.copy(left = -it.left, top = -it.top) }

        val ignore = selected
        val otherViews = unselected
                .asSequence()
                .filter { it.isChildOf(parent) }
                .map {
                    it to Dimension(
                            it.positionView?.left ?: 0.0,
                            it.positionView?.top ?: 0.0,
                            it.positionView?.width ?: 0.0,
                            it.positionView?.height ?: 0.0
                    )
                }
                .filter {
                    it.second in visible
                }
                .flatMap { (it, dim) ->
                    sequenceOf(
                            it to Point(dim.left, dim.top),
                            it to Point(dim.left + dim.width / 2, dim.top + dim.height / 2),
                            it to Point(dim.left + dim.width, dim.top + dim.height)
                    )
                }.toList()

        val points = listOf(
                topLeft,
                Point(topLeft.x + view.width / 2, topLeft.y + view.height / 2),
                Point(topLeft.x + view.width, topLeft.y + view.height)
        ).map { it to snapPoint(it, SnapDirection.BOTH, parent, ignore, otherViews) }

        val (sourceX, snapX) = points.minBy { (point, result) ->
            if (result.snapToViewX) {
                abs(point.x - result.point.x)
            } else {
                Double.MAX_VALUE
            }
        } ?: points.first()
        val (sourceY, snapY) = points.minBy { (point, result) ->
            if (result.snapToViewY) {
                abs(point.y - result.point.y)
            } else {
                Double.MAX_VALUE
            }
        } ?: points.first()

        val snap = Point(snapX.point.x, snapY.point.y)
        val source = Point(sourceX.x, sourceY.y)

        delta = snap - source + (topLeft - currentTopLeft)

        if (snapToView) {
            var vLines = emptySet<Double>()
            var hLines = emptySet<Double>()

            val newPoints = listOf(
                    snap,
                    Point(snap.x + view.width / 2, snap.y + view.height / 2),
                    Point(snap.x + view.width, snap.y + view.height)
            )
            for (it in newPoints) {
                otherViews.forEach { (v, p) ->
                    if (it.x == p.x) {
                        v.highlightSnap()
                        vLines += p.x
                    }
                    if (it.y == p.y) {
                        v.highlightSnap()
                        hLines += p.y
                    }
                }
            }

            val drawSnapLine = parent == navigationView.container
            if (drawSnapLine) {
                navigationView.vLines(vLines)
                navigationView.hLines(hLines)
            }
        }

        return event.copy(delta = delta)
    }

    fun snapPoint(
            point: Point,
            direction: SnapDirection = SnapDirection.BOTH,
            parent: ViewCollection<View<*>, *>? = null,
            ignore: List<Selectable> = emptyList(),
            snapableViews: List<Pair<Selectable, Point>>? = null
    ): SnapResult {
        var p = point
        var snapToViewX = false
        var snapToViewY = false

        val gridSize = navigationView.gridSize.toDouble()

        if (snapToGrid) {
            p = Point(
                    if (direction != SnapDirection.VERTICAL) (p.x / gridSize).roundToInt() * gridSize else p.x,
                    if (direction != SnapDirection.HORIZONTAL) (p.y / gridSize).roundToInt() * gridSize else p.y
            )
        }

        if (snapToView && parent != null) {
            val threshold = gridSize / 2

            val otherViews = snapableViews ?: (selectable - ignore)
                    .filter { (it.isChildOf(parent)) }
                    .flatMap {
                        listOf(
                                it to Point(it.left, it.top),
                                it to Point(it.left + it.width / 2, it.top + it.height / 2),
                                it to Point(it.left + it.width, it.top + it.height)
                        )
                    }

            var newPx = p.x
            var newPy = p.y

            if (direction != SnapDirection.VERTICAL) {
                // Test x center
                val foundX = otherViews
                        .map { (view, px) ->
                            SnapHelper(view, px, abs(px.x - p.x))
                        }
                        .filter { it.delta < threshold }
                if (foundX.isNotEmpty()) {
                    val (_, min, _) = foundX.minBy { it.delta } ?: foundX.first()
                    newPx = min.x

                    snapToViewX = true
                }
            }

            if (direction != SnapDirection.HORIZONTAL) {
                // Test y center
                val foundY = otherViews
                        .map { (view, py) ->
                            SnapHelper(view, py, abs(py.y - p.y))
                        }
                        .filter { it.delta < threshold }
                if (foundY.isNotEmpty()) {
                    val (_, min, _) = foundY.minBy { it.delta } ?: foundY.first()
                    newPy = min.y

                    snapToViewY = true
                }
            }

            p = Point(newPx, newPy)
        }

        return SnapResult(p, snapToViewX, snapToViewY)
    }

    private fun updateViewBox() {
        val dimension = navigationView.viewBox
        if (viewModel.container.left != dimension.left) {
            viewModel.container.left = dimension.left
        }
        if (viewModel.container.top != dimension.top) {
            viewModel.container.top = dimension.top
        }
        if (viewModel.container.width != dimension.width) {
            viewModel.container.width = dimension.width
        }
        if (viewModel.container.height != dimension.height) {
            viewModel.container.height = dimension.height
        }
    }

    private fun loadViewBox() {
        val dimension = Dimension(viewModel.container.left, viewModel.container.top, viewModel.container.width, viewModel.container.height)
        navigationView.viewBox = dimension
    }

    fun draw() {
        loadViewBox()

        navigationView.container.clear()

        navigationView.onContext.clearListeners()
        if (viewModel.container.hasContextMenu) {
            navigationView.onContext {
                val diagram = snapPoint(navigationView.mouseToCanvas(it.point())).point
                viewModel.container.onContextMenu.emit(ContextEvent(it.point(), diagram, viewModel.container))
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
        selectable = emptyList()

        htmlConnections = HtmlConnections(this, viewModel)

        htmlShape = HtmlShapeContainer(
                this,
                viewModel.container,
                navigationView.container
        )

        htmlConnections.init()

        viewModel.container.onSidebar.emit(SidebarEvent(viewModel.container))
    }

    operator fun get(id: Long, jsPlumbInstance: JsPlumbInstance): View<*>? = shapeMap
            .filter { it.key.id?.let(::abs) == id && it.value.jsPlumbInstance == jsPlumbInstance }
            .values.sortedBy { it.shape.id }.firstOrNull()?.view

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

    override fun panBy(point: Point) {
        navigationView.panBy(point)
    }
}

data class SnapHelper(
        val view: Selectable,
        val targetPoint: Point,
        val delta: Double
)

enum class SnapDirection {
    HORIZONTAL, VERTICAL, BOTH
}

data class SnapResult(
        val point: Point,
        val snapToViewX: Boolean,
        val snapToViewY: Boolean
)
