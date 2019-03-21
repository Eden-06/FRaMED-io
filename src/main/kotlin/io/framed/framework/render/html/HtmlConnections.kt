package io.framed.framework.render.html

import de.westermann.kobserve.ListenerReference
import io.framed.framework.JsPlumb
import io.framed.framework.JsPlumbInstance
import io.framed.framework.jsPlumbSourceOptionsInit
import io.framed.framework.jsPlumbTargetOptionsInit
import io.framed.framework.pictogram.Connection
import io.framed.framework.pictogram.Shape
import io.framed.framework.pictogram.ViewModel
import io.framed.framework.util.Dimension
import io.framed.framework.util.async
import io.framed.framework.util.point
import io.framed.framework.view.*
import org.w3c.dom.HTMLElement
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.set
import kotlin.math.abs

class HtmlConnections(
        private val htmlRenderer: HtmlRenderer,
        private val viewModel: ViewModel
) {

    val listeners = mutableListOf<ListenerReference<*>>()
    private val endpointMap = mutableMapOf<Shape, EndpointItem>()
    var relations: Map<Connection, HtmlRelation> = emptyMap()
    val anchors: MutableMap<View<*>, Set<RelationSide>> = mutableMapOf()
    var jsPlumbList: List<Pair<JsPlumbInstance, ViewCollection<View<*>, *>>> = emptyList()

    private var isConnecting: Shape? = null

    fun remove() {
        relations.values.forEach { it.remove(true) }
        for ((it, _) in jsPlumbList) {
            it.deleteEveryConnection()
            it.deleteEveryEndpoint()
            it.reset()
        }
        jsPlumbList = emptyList()

        for (it in listeners) {
            it.remove()
        }
        listeners.clear()
        relations = emptyMap()
        endpointMap.clear()
        anchors.clear()
        isConnecting = null
    }

    fun createJsPlumb(container: ViewCollection<View<*>, *>): JsPlumbInstance {
        val instance = JsPlumb.getInstance().apply {
            setContainer(container.html)

            setZoom(htmlRenderer.zoom)
            bind("beforeDrop") { info: dynamic ->
                val source = htmlRenderer.getShapeById(info.sourceId as String)?.id ?: return@bind false
                val target = htmlRenderer.getShapeById(info.targetId as String)?.id ?: return@bind false

                htmlRenderer.viewModel.handler.createConnection(source, target)

                return@bind false
            }

            bind("beforeDrag") { info: dynamic ->
                val source = htmlRenderer.getShapeById(info.sourceId as String) ?: return@bind true
                updateEndpoints(source)

                return@bind true
            }

            bind("connectionDragStop") {
                updateEndpoints()
            }

            bind("connectionAborted") {
                updateEndpoints()
            }

            htmlRenderer.onZoom { zoom ->
                setZoom(zoom)
            }
        }

        jsPlumbList += instance to container

        return instance
    }

    fun findInstance(idList: List<Long>): JsPlumbInstance? {
        val list = htmlRenderer.shapeMap.filterKeys { it.id in idList }.values.mapNotNull {
            it.jsPlumbInstance
        }.distinct()

        return when {
            list.isEmpty() -> null
            list.size == 1 -> list.first()
            jsPlumbList.first().first in list -> jsPlumbList.first().first
            else -> null
        }
    }

    fun addShape(shape: Shape) {
        async {
            if (viewModel.handler.canConnectionStart(shape.id ?: return@async)) {
                createEndpointInternal(shape)
            }

            val id = abs(shape.id)
            for ((conn, relation) in relations) {
                if (conn.source.value == id || conn.target.value == id) {
                    relation.draw()
                }
            }
        }
    }

    fun deleteShape(shape: Shape) {
        deleteEndpointInternal(shape)

        val id = abs(shape.id ?: return)
        for ((conn, html) in relations) {
            if (conn.source.value == id || conn.target.value == id) {
                html.draw()
            }
        }
    }

    fun revalidate(html: HTMLElement) {
        for ((instance, _) in jsPlumbList) {
            try {
                instance.revalidate(html)
            } catch (e: dynamic) {
            }
        }
    }

    private fun updateEndpoints(source: Shape? = null) {
        isConnecting = source
        if (source == null) {
            htmlRenderer.shapeMap.keys.forEach {
                setSourceEnabled(
                        it,
                        viewModel.handler.canConnectionStart(it.id ?: return)
                )
                setTargetEnabled(it, true)
            }
        } else {
            (htmlRenderer.shapeMap.keys - source).forEach {
                setTargetEnabled(
                        it,
                        viewModel.handler.canConnectionCreate(source.id ?: return, it.id ?: return)
                )
            }
        }
    }

    private fun setSourceEnabled(shape: Shape, enabled: Boolean) {
        val endpointItem = endpointMap[shape] ?: return
        val jsPlumbInstance = endpointItem.jsPlumbInstance
        val view = endpointItem.view
        val html = view.html

        if (jsPlumbInstance.isSourceEnabled(html) != enabled) {
            //jsPlumbInstance.setSourceEnabled(html)
        }

        view.classes["source-disabled"] = !enabled
    }

    private fun setTargetEnabled(shape: Shape, enabled: Boolean) {
        val endpointItem = endpointMap[shape] ?: return
        val jsPlumbInstance = endpointItem.jsPlumbInstance
        val view = endpointItem.view
        val html = view.html

        if (jsPlumbInstance.isTargetEnabled(html) != enabled) {
            //jsPlumbInstance.setTargetEnabled(html)
        }

        view.classes["target-disabled"] = !enabled
    }

    private fun createEndpointInternal(shape: Shape) {
        if (shape in endpointMap) return

        val view = htmlRenderer.shapeMap[shape]?.view ?: return
        val html = view.html
        val jsPlumbInstance = jsPlumbList.firstOrNull()?.first ?: return

        val handler = IconView(MaterialIcon.ADD)
        html.appendChild(handler.html)
        handler.id = "shape-${shape.id}-${endpointMap.size}"
        handler.classes += "connection-source"
        handler.preventDrag()
        handler.onMouseMove {
            it.stopPropagation()
        }

        jsPlumbInstance.makeSource(html, jsPlumbSourceOptionsInit {
            filter = { event, _ ->
                val target = event.target as HTMLElement
                target == handler.html || target.parentElement == handler.html
            }
        })
        jsPlumbInstance.makeTarget(html, jsPlumbTargetOptionsInit {
            allowLoopback = false
        })

        val references = mutableListOf<ListenerReference<*>>()
        view.onMouseEnter.reference {
            if (isConnecting != null && isConnecting != shape && "target-disabled" !in view.classes) {
                view.classes += "drop-target"

                var reference: ListenerReference<*>? = null
                reference = Root.onMouseMove.reference { event ->
                    val dimension = Dimension(shape.leftOffset, shape.topOffset, shape.width, shape.height)
                    val can = htmlRenderer.navigationView.mouseToCanvas(event.point())
                    if (can !in dimension || event.buttons == 0.toShort()) {
                        view.classes -= "drop-target"
                        reference?.remove()
                    }
                }
            }
        }?.let(references::add)
        endpointMap[shape] = EndpointItem(view, handler.html, references, jsPlumbInstance)

        setSourceEnabled(shape, true)
        setTargetEnabled(shape, true)

    }

    /**
     * The model removes the endpoint for the given shape
     */
    private fun deleteEndpointInternal(shape: Shape) {
        val endpointItem = endpointMap[shape] ?: return
        val jsPlumbInstance = endpointItem.jsPlumbInstance
        val view = endpointItem.view
        val html = view.html

        for (reference in endpointItem.references) {
            reference.remove()
        }

        view.classes -= "source-disabled"
        view.classes -= "target-disabled"

        jsPlumbInstance.unmakeSource(html)
        jsPlumbInstance.unmakeTarget(html)
        endpointMap -= shape
    }

    private fun drawRelation(relation: Connection) {
        relations += relation to HtmlRelation(relation, htmlRenderer)
    }

    fun limitSide(view: View<*>, anchor: Set<RelationSide>) {
        if (anchors[view] != anchor) {
            anchors[view] = anchor
            relations.values.forEach {
                if (it.sourceView == view || it.targetView == view) {
                    it.draw()
                }
            }
        }
    }

    init {
        viewModel.onConnectionAdd.reference {
            async {
                drawRelation(it)
            }
        }?.let(listeners::add)

        viewModel.onConnectionRemove.reference { r ->
            relations[r]?.let { relation ->
                relations -= r
                relation.remove(true)
            }
        }?.let(listeners::add)
    }

    fun init() {
        viewModel.connections.forEach(this::drawRelation)
    }

    class EndpointItem(
            val view: View<*>,
            val handler: HTMLElement,
            val references: List<ListenerReference<*>>,
            val jsPlumbInstance: JsPlumbInstance
    )
}
