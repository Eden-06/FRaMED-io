package io.framed.controller

import io.framed.JsPlumbConnection
import io.framed.jsPlumbConnect
import io.framed.jsPlumbPaintStyle
import io.framed.model.Relation
import io.framed.view.Sidebar
import io.framed.view.View

/**
 * @author lars
 */
class RelationController(
        relation: Relation,
        val parent: ContainerController
) : Controller {

    override val view: View<*>
        get() = throw UnsupportedOperationException("A relation does not provide its on view")

    override val sidebar: Sidebar = parent.createSidebar()

    private var connections: List<JsPlumbConnection> = emptyList()

    val jsPlumbInstance = parent.jsPlumbInstance

    /**
     * Remove this relation.
     */
    fun remove() {
        connections.forEach {
            jsPlumbInstance.deleteConnection(it)
        }
        connections = emptyList()
    }


    init {
        val sourceController = parent.views[relation.source]
        val targetController = parent.views[relation.target]

        if (sourceController == null || targetController == null) {
            throw IllegalArgumentException()
        }

        connections += jsPlumbInstance.connect(jsPlumbConnect {
            source = sourceController.html
            target = targetController.html

            anchor = arrayOf("Top", "Left", "Bottom", "Right")
            connector = arrayOf("Flowchart", object {
                val cornerRadius = 5
            })
            endpoint = "Blank"

            paintStyle = jsPlumbPaintStyle {
                stroke = parent.application?.textColor ?: "black"
                strokeWidth = 8
                dashstyle = "1 0.3"
            }
        })
        connections += jsPlumbInstance.connect(jsPlumbConnect {
            source = sourceController.html
            target = targetController.html

            anchor = arrayOf("Top", "Left", "Bottom", "Right")
            connector = arrayOf("Flowchart", object {
                val cornerRadius = 5
            })
            endpoint = "Blank"

            paintStyle = jsPlumbPaintStyle {
                stroke = parent.application?.backgroundColor ?: "white"
                strokeWidth = 5.5
            }
        })
        connections += jsPlumbInstance.connect(jsPlumbConnect {
            source = sourceController.html
            target = targetController.html

            anchor = arrayOf("Top", "Left", "Bottom", "Right")
            connector = arrayOf("Flowchart", object {
                val cornerRadius = 5
            })
            endpoint = "Blank"

            paintStyle = jsPlumbPaintStyle {
                stroke = parent.application?.textColor ?: "black"
                strokeWidth = 1.5
            }
        })
    }
}