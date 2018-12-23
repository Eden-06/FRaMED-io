package io.framed.framework.render.html

import de.westermann.kobserve.EventHandler
import io.framed.framework.JsPlumbInstance
import io.framed.framework.pictogram.BoxShape
import io.framed.framework.pictogram.IconShape
import io.framed.framework.pictogram.Shape
import io.framed.framework.pictogram.TextShape
import io.framed.framework.view.View
import io.framed.framework.view.ViewCollection

class HtmlShapeContainer(
        private val htmlRenderer: HtmlRenderer,
        private val containerShape: BoxShape,
        private val container: ViewCollection<View<*>, *>,
        jsPlumbInstanceParent: JsPlumbInstance? = null,
        private val parent: HtmlContentShape? = null
) {

    val jsPlumbInstance = if (containerShape.position == BoxShape.Position.ABSOLUTE || jsPlumbInstanceParent == null) {
        println("Create js plumb for")
        console.log(container.html)
        htmlRenderer.htmlConnections.createJsPlumb(container.html)
    } else jsPlumbInstanceParent

    private val shapeMap: MutableMap<Shape, HtmlShape> = mutableMapOf()

    fun create(shape: Shape): HtmlShape = when (shape) {
        is BoxShape -> {
            if (shape.position == BoxShape.Position.BORDER) {
                HtmlBorderShape(htmlRenderer, shape, parent, this, container, containerShape.position, jsPlumbInstance)
            } else {
                HtmlBoxShape(htmlRenderer, shape, parent, this, container, containerShape.position, jsPlumbInstance)
            }
        }
        is TextShape -> HtmlTextShape(htmlRenderer, shape, parent, this, container, jsPlumbInstance)
        is IconShape -> HtmlIconShape(htmlRenderer, shape, parent, this, container, containerShape.position, jsPlumbInstance)
        else -> throw UnsupportedOperationException()
    }.also {
        it.view.html.setAttribute("data-id", shape.id?.toString() ?: "NULL")
    }

    private fun add(shape: Shape) {
        if (shape !in shapeMap) {
            val html = create(shape)
            shapeMap += shape to html
            if (shape.id != null) {
                println("Add shape with id ${shape.id}")
                htmlRenderer.shapeMap += shape to html
            }

            if (html is HtmlContentShape) {
                onParentMove(html.content.onParentMove::emit)
            }

            htmlRenderer.htmlConnections.addShape(shape)

            checkParentAutosize()
        }
    }

    private fun remove(shape: Shape) {
        shapeMap.remove(shape)?.let { html ->
            if (html is HtmlContentShape) {
                onParentMove.removeListener(html.content.onParentMove::emit)
            }
            html.remove()
        }
        println("Remove shape with id ${shape.id}")
        htmlRenderer.shapeMap -= shape
        htmlRenderer.htmlConnections.deleteShape(shape)
        checkParentAutosize()
    }

    private tailrec fun checkParentAutosize(p: HtmlContentShape? = parent) {
        if (p != null) {
            if (p is HtmlBoxShape) {
                val resizer = p.resizer

                if (resizer != null) {
                    if (p.shape.autosize) {
                        p.updateAutosize()
                    }
                }
            }

            checkParentAutosize(p.parent)
        }
    }

    val onParentMove = EventHandler<Shape>()

    init {
        containerShape.shapes.forEach(this::add)
        containerShape.onAdd(this::add)
        containerShape.onRemove(this::remove)
    }

    fun remove() {
        shapeMap.keys.forEach(this::remove)
    }
}