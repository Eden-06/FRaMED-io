package io.framed.framework

import io.framed.framework.pictogram.BoxShape
import io.framed.framework.pictogram.Connection

object Layouting {
    fun autoLayout(container: BoxShape, connections: Set<Connection>) {
        val graph = Dagre.getGraph()
        graph.setDefaultEdgeLabel()
        graph.setGraph(dagreGraphOptions {
            nodesep = 30
        })
        val list = container.shapes.mapNotNull { shape ->
            val id = shape.id ?: return@mapNotNull null
            graph.setNode(id, dagreNodeOptions {
                label = "shape-" + shape.id
                height = shape.height
                width = shape.width
            })
            id
        }
        connections.forEach { connection ->
            val source = connection.source.value
            val target = connection.target.value
            if (source in list && target in list) {
                graph.setEdge(
                        connection.source.value,
                        connection.target.value
                )
            }
        }
        Dagre.layout(graph)
        var minX = 20.0
        var maxX = 0.0
        var minY = 20.0
        var maxY = 0.0
        container.shapes.forEach { shape ->
            val id = shape.id ?: return@forEach
            val options = graph.node(id)
            shape.top = options.top
            shape.left = options.left
            shape.top = options.top - (shape.height/2)
            shape.left = options.left - (shape.width/2)
            /*
            if (shape.left < minX) {
                minX = shape.left
            }
            if ((shape.left + shape.width) > maxX) {
                maxX = (shape.left + shape.width)
            }
            if (shape.top < minY) {
                minY = shape.top
            }
            if ((shape.top + shape.height) > maxY) {
                maxY = (shape.top + shape.height)
            }
            */
        }
        container.parent?.height = (minY + maxY + container.topOffset - (container.parent?.topOffset ?: 0.0))
        container.parent?.width = (minX + maxX + container.leftOffset - (container.parent?.leftOffset ?: 0.0))
    }
}