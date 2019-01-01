/**
 * The class binds the Dagre-library to the middleware.
 */
external class Dagre {
    companion object {
        /**
         * Get a new directed Dagre Graph.
         */
        fun getGraph(): DagreGraph
    }

    /**
     * The method sorts all nodes according to the current options.
     */
    fun layout(graph: DagreGraph)
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
    var sourceId: String
    var targetId: String
    /**
     * The weight to assign edges. Higher weight edges are generally made shorter and straighter than lower weight edges.
     */
    var weight: Int
}
external interface DagreGraph {
    /**
     * The method sets the general settings for the algorithm.
     */
    fun setGraph(options: DagreGraphOptions)

    /**
     * The method adds a new node to the algorithm.
     */
    fun setNode(node: DagreNode)
    /**
     * The method adds a new edge to the algorithm.
     */
    fun setEdge(edge: DagreEdge)
    /**
     * The method gets all (sorted) nodes from the graph.
     */
    fun getNodes()
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
     * Direction for rank nodes. Can be TB, BT, LR, or RL, where T = top, B = bottom, L = left, and R = right.
     */
    var rankdir: RankerDirection
    /**
     * Type of algorithm to assigns a rank to each node in the input graph. Possible values: network-simplex, tight-tree or longest-path
     */
    var ranker: RankerTechnique
    /**
     * Number of pixels between each rank in the layout.
     */
    var rankSep: Int
}
external interface DagreNode {
    var id: Int
    var options: DagreNodeOptions
}
external interface DagreNodeOptions {
    var width: Double
    var height: Double
    /// Hier können noch weitere Attribute kommen
}
enum class RankerDirection {
    TB, BT, LR, RL
}
enum class RankerTechnique(val jsName: String) {
    NETWORK_SIMPLEX("network-simplex"),
    TIGHT_TREE("tight-tree"),
    LONGEST_PATH("longest-path")
}