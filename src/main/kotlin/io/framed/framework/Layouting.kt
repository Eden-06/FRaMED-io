package io.framed.framework

import io.framed.framework.pictogram.BoxShape
import io.framed.framework.pictogram.Connection
import io.framed.framework.pictogram.Shape

object Layouting {
    fun autoLayout(container: BoxShape, connections: Set<Connection>, modelLinker: ModelLinker<*, *, *>) {
        val graph = Dagre.getGraph()
        graph.setDefaultEdgeLabel()
        graph.setGraph(dagreGraphOptions {
            nodesep = 50
            rankSep = 100
            marginx = 12
            marginy = 12
        })
        console.log(container.shapes)
        val list = container.shapes.mapNotNull { shape ->
            val id = shape.id ?: return@mapNotNull null
            graph.setNode(id, dagreNodeOptions {
                label = "shape-" + shape.id
                height = shape.height
                width = shape.width
            })

            fun findChildren(shape: Shape): List<Long> = listOfNotNull(shape.id) +
                    if (shape is BoxShape) shape.shapes.flatMap { findChildren(it) } else emptyList()

            id to findChildren(shape).toSet()
        }.toMap()

        for (connection in connections) {
            val source = connection.source.value
            val target = connection.target.value

            console.log(source, target)

            var sourceId: Long? = null
            var targetId: Long? = null

            for ((id, children) in list) {
                if (source in children) {
                    sourceId = id
                }
                if (target in children) {
                    targetId = id
                }
            }
            if (sourceId == null || targetId == null) continue

            graph.setEdge(sourceId, targetId)
        }
        Dagre.layout(graph)

        container.shapes.forEach { shape ->
            val id = shape.id ?: return@forEach
            val options = graph.node(id)
            shape.top = options.top
            shape.left = options.left
            shape.top = options.top - (shape.height / 2)
            shape.left = options.left - (shape.width / 2)
        }

        modelLinker.updateSize(true)
        modelLinker.updatePorts(true)

        for (linker in modelLinker.shapeLinkers) {
            if (linker is ModelLinker<*, *, *>) {
                linker.updatePorts(true)
            }
        }
    }
}