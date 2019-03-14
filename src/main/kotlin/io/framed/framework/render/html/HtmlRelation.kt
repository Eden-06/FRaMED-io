package io.framed.framework.render.html

import de.westermann.kobserve.ListenerReference
import io.framed.framework.*
import io.framed.framework.pictogram.*
import io.framed.framework.util.Dimension
import io.framed.framework.util.Point
import io.framed.framework.util.point
import io.framed.framework.view.View
import io.framed.framework.view.ViewCollection
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.MouseEvent

/**
 * @author lars
 */
@Suppress("UNUSED")
class HtmlRelation(
        val connection: Connection,
        val renderer: HtmlRenderer
) : Selectable {

    override val id: Long = connection.id!!
    override val pictogram: Pictogram = connection

    override val left: Double
        get() = connections.last().canvas.style.left.replace("px", "").toDouble()
    override val top: Double
        get() = connections.last().canvas.style.top.replace("px", "").toDouble()
    override val width: Double
        get() = connections.last().canvas.width.baseVal.value.toDouble()
    override val height: Double
        get() = connections.last().canvas.height.baseVal.value.toDouble()

    override val positionView: View<*>? = null

    override fun select() {
        isSelected = true
        connection.onSidebar.emit(SidebarEvent(connection))
        draw()
    }

    override fun unselect() {
        isSelected = false
        draw()
    }

    override fun selectArea(area: Dimension) {
        val old = isSelected
        isSelected = Dimension(left, top, width, height) in area
        if (isSelected != old) {
            draw()
        }
    }

    override var isSelected: Boolean = false
    override val isDraggable: Boolean = false

    override fun drag(delta: Point) {
        // Ignore
    }

    override fun setZoom(zoom: Double) {
        // Ignore
    }

    override fun highlightSnap() {
        // Ignore
    }

    override fun unhighlightSnap() {
        // Ignore
    }

    override fun isChildOf(container: ViewCollection<View<*>, *>): Boolean = false

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
            renderer.selectable -= this
            renderer.htmlConnections.relations -= connection
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
                c.bind("mousedown") { _, event ->
                    if (!event.defaultPrevented && event is MouseEvent) {
                        event.preventDefault()
                        event.stopPropagation()
                        renderer.selectView(this, event.ctrlKey, false)
                    }
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
            stroke = if (isSelected) "#2980b9" else line.paintStyle.stroke.toCss()
            strokeWidth = line.paintStyle.strokeWidth

            dashstyle = line.paintStyle.dashArray.joinToString(" ")
        }
    }

    private fun createEndStyle(connectInit: JsPlumbConnectInit) {
        labels = connection.labels.map { label ->
            HtmlLabel(renderer, label, container).also {
                it.view.onMouseDown { event ->
                    if (!event.defaultPrevented) {
                        event.stopPropagation()
                        connection.onSidebar.emit(SidebarEvent(connection))
                    }
                }
            }
        }

        var overlays = labels.map { label ->
            arrayOf("Custom", object {
                val create: (dynamic) -> HTMLElement = {
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
                val location = style.length * style.foldback +1
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

        renderer.selectable += this
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
