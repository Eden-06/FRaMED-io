import io.framed.framework.*
import io.framed.framework.pictogram.BoxShape
import io.framed.framework.pictogram.Connection

object Layouting {
    fun autoLayout(container: BoxShape, connections: Set<Connection>) {
        val graph = Dagre.getGraph()
        graph.setDefaultEdgeLabel()
        graph.setGraph(dagreGraphOptions {
            rankSep = 70
            rankdir = "BT"
            ranker = "network-simplex"
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

        container.shapes.forEach { shape ->
            val id = shape.id ?: return@forEach
            val options = graph.node(id)
            shape.top = options.top
            shape.left = options.left
        }

        val o = graph.graph()

        console.log(o)

        container.height = o.height
        container.width = o.width
    }
}