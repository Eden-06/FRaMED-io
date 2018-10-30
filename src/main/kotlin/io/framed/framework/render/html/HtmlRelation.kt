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
            it.dragType = View.DragType.MARGIN
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

        connector = when (line.type) {
            ConnectionLine.Type.STRAIGHT -> {
                arrayOf("Straight")
            }
            ConnectionLine.Type.RECTANGLE -> {
                arrayOf("Flowchart", object {
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

    private fun createEndStyle(connectInit: JsPlumbConnectInit, drawSource: Boolean, drawTarget: Boolean) {
        var overlays = views.map { (view, position) ->
            arrayOf("Custom", object {
                val create = { _: dynamic ->
                    view.html
                }
                val cssClass = "front-end-label input-view"
                val location = position
            })
        }

        if (drawSource) {
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
        }

        if (drawTarget) {
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
        }

        connectInit.overlays = overlays.toTypedArray()
    }

    fun draw(sourceId: Long, targetId: Long) {
        remove()

        val directSourceView = renderer[sourceId, jsPlumbInstance]
        val directTargetView = renderer[targetId, jsPlumbInstance]

        val drawSource = directSourceView != null
        val drawTarget = directTargetView != null

        val sourceView = directSourceView ?: renderer[sourceId, jsPlumbInstance, true] ?: return
        val targetView = directTargetView ?: renderer[targetId, jsPlumbInstance, true] ?: return

        val zIndex = listOfNotNull(sourceView.zIndex, targetView.zIndex).max() ?: 0

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
        }).also {
            it.canvas.style.zIndex = zIndex.toString()
        }

        connection.lines.dropLast(1).forEach { line ->
            connections += jsPlumbInstance.connect(createJsPlumbConnection(line, sourceView, targetView)).also {
                it.canvas.style.zIndex = zIndex.toString()
            }
        }
        connection.lines.lastOrNull()?.let { line ->
            val init = createJsPlumbConnection(line, sourceView, targetView)
            createEndStyle(init, drawSource, drawTarget)
            connections += jsPlumbInstance.connect(init).also {
                it.canvas.style.zIndex = zIndex.toString()
            }
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