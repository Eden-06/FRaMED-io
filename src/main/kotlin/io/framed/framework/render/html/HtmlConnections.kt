package io.framed.framework.render.html

import de.westermann.kobserve.event.EventListener
import io.framed.framework.JsPlumb
import io.framed.framework.JsPlumbInstance
import io.framed.framework.jsPlumbSourceOptionsInit
import io.framed.framework.jsPlumbTargetOptionsInit
import io.framed.framework.pictogram.Connection
import io.framed.framework.pictogram.Shape
import io.framed.framework.pictogram.ViewModel
import io.framed.framework.util.async
import io.framed.framework.view.*
import org.w3c.dom.HTMLElement
import kotlinx.browser.document
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.set
import kotlin.math.abs

/**
 * HTML representation of a [Connection].
 *
 * @author Lars Westermann, David Oberacker
 */
class HtmlConnections(
    private val htmlRenderer: HtmlRenderer,
    private val viewModel: ViewModel
) {

    val listeners = mutableListOf<EventListener<*>>()
    private val endpointMap = mutableMapOf<Shape, EndpointItem>()
    private val endpointReverseMap = mutableMapOf<HTMLElement, Shape>()

    var relations: Map<Connection, HtmlRelation> = emptyMap()
    val anchors: MutableMap<View<*>, Set<RelationSide>> = mutableMapOf()
    var jsPlumbList: List<Pair<JsPlumbInstance, Pair<Shape, ViewCollection<View<*>, *>>>> = emptyList()

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
            it.detach()
        }
        listeners.clear()
        relations = emptyMap()
        endpointMap.clear()
        endpointReverseMap.clear()
        anchors.clear()
        isConnecting = null
    }

    private var createSourceShape: Shape? = null
    private var createTargetShape: Shape? = null

    /**
     * Creates a new jsPlumb instance managing the given [container] inside the specified [shape].
     */
    fun createJsPlumb(shape: Shape, container: ViewCollection<View<*>, *>): JsPlumbInstance {
        val instance = JsPlumb.JsPlumb.getInstance().apply {
            setContainer(container.html)

            var reference: EventListener<*>? = null

            setZoom(htmlRenderer.zoom)
            bind("beforeDrop") { info: dynamic ->
                val source = createSourceShape?.id
                    ?: htmlRenderer.getShapeById(info.sourceId as String)?.id
                    ?: return@bind false
                val target = createTargetShape?.id
                    ?: htmlRenderer.getShapeById(info.targetId as String)?.id
                    ?: return@bind false

                htmlRenderer.viewModel.handler.createConnection(source, target)

                reference?.detach()

                createTargetShape?.let { endpointMap[it]?.let { it.view.classes -= "drop-target" } }
                createSourceShape = null
                createTargetShape = null

                return@bind false
            }

            bind("beforeDrag") { info: dynamic ->
                val source = htmlRenderer.getShapeById(info.sourceId as String) ?: return@bind true
                createSourceShape = source
                createTargetShape = null

                reference = Root.onMouseMove.reference {
                    checkMousePosition()
                }

                updateEndpoints(source)

                return@bind true
            }

            bind("connectionDragStop") {
                createTargetShape?.let { endpointMap[it]?.let { it.view.classes -= "drop-target" } }
                createSourceShape = null
                createTargetShape = null
                reference?.detach()
                updateEndpoints()
            }

            bind("connectionAborted") {
                createTargetShape?.let { endpointMap[it]?.let { it.view.classes -= "drop-target" } }
                createSourceShape = null
                createTargetShape = null
                reference?.detach()
                updateEndpoints()
            }

            htmlRenderer.onZoom { zoom ->
                setZoom(zoom)
            }
        }

        // Some shapes do not have id's.
        // Find the closest shape that is a parent, that has a id.
        var idParent: Shape = shape
        var parent = shape.parent
        while (parent != null) {
            if (parent.id != null) {
                idParent = parent
                break;
            }
            parent = parent.parent
        }

        // Add the new instance with the parent shape and the container to the map.
        jsPlumbList += instance to (idParent to container)

        return instance
    }

    /**
     * Finds all ancestors of a element with an id that are non anonymous (also have a id).
     *
     * The ancestors are sorted from the oldest ancestor to the element with the id given in [idList].
     * The shape itself is included in the list.
     *
     * This function has a complexity of O(|htmlRenderer.shapeMap|*|idList|)
     */
    private fun findNonAnonymousAncestors(idList: List<Long>): List<List<Shape>> {
        return htmlRenderer.shapeMap.filterKeys { it.id in idList }.map { (shape, _) ->
            val ancestors = mutableListOf<Shape>()
            ancestors += shape

            var parent = shape.parent
            while (parent != null) {
                if (parent.id != null) {
                    ancestors += parent
                }
                parent = parent.parent
            }
            ancestors.reversed()
        }
    }

    /**
     * Finds the lowest jsPlumbInstance that encompasses all shapes in the idList.
     *
     * A suitable instance has to contain all shapes in the list.
     * Thus if a parent and child shape are give, the parent of the parent should be returned.
     *
     * @author David Oberacker (david.oberacker@student.kit.edu)
     */
    fun findInstance(idList: List<Long>): JsPlumbInstance {

        // Finds all ancestors that are not in the idList.
        val ancestorLists = findNonAnonymousAncestors(idList).map {
            it.filter { shape -> !idList.contains(shape.id) }
        }

        // Calculate the intersection of the different ancestor lists.
        val validAncestorIds = ancestorLists
            .map { ancestors -> ancestors.map { ancestor -> ancestor.id } }
            .reduceRightOrNull { a, b ->
                a.intersect(b).toMutableList()
            } ?: return jsPlumbList.first().first

        val selectedParentShape = ancestorLists
            .asSequence()
            .flatten()
            .distinct()
            .filter { it.id in validAncestorIds }
            .sortedWith(compareByDescending { htmlRenderer.calculateDepthOfView(it) })
            .firstOrNull()

        return if (selectedParentShape == null) {
            jsPlumbList.first().first
        } else {
            jsPlumbList.firstOrNull { it.second.first.id == selectedParentShape.id }?.first ?: jsPlumbList.first().first
        }

    }

    fun addShape(shape: Shape) {
        async {
            val originalId = shape.id ?: return@async
            if (viewModel.handler.isConnectable(originalId)) {
                createEndpointInternal(shape, viewModel.handler.canConnectionStart(originalId))
            }

            val id = abs(originalId)
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

    /**
     * Function ot give a shape the source-disabled css class.
     */
    private fun setSourceEnabled(shape: Shape, enabled: Boolean) {
        val endpointItem = endpointMap[shape] ?: return
        val view = endpointItem.view

        view.classes["source-disabled"] = !enabled
    }

    /**
     * Function to give a shape the target-disabled css class.
     */
    private fun setTargetEnabled(shape: Shape, enabled: Boolean) {
        val endpointItem = endpointMap[shape] ?: return
        val view = endpointItem.view
        view.classes["target-disabled"] = !enabled
    }

    /**
     * Function for checking if the elements at the current mouse position is a valid target for the new connection.
     */
    private fun checkMousePosition() {
        if (jsTypeOf(document::elementsFromPoint) != "function") return

        val position = Root.mousePosition
        val elements = document.elementsFromPoint(position.x, position.y)

        createTargetShape?.let {
            endpointMap[it]?.let {
                it.view.classes -= "drop-target"
            }
        }

        val selectedDropTarget = elements
            .filter { element ->
                val shape = this.endpointReverseMap[element] ?: return@filter false
                val entry = this.endpointMap[shape] ?: return@filter false

                // Change break to continue to allow connections to visible hidden elements
                return@filter !(isConnecting == shape || "target-disabled" in entry.view.classes)
            }.map { element ->
                val shape = this.endpointReverseMap[element]!!
                val entry = this.endpointMap[shape]!!

                return@map Pair(entry, shape)
            }.toList()
            .distinctBy { (_, s) ->
                s.id
            }.sortedWith { (_, s1), (_, s2) ->
                compareValues(
                    htmlRenderer.calculateDepthOfView(s1),
                    htmlRenderer.calculateDepthOfView(s2)
                )
            }
            .lastOrNull()

        if (selectedDropTarget != null) {
            val entry = selectedDropTarget.first
            val shape = selectedDropTarget.second

            entry.view.classes += "drop-target"
            createTargetShape = shape
        } else {
            createTargetShape = null
        }
    }

    /**
     * Function adding the Add Symbol next to shape for creating a new connection.
     */
    private fun createEndpointInternal(shape: Shape, canStart: Boolean) {
        if (shape in endpointMap) return

        val view = htmlRenderer.shapeMap[shape]?.view ?: return
        val html = view.html
        val jsPlumbInstance = jsPlumbList.firstOrNull()?.first ?: return

        val handler = if (canStart) {
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

            handler
        } else null

        jsPlumbInstance.makeTarget(html, jsPlumbTargetOptionsInit {
            allowLoopback = false
        })

        endpointMap[shape] = EndpointItem(view, handler?.html, jsPlumbInstance)
        endpointReverseMap[view.html] = shape

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

        view.classes -= "source-disabled"
        view.classes -= "target-disabled"

        jsPlumbInstance.unmakeSource(html)
        jsPlumbInstance.unmakeTarget(html)
        endpointMap -= shape
        endpointReverseMap -= html
    }

    private fun drawRelation(relation: Connection) {
        relations += relation to HtmlRelation(relation, htmlRenderer)
    }

    fun limitSide(view: View<*>, anchor: Set<RelationSide>) {
        if (anchors[view] != anchor) {
            anchors[view] = anchor
            relations.values.forEach {
                if (it::sourceView.isInitialized && it.sourceView == view || it::targetView.isInitialized && it.targetView == view) {
                    it.draw()
                }
            }
        }
    }

    init {
        listeners += viewModel.onConnectionAdd.reference {
            async {
                drawRelation(it)
            }
        }

        listeners += viewModel.onConnectionRemove.reference { r ->
            relations[r]?.let { relation ->
                relations -= r
                relation.remove(true)
            }
        }
    }

    fun init() {
        viewModel.connections.forEach(this::drawRelation)
    }

    class EndpointItem(
        val view: View<*>,
        val handler: HTMLElement?,
        val jsPlumbInstance: JsPlumbInstance
    )
}
