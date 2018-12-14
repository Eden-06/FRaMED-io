package io.framed.framework.render.html

import de.westermann.kobserve.ListenerReference
import io.framed.framework.*
import io.framed.framework.pictogram.Connection
import io.framed.framework.pictogram.ConnectionLine
import io.framed.framework.pictogram.ContextEvent
import io.framed.framework.pictogram.SidebarEvent
import io.framed.framework.util.point
import io.framed.framework.view.InputView
import io.framed.framework.view.TextView
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

    private val references: MutableList<ListenerReference<*>> = mutableListOf()
    private var connections: List<JsPlumbConnection> = emptyList()
    fun remove(complete: Boolean = false) {
        connections.forEach {
            jsPlumbInstance.deleteConnection(it)
        }
        connections = emptyList()

        if (complete) {
            references.forEach {
                it.remove()
            }
            references.clear()
        }
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

        anchors = arrayOf(sourceAnchorString, targetAnchorString) as Array<Array<Any>>

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
        } else {
            overlays += listOf(arrayOf("Custom", object {
                val create = { _: dynamic ->
                    TextView("Id: ${connection.source.get()}").html
                }
                val location = 11
            }))
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
        } else {
            overlays += listOf(arrayOf("Custom", object {
                val create = { _: dynamic ->
                    TextView("Id: ${connection.target.get()}").html
                }
                val location = -10
            }))
        }

        connectInit.overlays = overlays.toTypedArray()
    }

    var sourceAnchor: Set<RelationSide> = ALL_SIDES
    private val sourceAnchorString: Array<String>
        get() = sourceAnchor.map { it.jsPlumb }.toTypedArray()

    var targetAnchor: Set<RelationSide> = ALL_SIDES
    private val targetAnchorString: Array<String>
        get() = targetAnchor.map { it.jsPlumb }.toTypedArray()

    lateinit var sourceView: View<*>
    lateinit var targetView: View<*>

    fun draw(sourceId: Long, targetId: Long) {
        remove()

        val sourceViewNew = renderer[sourceId, jsPlumbInstance] ?: return
        val targetViewNew = renderer[targetId, jsPlumbInstance] ?: return

        if (!this::sourceView.isInitialized || sourceView != sourceViewNew) {
            sourceView = sourceViewNew
            sourceAnchor = ALL_SIDES
        }
        if (!this::targetView.isInitialized || targetView != targetViewNew) {
            targetView = targetViewNew
            targetAnchor = ALL_SIDES
        }

        val zIndex = listOfNotNull(sourceView.zIndex, targetView.zIndex).max() ?: 0

        // Click area
        connections += jsPlumbInstance.connect(jsPlumbConnect {
            source = sourceView.html
            target = targetView.html

            anchors = arrayOf(sourceAnchorString, targetAnchorString) as Array<Array<Any>>
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
            console.log(it)
        }

        connection.lines.dropLast(1).forEach { line ->
            connections += jsPlumbInstance.connect(createJsPlumbConnection(line, sourceView, targetView)).also {
                it.canvas.style.zIndex = zIndex.toString()
            }
        }
        connection.lines.lastOrNull()?.let { line ->
            val init = createJsPlumbConnection(line, sourceView, targetView)
            createEndStyle(init, true, true)
            connections += jsPlumbInstance.connect(init).also {
                it.canvas.style.zIndex = zIndex.toString()
            }
        }

        connections.forEach { c ->
            if (connection.hasSidebar) {
                c.bind("click") { _, event ->
                    event.stopPropagation()
                    connection.onSidebar.emit(SidebarEvent(connection))
                }
            }

            if (connection.hasContextMenu) {
                c.bind("contextmenu") { _, event ->
                    (event as? MouseEvent)?.let { e ->
                        e.stopPropagation()
                        e.preventDefault()
                        connection.onContextMenu.emit(ContextEvent(e.point(), connection))
                    }
                }
            }
        }
    }

    init {
        draw()

        connection.source.onChange.reference {
            draw()
        }?.let(references::add)
        connection.target.onChange.reference {
            draw()
        }?.let(references::add)
        connection.onStyleChange.reference {
            draw()
        }?.let(references::add)
    }

    companion object {
        val ALL_SIDES = setOf(RelationSide.TOP, RelationSide.LEFT, RelationSide.BOTTOM, RelationSide.RIGHT)
    }
}

enum class RelationSide(val jsPlumb: String) {
    TOP("Top"),
    LEFT("Left"),
    BOTTOM("Bottom"),
    RIGHT("Right")
}
