package io.framed.render.html

import io.framed.JsPlumb
import io.framed.JsPlumbInstance
import io.framed.jsPlumbEndpointOptions
import io.framed.picto.*
import io.framed.render.Renderer
import io.framed.util.Dimension
import io.framed.util.Point
import io.framed.util.point
import io.framed.view.*
import org.w3c.dom.HTMLElement

/**
 * @author lars
 */
class HtmlRenderer(
        private val workspace: ListView
) : Renderer {
    private var viewModel: ViewModel = ViewModel(BoxShape())

    private val layerChangeListener = { _: Unit ->
        draw()
    }

    /**
     * The map stores the endpoints for all shape
     */
    val endpointMap = mutableMapOf<Shape, HTMLElement>()

    /**
     * The method removes the endpoint for the given shape
     */
    fun deleteEndpoint(shape: Shape, instance: JsPlumbInstance){
        val ep = this.endpointMap[shape]
        if(ep != null) {
            instance.deleteEndpoint(ep)
        }
    }

    override fun render(viewModel: ViewModel) {
        this.viewModel.onLayerChange -= layerChangeListener

        this.viewModel = viewModel

        this.viewModel.onLayerChange += layerChangeListener
        draw()
    }

    private var draggableViews: List<View<*>> = emptyList()
    private val selectedViews: List<View<*>>
        get() = draggableViews.filter { it.selectedView }

    private var navigationView = NavigationView()

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

    private fun draw() {
        workspace.clear()
        draggableViews = emptyList()

        navigationView = NavigationView()
        workspace += navigationView

        loadViewBox()

        navigationView.onSelect { dimension ->
            if (dimension == null) {
                selectedViews.forEach { it.selectedView = false }
            } else {
                draggableViews.forEach {
                    it.selectedView = (it.dimension in dimension)
                }
            }
        }

        if (viewModel.container.hasContext) {
            navigationView.onContext {
                viewModel.container.onContext.fire(ContextEvent(it.point(), viewModel.container))
            }
        }
        if (viewModel.container.hasSidebar) {
            navigationView.onMouseDown {
                viewModel.container.onSidebar.fire(SidebarEvent(viewModel.container))
            }
        }

        val jsPlumbInstance = JsPlumb.getInstance().apply {
            setContainer(navigationView.container.html)

            setZoom(1.0)
            bind("beforeDrop") { info: dynamic ->
                val source = getShapeById(info.sourceId as String)
                val target = getShapeById(info.targetId as String)

                if (source != null && target != null) {
                    viewModel.onRelationDraw.fire(source to target)
                }

                return@bind false
            }
        }
        navigationView.onZoom { zoom ->
            jsPlumbInstance.setZoom(zoom)
            draggableViews.forEach { it.dragZoom = zoom }
            updateViewBox()
        }
        navigationView.onPan {
            updateViewBox()
        }

        // Draw children
        var map = viewModel.container.shapes.map {
            it to drawShape(it, navigationView.container, BoxShape.Position.ABSOLUTE, jsPlumbInstance)
        }.toMap()

        viewModel.container.onAdd {
            map += it to drawShape(it, navigationView.container, BoxShape.Position.ABSOLUTE, jsPlumbInstance)
        }
        viewModel.container.onRemove {
            map[it]?.let { v ->
                navigationView.container.remove(v)
            }
        }

        viewModel.connections.forEach {
            drawRelation(it, jsPlumbInstance)
        }
        viewModel.onRelationAdd {
            drawRelation(it, jsPlumbInstance)
        }
        viewModel.onRelationRemove { r ->
            relations[r]?.let { relation ->
                relations -= r
                relation.remove()
            }
        }
    }

    operator fun get(shape: Shape): View<*> = shapeMap[shape] ?: throw IllegalArgumentException()

    private fun getShapeById(id: String): Shape? = shapeMap.entries.find { (_, view) ->
        view.id == id
    }?.key

    private var shapeMap: Map<Shape, View<*>> = emptyMap()

    private var relations: Map<Connection, HtmlRelation> = emptyMap()
    private fun drawRelation(relation: Connection, jsPlumbInstance: JsPlumbInstance) {
        relations += relation to HtmlRelation(relation, jsPlumbInstance, this)
    }

    private fun drawShape(shape: Shape, parent: ViewCollection<View<*>, *>, position: BoxShape.Position, jsPlumbInstance: JsPlumbInstance): View<*> {
        return when (shape) {
            is BoxShape -> drawBoxShape(shape, parent, position, jsPlumbInstance)
            is TextShape -> drawTextShape(shape, parent)
            is IconShape -> drawIconShape(shape, parent)
            else -> throw UnsupportedOperationException()
        }.also { view ->
            shapeMap += shape to view

            if (shape.acceptRelation) {
                val ep = jsPlumbInstance.addEndpoint(view.html, jsPlumbEndpointOptions {
                    anchors = arrayOf("Bottom")
                    isSource = true
                    isTarget = true
                    endpoint = "Dot"
                })
                this.endpointMap[shape] = ep
            }
        }
    }

    private fun style(view: View<*>, style: Style) {
        style.background?.let {
            view.html.style.background = it.toCss()
        }
        style.border?.let {
            view.html.style.border = it.toCss()
            if (it.radius > 0) {
                view.html.style.borderRadius = "${it.radius}px"
            }
        }
    }

    private fun selectView(view: View<*>, ctrlKey: Boolean, dblClick: Boolean) {
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

    private fun events(view: View<*>, shape: Shape) {
        if (shape.hasContext) {
            view.onContext {
                it.stopPropagation()
                it.preventDefault()
                shape.onContext.fire(ContextEvent(it.point(), shape))
            }
        }
        if (shape.hasSidebar) {
            view.onMouseDown {
                if (!it.defaultPrevented) {
                    it.preventDefault()
                    shape.onSidebar.fire(SidebarEvent(shape))
                }
            }
            view.onClick { it.stopPropagation() }
        }
    }

    private fun drawBoxShape(shape: BoxShape, parent: ViewCollection<View<*>, *>, position: BoxShape.Position, jsPlumbInstance: JsPlumbInstance): View<*> {
        val view = ListView()
        parent += view

        style(view, shape.style)
        events(view, shape)

        if (position == BoxShape.Position.ABSOLUTE) {
            view.left = shape.left ?: 0.0
            view.top = shape.top ?: 0.0
            view.classes += "absolute-view"

            shape.onPositionChange { force ->
                if (force) {
                    view.left = shape.left ?: 0.0
                    view.top = shape.top ?: 0.0
                }
            }

            view.draggable = View.DragType.ABSOLUTE
            view.onMouseDown { event ->
                event.stopPropagation()
                selectView(view, event.ctrlKey, false)
            }
            view.onClick { event ->
                event.stopPropagation()
            }
            view.onDblClick { event ->
                event.stopPropagation()
                selectView(view, event.ctrlKey, true)
            }
            view.onDrag { event ->
                if (event.direct) {
                    (selectedViews - view).forEach {
                        it.performDrag(event.indirect)
                    }
                }
                jsPlumbInstance.revalidate(view.html)

                shape.left = event.newPosition.x
                shape.top = event.newPosition.y
            }
            draggableViews += view
        } else {
            view.classes += "content-view"
        }

        var map = shape.shapes.map {
            it to drawShape(it, view, shape.position, jsPlumbInstance)
        }.toMap()

        shape.onAdd {
            map += it to drawShape(it, view, shape.position, jsPlumbInstance)
        }
        shape.onRemove {
            map[it]?.let { v ->
                view.remove(v)
            }
        }
        return view
    }

    private fun drawTextShape(shape: TextShape, parent: ViewCollection<View<*>, *>): View<*> {
        return InputView(shape.property).also { view ->
            style(view, shape.style)
            events(view, shape)

            view.autocomplete = shape.autocomplete

            view.onMouseDown {
                view.focus()
            }

            parent.append(view)
        }
    }

    private fun drawIconShape(shape: IconShape, parent: ViewCollection<View<*>, *>): View<*> {
        return IconView(shape.property).also { view ->
            style(view, shape.style)
            events(view, shape)

            view.onMouseDown {
                view.focus()
            }

            parent.append(view)
        }
    }

    override fun zoomTo(zoom: Double) {
        navigationView.zoomTo(zoom)
    }

    override fun panTo(point: Point) {
        navigationView.panTo(point)
    }
}