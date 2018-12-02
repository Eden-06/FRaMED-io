package io.framed.framework.render.html

import io.framed.framework.JsPlumbInstance
import io.framed.framework.pictogram.BoxShape
import io.framed.framework.pictogram.Shape
import io.framed.framework.view.View
import io.framed.framework.view.ViewCollection

class HtmlShapeContainer(
        private val htmlRenderer: HtmlRenderer,
        private val containerShape: BoxShape,
        private val container: ViewCollection<View<*>, *>,
        jsPlumbInstanceParent: JsPlumbInstance? = null,
        private val parent: HtmlShape? = null
) {

    val jsPlumbInstance = if (containerShape.position == BoxShape.Position.ABSOLUTE || jsPlumbInstanceParent == null) {
        htmlRenderer.htmlConnections.createJsPlumb(container.html)
    } else jsPlumbInstanceParent

    private val shapeMap: MutableMap<Shape, HtmlShape> = mutableMapOf()

    private fun add(shape: Shape) {
        if (shape !in shapeMap) {
            val html = HtmlShape.create(htmlRenderer, shape, container, containerShape.position, jsPlumbInstance, parent)
            shapeMap += shape to html
            if (shape.id != null) {
                htmlRenderer.shapeMap += shape to html
            }

            htmlRenderer.htmlConnections.addShape(shape)
        } else {
        }
    }

    private fun remove(shape: Shape) {
        shapeMap.remove(shape)?.remove()
        htmlRenderer.shapeMap -= shape
        htmlRenderer.htmlConnections.deleteShape(shape)
    }

    init {
        containerShape.shapes.forEach(this::add)
        containerShape.onAdd(this::add)
        containerShape.onRemove(this::remove)
    }

    fun remove() {
        shapeMap.keys.forEach(this::remove)
    }
}