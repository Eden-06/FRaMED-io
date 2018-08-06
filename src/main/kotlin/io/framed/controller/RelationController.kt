package io.framed.controller

import io.framed.JsPlumbConnection
import io.framed.jsPlumbConnect
import io.framed.jsPlumbPaintStyle
import io.framed.model.Relation
import io.framed.view.InputView
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

    private fun drawInheritance(sourceController: View<*>, targetController: View<*>, name: String, sourceCardinality: String, targetCardinality: String) {
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
                strokeWidth = 1
            }

            val l = InputView()
            l.value = name
            l.classes += "front-end-label"

            overlays = arrayOf(
                    arrayOf("Arrow", object {
                        val width = 20
                        val length = 20
                        val location = 1
                        val cssClass = "front-end-arrow-inheritance"
                    }),
                    arrayOf("Custom", object {
                        val create = { component: dynamic ->
                            l.html
                        }
                        val cssClass = "front-end-label"
                        val location = 0.5
                    }),
                    arrayOf("Label", object {
                        val label = sourceCardinality
                        val cssClass = "front-end-label"
                        val location = 0.1
                    }),
                    arrayOf("Label", object {
                        val label = targetCardinality
                        val cssClass = "front-end-label"
                        val location = 0.9
                    })

            )
        })
    }

    private fun drawAssociation(sourceController: View<*>, targetController: View<*>, name: String, sourceCardinality: String, targetCardinality: String) {
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
                strokeWidth = 1
            }

            val l = InputView()
            l.value = name
            l.classes += "front-end-label"

            overlays = arrayOf(
                    arrayOf("Custom", object {
                        val create = { component: dynamic ->
                            l.html
                        }
                        val cssClass = "front-end-label"
                        val location = 0.5
                    }),
                    arrayOf("Label", object {
                        val label = sourceCardinality
                        val cssClass = "front-end-label"
                        val location = 0.1
                    }),
                    arrayOf("Label", object {
                        val label = targetCardinality
                        val cssClass = "front-end-label"
                        val location = 0.9
                    })

            )
        })
    }

    private fun drawAggregation(sourceController: View<*>, targetController: View<*>, name: String, sourceCardinality: String, targetCardinality: String) {
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
                strokeWidth = 1
            }

            val l = InputView()
            l.value = name
            l.classes += "front-end-label"

            overlays = arrayOf(
                    arrayOf("Diamond", object {
                        val width = 15
                        val length = 25
                        val location = 1
                        val cssClass = "front-end-arrow-aggregation"
                    }),
                    arrayOf("Custom", object {
                        val create = { component: dynamic ->
                            l.html
                        }
                        val cssClass = "front-end-label"
                        val location = 0.5
                    }),
                    arrayOf("Label", object {
                        val label = sourceCardinality
                        val cssClass = "front-end-label"
                        val location = 0.1
                    }),
                    arrayOf("Label", object {
                        val label = targetCardinality
                        val cssClass = "front-end-label"
                        val location = 0.9
                    })

            )
        })
    }

    init {
        val sourceController = parent.views[relation.source]
        val targetController = parent.views[relation.target]

        if (sourceController == null || targetController == null) {
            throw IllegalArgumentException()
        }

        when (relation.type) {
            Relation.Type.INHERITANCE -> drawInheritance(sourceController, targetController, relation.name, relation.sourceCardinality, relation.targetCardinality)
            Relation.Type.ASSOCIATION -> drawAssociation(sourceController, targetController, relation.name, relation.sourceCardinality, relation.targetCardinality)
            Relation.Type.AGGREGATION -> drawAggregation(sourceController, targetController, relation.name, relation.sourceCardinality, relation.targetCardinality)
        }
    }
}