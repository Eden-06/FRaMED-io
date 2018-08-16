package io.framed.render.html

import io.framed.JsPlumb
import io.framed.JsPlumbInstance
import io.framed.picto.*
import io.framed.render.Renderer
import io.framed.util.point
import io.framed.view.*

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

    override fun render(viewModel: ViewModel) {
        this.viewModel.onLayerChange -= layerChangeListener

        this.viewModel = viewModel

        this.viewModel.onLayerChange += layerChangeListener
        draw()
    }

    private var draggableViews: List<View<*>> = emptyList()
    private val selectedViews: List<View<*>>
        get() = draggableViews.filter { it.selectedView }

    private fun draw() {
        workspace.clear()
        draggableViews = emptyList()

        val navigationView = NavigationView()
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

        if (viewModel.container.hasContext) {
            navigationView.onContext {
                viewModel.container.onContext.fire(it.point())
            }
        }
        if (viewModel.container.hasSidebar) {
            navigationView.onMouseDown {
                viewModel.container.onSidebar.fire(Unit)
            }
        }

        val jsPlumbInstance = JsPlumb.getInstance().apply {
            setContainer(navigationView.container.html)

            setZoom(1.0)
        }
        navigationView.onZoom { zoom ->
            jsPlumbInstance.setZoom(zoom)
            draggableViews.forEach { it.dragZoom = zoom }
        }

        viewModel.container.shapes.forEach {
            drawShape(it, navigationView.container, BoxShape.Position.ABSOLUTE, jsPlumbInstance)
        }

        viewModel.relations.forEach {
            drawRelation(it, jsPlumbInstance)
        }
        viewModel.onRelationAdd {
            drawRelation(it, jsPlumbInstance)
        }
        viewModel.onRelationRemove { r->
            relations[r]?.let { relation ->
                relations -= r
                relation.remove()
            }
        }
    }

    operator fun get(shape: Shape): View<*> = shapeMap[shape] ?: throw IllegalArgumentException()

    private var shapeMap: Map<Shape, View<*>> = emptyMap()

    var relations: Map<Relation, HtmlRelation> = emptyMap()
    private fun drawRelation(relation: Relation, jsPlumbInstance: JsPlumbInstance) {
        relations += relation to HtmlRelation(relation, jsPlumbInstance, this)
    }

    private fun drawShape(shape: Shape, parent: ViewCollection<View<*>, *>, position: BoxShape.Position, jsPlumbInstance: JsPlumbInstance): View<*> {
        return when (shape) {
            is BoxShape -> drawBoxShape(shape, parent, position, jsPlumbInstance)
            is TextShape -> drawTextShape(shape, parent)
            else -> throw UnsupportedOperationException()
        }.also {
            shapeMap += shape to it
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
                shape.onContext.fire(it.point())
            }
        }
        if (shape.hasSidebar) {
            view.onMouseDown {
                if (!it.defaultPrevented) {
                    it.preventDefault()
                    shape.onSidebar.fire(Unit)
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
}