package io.framed.framework.render.html

import io.framed.framework.*
import io.framed.framework.pictogram.*
import io.framed.framework.util.point
import io.framed.framework.view.InputView
import io.framed.framework.view.View
import org.w3c.dom.events.MouseEvent

/**
 * @author lars
 */
class HtmlRelation(
        val connection: Connection,
        val jsPlumbInstance: JsPlumbInstance,
        val renderer: HtmlRenderer
) {

    private var connections: List<JsPlumbConnection> = emptyList()
    fun remove() {
        connections.forEach {
            jsPlumbInstance.deleteConnection(it)
        }
        connections = emptyList()
    }

    private val views = connection.labels.map { (shape, position) ->
        InputView(shape.property).also {
            it.drag = View.DragType.MARGIN
            it.autocomplete = shape.autocomplete
            it.sizeMatchText()
        } to position
    }

    fun draw() {
        draw(connection.source.get(), connection.target.get())
    }

    private fun createJsPlumbConnection(line: ConnectionLine, sourceView: View<*>, targetView: View<*>) = jsPlumbConnect {
        source = sourceView.html
        target = targetView.html

        anchor = arrayOf("Top", "Left", "Bottom", "Right")

        when (line.type) {
            ConnectionLine.Type.STRAIGHT -> {
                connector = arrayOf("Straight")
            }
            ConnectionLine.Type.RECTANGLE -> {
                connector = arrayOf("Flowchart", object {
                    val cornerRadius = 5
                })
            }
        }
        endpoint = "Blank"

        paintStyle = jsPlumbPaintStyle {
            stroke = line.paintStyle.stroke.toCss()
            strokeWidth = line.paintStyle.strokeWidth
        }
    }

    fun createEndStyle(connectInit: JsPlumbConnectInit) {
        var overlays = views.map { (view, position) ->
            arrayOf("Custom", object {
                val create = { _: dynamic ->
                    view.html
                }
                val cssClass = "front-end-label input-view"
                val location = position
            })
        }

        connection.sourceStyle?.let { style ->
            overlays += listOf(arrayOf("Arrow", object {
                val width = style.width
                val length = style.length
                val foldback = style.foldback
                val paintStyle = jsPlumbPaintStyle {
                    stroke = style.paintStyle.stroke.toCss()
                    strokeWidth = style.paintStyle.strokeWidth
                    fill = style.paintStyle.fill.toCss()
                }
                val location = 0
            }))
        }
        connection.targetStyle?.let { style ->
            overlays += listOf(arrayOf("Arrow", object {
                val width = style.width
                val length = style.length
                val foldback = style.foldback
                val paintStyle = jsPlumbPaintStyle {
                    stroke = style.paintStyle.stroke.toCss()
                    strokeWidth = style.paintStyle.strokeWidth
                    fill = style.paintStyle.fill.toCss()
                }
                val location = 1
            }))
        }

        connectInit.overlays = overlays.toTypedArray()
    }

    fun draw(sourceShape: Shape, targetShape: Shape) {
        remove()

        val sourceView = renderer[sourceShape]
        val targetView = renderer[targetShape]

        // Click area
        connections += jsPlumbInstance.connect(jsPlumbConnect {
            source = sourceView.html
            target = targetView.html

            anchor = arrayOf("Top", "Left", "Bottom", "Right")
            connector = arrayOf("Flowchart", object {
                val cornerRadius = 5
            })
            endpoint = "Blank"

            paintStyle = jsPlumbPaintStyle {
                stroke = "transparent"
                strokeWidth = 15
            }
        })

        connection.lines.dropLast(1).forEach { line ->
            connections += jsPlumbInstance.connect(createJsPlumbConnection(line, sourceView, targetView))
        }
        connection.lines.lastOrNull()?.let { line ->
            val init = createJsPlumbConnection(line, sourceView, targetView)
            createEndStyle(init)
            connections += jsPlumbInstance.connect(init)
        }

        connections.forEach { c ->
            if (connection.hasSidebar) {
                c.bind("click") { _, event ->
                    event.stopPropagation()
                    connection.onSidebar.fire(SidebarEvent(connection))
                }
            }

            if (connection.hasContextMenu) {
                c.bind("contextmenu") { _, event ->
                    (event as? MouseEvent)?.let { e ->
                        e.stopPropagation()
                        e.preventDefault()
                        connection.onContextMenu.fire(ContextEvent(e.point(), connection))
                    }
                }
            }
        }
    }

    init {
        draw()

        connection.source.onChange {
            draw()
        }
        connection.target.onChange {
            draw()
        }
        connection.onStyleChange {
            draw()
        }
    }
}