package io.framed.framework.render.html

import de.westermann.kobserve.ListenerReference
import io.framed.framework.*
import io.framed.framework.pictogram.Connection
import io.framed.framework.pictogram.Shape
import io.framed.framework.pictogram.ViewModel
import io.framed.framework.util.async
import io.framed.framework.view.IconView
import io.framed.framework.view.MaterialIcon
import io.framed.framework.view.View
import org.w3c.dom.HTMLElement
import kotlin.math.abs

class HtmlConnections(
        private val htmlRenderer: HtmlRenderer,
        private val viewModel: ViewModel
) {

    val listeners = mutableListOf<ListenerReference<*>>()
    private val endpointMap = mutableMapOf<Shape, EndpointItem>()
    private var relations: Map<Connection, HtmlRelation> = emptyMap()
    val anchors: MutableMap<View<*>, Set<RelationSide>> = mutableMapOf()
    private var jsPlumbList: List<JsPlumbInstance> = emptyList()

    fun remove() {
        jsPlumbList.forEach {
            it.deleteEveryConnection()
            it.deleteEveryEndpoint()
        }
        jsPlumbList = emptyList()

        listeners.forEach { it.remove() }
        listeners.clear()
        relations = emptyMap()
        endpointMap.clear()
        anchors.clear()
    }

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

    private fun updateEndpoints(source: Shape? = null) {
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
        val jsPlumbRoot = endpointItem.jsPlumbRoot
        val view = endpointItem.view
        val html = view.html

        if (jsPlumbInstance.isTargetEnabled(html) != enabled) {
            //jsPlumbInstance.setTargetEnabled(html)
        }
        if (jsPlumbRoot.isTargetEnabled(html) != enabled) {
            //jsPlumbRoot.setTargetEnabled(html)
        }

        view.classes["target-disabled"] = !enabled
    }

    private fun createEndpointInternal(shape: Shape) {
        if (shape in endpointMap) return

        val view = htmlRenderer.shapeMap[shape]?.view ?: return
        val html = view.html
        val jsPlumbInstance = htmlRenderer.shapeMap[shape]?.jsPlumbInstance ?: return
        val jsPlumbRoot = jsPlumbList.first()

        val handler = IconView(MaterialIcon.ADD)
        html.appendChild(handler.html)
        handler.id = "shape-${shape.id}-${endpointMap.size}"
        handler.classes += "connection-source"

        jsPlumbInstance.makeSource(html, jsPlumbSourceOptionsInit {
            filter = { event, element ->
                val target = event.target as HTMLElement
                (target == handler.html || target.parentElement == handler.html)
            }
        })
        jsPlumbInstance.makeTarget(html, jsPlumbTargetOptionsInit {
            allowLoopback = false
        })
        if (jsPlumbInstance != jsPlumbRoot) {
            jsPlumbRoot.makeTarget(html, jsPlumbTargetOptionsInit {
                allowLoopback = false
            })
        }

        endpointMap[shape] = EndpointItem(view, handler.html, jsPlumbInstance, jsPlumbRoot)

        setSourceEnabled(shape, true)
        setTargetEnabled(shape, true)
    }

    /**
     * The model removes the endpoint for the given shape
     */
    private fun deleteEndpointInternal(shape: Shape) {
        val endpointItem = endpointMap[shape] ?: return
        val jsPlumbInstance = endpointItem.jsPlumbInstance
        val jsPlumbRoot = endpointItem.jsPlumbRoot
        val view = endpointItem.view
        val html = view.html

        view.classes -= "source-disabled"
        view.classes -= "target-disabled"

        jsPlumbInstance.unmakeSource(html)
        jsPlumbInstance.unmakeTarget(html)
        jsPlumbRoot.unmakeTarget(html)
        endpointMap -= shape
    }

    private fun drawRelation(jsPlumbInstance: JsPlumbInstance, relation: Connection) {
        relations += relation to HtmlRelation(relation, jsPlumbInstance, htmlRenderer)
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
            val view: View<*>,
            val handler: HTMLElement,
            val jsPlumbInstance: JsPlumbInstance,
            val jsPlumbRoot: JsPlumbInstance
    )
}
