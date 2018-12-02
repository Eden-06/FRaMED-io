package io.framed.framework.render.html

import de.westermann.kobserve.EventHandler
import de.westermann.kobserve.ListenerReference
import io.framed.framework.JsPlumbInstance
import io.framed.framework.pictogram.ContextEvent
import io.framed.framework.pictogram.Shape
import io.framed.framework.pictogram.SidebarEvent
import io.framed.framework.pictogram.ViewModel
import io.framed.framework.render.Renderer
import io.framed.framework.util.Dimension
import io.framed.framework.util.Point
import io.framed.framework.util.point
import io.framed.framework.view.ListView
import io.framed.framework.view.NavigationView
import io.framed.framework.view.Root
import io.framed.framework.view.View

/**
 * @author lars
 */
class HtmlRenderer(
        val workspace: ListView
) : Renderer {
    lateinit var viewModel: ViewModel

    var layerChangeListener: ListenerReference<Unit>? = null

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