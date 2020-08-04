package io.framed.framework

/**
 * The class connects the Dagre-library to the middleware.
 * @author Sebastian
 */
class Dagre {
    companion object {
        /**
         * Get a new directed Dagre Graph.
         */
        @Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
        fun getGraph(): DagreGraph {
            return js("new dagre.graphlib.Graph()") as DagreGraph
        }

        /**
         * The method sorts all nodes according to the current options.
         */
        @Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
        fun layout(graph: DagreGraph) {
            js("dagre").layout(graph)
        }
    }
}

/**
 * The interface represents an edge in the dagre framework.
 * Source and target id are equal to the IDs of the DagreNode(-s).
 */
external interface DagreEdge {
    /**
     * The number of ranks to keep between the source and target of the edge.
     */
    var minlen: Int
    var sourceId: Long
    var targetId: Long
    /**
     * The weight to assign edges. Higher weight edges are generally made shorter and straighter than lower weight edges.
     */
    var weight: Int
}

/**
 *
 */
fun dagreEdge(init: DagreEdge.() -> Unit = {}): DagreEdge {
    val e = js("{}")
    init(e)
    return e
}

@JsName("Graph")
external interface DagreGraph {
    /**
     * The method sets the general settings for the algorithm.
     */
    fun setGraph(options: DagreGraphOptions)

    /**
     * The method adds a new node to the algorithm.
     */
    fun setNode(id: Long, node: DagreNodeOptions)

    /**
     * The method adds a new edge to the algorithm.
     */
    fun setEdge(source: Long, target: Long)
    /**
     *
     */
    /**
     * The method gets all (sorted) nodes from the graph.
     */
    fun nodes(): Array<Long>

    /**
     * The method returns the node with the given ID
     */
    fun node(id: Long): DagreNodeOptions

    /**
     * The method gets the output graph with the current height and width.
     */
    fun graph(): DagreOutputGraph
}

enum class DagreGraphAlign {
    UL, UR, DL, DR
}

external interface DagreGraphOptions {
    /**
     * Alignment for rank nodes. Can be UL, UR, DL, or DR, where U = up, D = down, L = left, and R = right.
     */
    var align: DagreGraphAlign
    /**
     * Number of pixels to use as a margin around the left and right of the graph.
     */
    var marginx: Int
    /**
     * Number of pixels to use as a margin around the top and bottom of the graph.
     */
    var marginy: Int
    /**
     * Number of pixels that separate nodes horizontally in the layout.
     */
    var nodesep: Int
    /**
     * io.framed.exporter.crom.crom.Direction for rank nodes. Can be TB, BT, LR, or RL, where T = top, B = bottom, L = left, and R = right.
     */
    var rankdir: String
    /**
     * io.framed.exporter.crom.crom.Type of algorithm to assigns a rank to each node in the input graph. Possible values: `network-simplex`, `tight-tree` or `longest-path`
     */
    var ranker: String
    /**
     * Number of pixels between each rank in the layout.
     */
    var rankSep: Int

    var edgesep: Double
}

/**
 *
 */
fun dagreGraphOptions(init: DagreGraphOptions.() -> Unit = {}): DagreGraphOptions {
    val d = js("{}")
    init(d)
    return d
}

/**
 *
 */
external interface DagreNode {
    var id: Long
    var options: DagreNodeOptions
}

/**
 *
 */
fun dagreNode(init: DagreNode.() -> Unit = {}): DagreNode {
    val n = js("{}")
    init(n)
    return n
}

/**
 *
 */
external interface DagreNodeOptions {
    var width: Double
    var height: Double
    var label: String
    @JsName("y")
    var top: Double
    @JsName("x")
    var left: Double
}

/**
 * The interface binds the dagre output graph.
 */
external interface DagreOutputGraph {
    /**
     * height of the output graph
     */
    var height: Double
    /**
     * width of the output graph
     */
    var width: Double
}

fun dagreNodeOptions(init: DagreNodeOptions.() -> Unit = {}): DagreNodeOptions {
    val n = js("{}")
    init(n)
    return n
}

fun DagreGraph.setDefaultEdgeLabel() = asDynamic().setDefaultEdgeLabel {
    js("{}")
}