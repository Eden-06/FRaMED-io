package io.framed.controller

import io.framed.JsPlumbConnect
import io.framed.JsPlumbPaintStyle
import io.framed.model.Relation
import io.framed.view.View

/**
 * @author lars
 */
class RelationController(
        val relation: Relation,
        val parent: ContainerController
) : Controller {

    override val view: View<*>
        get() = throw UnsupportedOperationException("A relation does not provide its on view")

    init {
        val jsPlumbInstance = parent.jsPlumbInstance

        val sourceController = parent.views[relation.source]
        val targetController = parent.views[relation.target]

        if (sourceController == null || targetController == null) {
            throw IllegalArgumentException()
        }

        jsPlumbInstance.connect(JsPlumbConnect {
            source = sourceController.html
            target = targetController.html

            anchor = arrayOf("Top", "Left", "Bottom", "Right")
            connector = arrayOf("Flowchart", object {
                val cornerRadius = 5
            })
            endpoint = "Blank"

            paintStyle = JsPlumbPaintStyle {
                stroke = "black"
                strokeWidth = 8
                dashstyle = "1 0.3"
            }
        })
        jsPlumbInstance.connect(JsPlumbConnect {
            source = sourceController.html
            target = targetController.html

            anchor = arrayOf("Top", "Left", "Bottom", "Right")
            connector = arrayOf("Flowchart", object {
                val cornerRadius = 5
            })
            endpoint = "Blank"

            paintStyle = JsPlumbPaintStyle {
                stroke = "white"
                strokeWidth = 5.5
            }
        })
        jsPlumbInstance.connect(JsPlumbConnect {
            source = sourceController.html
            target = targetController.html

            anchor = arrayOf("Top", "Left", "Bottom", "Right")
            connector = arrayOf("Flowchart", object {
                val cornerRadius = 5
            })
            endpoint = "Blank"

            paintStyle = JsPlumbPaintStyle {
                stroke = "black"
                strokeWidth = 1.5
            }
        })
    }
}