package io.framed.framework.render.html

import de.westermann.kobserve.ListenerReference
import io.framed.framework.*
import io.framed.framework.pictogram.Connection
import io.framed.framework.pictogram.ConnectionLine
import io.framed.framework.pictogram.ContextEvent
import io.framed.framework.pictogram.SidebarEvent
import io.framed.framework.util.point
import io.framed.framework.view.View
import io.framed.framework.view.ViewCollection
import org.w3c.dom.events.MouseEvent

/**
 * @author lars
 */
class HtmlRelation(
        val connection: Connection,
        val renderer: HtmlRenderer
) {

    private val references: MutableList<ListenerReference<*>> = mutableListOf()
    private var connections: List<JsPlumbConnection> = emptyList()
    private var labels: List<HtmlLabel> = emptyList()
    fun remove(complete: Boolean = false) {
        if (this::jsPlumbInstance.isInitialized) {
            connections.forEach {
                jsPlumbInstance.deleteConnection(it)
            }
            connections = emptyList()
        }

        for (label in labels) {
            label.remove()
        }
        labels = emptyList()

        if (complete) {
            references.forEach {
                it.remove()
            }
            references.clear()
        }
    }

    fun draw() {
        val sourceId = connection.source.value
        val targetId = connection.target.value

        val instance = renderer.htmlConnections.findInstance(listOf(sourceId, targetId))
        val box = renderer.htmlConnections.jsPlumbList.find { it.first == instance }?.second

        remove()

        if (instance == null || box == null) {
            return
        }

        jsPlumbInstance = instance
        container = box

        val sourceViewNew = renderer[sourceId, jsPlumbInstance] ?: return
        val targetViewNew = renderer[targetId, jsPlumbInstance] ?: return

        if (!this::sourceView.isInitialized || sourceView != sourceViewNew) {
            sourceView = sourceViewNew
        }
        if (!this::targetView.isInitialized || targetView != targetViewNew) {
            targetView = targetViewNew
        }

        val zIndex = listOfNotNull(sourceView.zIndex, targetView.zIndex).max() ?: 0

        // Click area
        connections += jsPlumbInstance.connect(jsPlumbConnect {
            source = sourceView.html
            target = targetView.html

            anchors = arrayOf(sourceAnchorString, targetAnchorString)
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
            createEndStyle(init)
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
                        val diagram = renderer.snapPoint(renderer.navigationView.mouseToCanvas(e.point())).point
                        connection.onContextMenu.emit(ContextEvent(e.point(), diagram, connection))
                    }
                }
            }
        }
    }

    private fun createJsPlumbConnection(line: ConnectionLine, sourceView: View<*>, targetView: View<*>) = jsPlumbConnect {
        source = sourceView.html
        target = targetView.html

        anchors = arrayOf(sourceAnchorString, targetAnchorString)

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

    private fun createEndStyle(connectInit: JsPlumbConnectInit) {
        labels = connection.labels.map { label ->
            HtmlLabel(renderer, label, container)
        }

        var overlays = labels.map { label ->
            arrayOf("Custom", object {
                val create = { _: dynamic ->
                    label.view.html
                }
                val cssClass = label.view.classes.toString()
                val location = label.label.positionProperty.value
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

    @Suppress("UNCHECKED_CAST")
    private val sourceAnchorString: Array<Any>
        get() = (renderer.htmlConnections.anchors[sourceView]?.map { it.jsPlumb }?.toTypedArray()
                ?: ALL_SIDES_STRING) as Array<Any>

    @Suppress("UNCHECKED_CAST")
    private val targetAnchorString: Array<Any>
        get() = (renderer.htmlConnections.anchors[targetView]?.map { it.jsPlumb }?.toTypedArray()
                ?: ALL_SIDES_STRING) as Array<Any>

    lateinit var sourceView: View<*>
    lateinit var targetView: View<*>
    lateinit var jsPlumbInstance: JsPlumbInstance
    lateinit var container: ViewCollection<View<*>, *>

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
        val ALL_SIDES_STRING = ALL_SIDES.map {
            it.jsPlumb
        }.toTypedArray()
    }
}

enum class RelationSide(val jsPlumb: String) {
    TOP("Top"),
    LEFT("Left"),
    BOTTOM("Bottom"),
    RIGHT("Right")
}
