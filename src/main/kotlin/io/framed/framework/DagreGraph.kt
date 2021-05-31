@file:JsModule("dagre")
@file:JsNonModule
@file:JsQualifier("graphlib")

package io.framed.framework

/**
 * Get a new directed Dagre Graph.
 */
external class Graph() : DagreGraph {
    override fun setGraph(options: DagreGraphOptions)
    override fun setNode(id: Long, node: DagreNodeOptions)
    override fun setEdge(source: Long, target: Long)
    override fun nodes(): Array<Long>
    override fun node(id: Long): DagreNodeOptions
    override fun graph(): DagreOutputGraph
}
