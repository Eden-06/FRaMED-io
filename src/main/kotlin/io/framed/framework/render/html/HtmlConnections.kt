package io.framed.framework.render.html;

import de.westermann.kobserve.ListenerReference
import io.framed.framework.JsPlumb
import io.framed.framework.JsPlumbInstance
import io.framed.framework.jsPlumbEndpointOptions
import io.framed.framework.pictogram.Connection
import io.framed.framework.pictogram.Shape
import io.framed.framework.pictogram.ViewModel
import io.framed.framework.util.async
import io.framed.framework.view.View
import org.w3c.dom.HTMLElement
import kotlin.math.abs

class HtmlConnections(
        val htmlRenderer: HtmlRenderer,
        val viewModel: ViewModel
) {

    val listeners = mutableListOf<ListenerReference<*>>()

    fun remove() {
        jsPlumbList.forEach {
            it.deleteEveryConnection()
            it.deleteEveryEndpoint()
        }
        jsPlumbList = emptyList()

        listeners.forEach { it.remove() }
        listeners.clear()
        anchors.clear()
    }

    private var jsPlumbList: List<JsPlumbInstance> = emptyList()

    fun createJsPlumb(container: HTMLElement): JsPlumbInstance {
        val instance = JsPlumb.getInstance().apply {
            setContainer(container)

            setZoom(1.0)
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

            htmlRenderer.navigationView.onZoom { zoom ->
                setZoom(zoom)
            }
        }

        jsPlumbList += instance

        return instance
    }

    private fun findInstance(idList: List<Long>): JsPlumbInstance? {
        val list = htmlRenderer.shapeMap.filterKeys { it.id in idList }.values.mapNotNull {
            it.jsPlumbInstance
        }.distinct()

        return when {
            list.isEmpty() -> null
            list.size == 1 -> list.first()
            jsPlumbList.first() in list -> jsPlumbList.first()
            else -> null
        }
    }

    fun addShape(shape: Shape) {
        async {
            if (viewModel.handler.canConnectionStart(shape.id ?: return@async)) {
                createEndpointInternal(shape)
            }

            val id = abs(shape.id)
            for ((conn, html) in relations) {
                if (conn.source.value == id || conn.target.value == id) {
                    html.draw()
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

    fun updateEndpoints(source: Shape? = null) {
        if (source == null) {
            htmlRenderer.shapeMap.keys.forEach {
                if (viewModel.handler.canConnectionStart(it.id ?: return)) {
                    createEndpointInternal(it)
                } else {
                    deleteEndpointInternal(it)
                }
            }
        } else {
            (htmlRenderer.shapeMap.keys - source).forEach {
                if (viewModel.handler.canConnectionCreate(source.id ?: return, it.id ?: return)) {
                    createEndpointInternal(it)
                } else {
                    deleteEndpointInternal(it)
                }
            }
        }
    }

    private fun createEndpointInternal(shape: Shape) {
        if (shape in endpointMap) return

        val html = htmlRenderer.shapeMap[shape]?.view?.html ?: return
        val jsPlumbInstance = htmlRenderer.shapeMap[shape]?.jsPlumbInstance ?: return

        // Endpoints in flat preview? The following disables them.
        // if (jsPlumbInstance != jsPlumbList.first()) return

        endpointMap[shape] = EndpointItem(jsPlumbInstance.addEndpoint(html, jsPlumbEndpointOptions {
            anchors = arrayOf("Bottom")
            isSource = true
            isTarget = true
            endpoint = "Dot"
        }), jsPlumbInstance)
    }

    /**
     * The model removes the endpoint for the given shape
     */
    private fun deleteEndpointInternal(shape: Shape) {
        val endpointItem = endpointMap[shape] ?: return
        endpointItem.jsPlumbInstance.deleteEndpoint(endpointItem.html)
        endpointMap -= shape
    }

    /**
     * The map stores the endpoints for all shapes
     */
    private val endpointMap = mutableMapOf<Shape, EndpointItem>()

    var relations: Map<Connection, HtmlRelation> = emptyMap()
    private fun drawRelation(jsPlumbInstance: JsPlumbInstance, relation: Connection) {
        relations += relation to HtmlRelation(relation, jsPlumbInstance, htmlRenderer)
    }

    val anchors: MutableMap<View<*>, Set<RelationSide>> = mutableMapOf()

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
                drawRelation(findInstance(listOf(it.source.get(), it.target.get())) ?: return@async, it)
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
        viewModel.connections.forEach {
            drawRelation(findInstance(listOf(it.source.get(), it.target.get())) ?: return@forEach, it)
        }
    }

    class EndpointItem(
            val html: HTMLElement,
            val jsPlumbInstance: JsPlumbInstance
    )
}
