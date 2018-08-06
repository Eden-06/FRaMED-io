package io.framed.controller

import io.framed.JsPlumbConnection
import io.framed.jsPlumbConnect
import io.framed.jsPlumbPaintStyle
import io.framed.model.Relation
import io.framed.view.*
import org.w3c.dom.events.MouseEvent

/**
 * @author lars
 */
class RelationController(
        private val relation: Relation,
        val parent: ContainerController
) : NamedController() {

    override var name: String
        get() = relation.name
        set(value) {
            relation.name = value.trim()
            onNameChange.fire(value.trim())
        }

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

    private val nameView = InputView().also {
        it.value = name
        it.classes += "front-end-label"
    }

    private fun drawInheritance(sourceController: View<*>, targetController: View<*>, sourceCardinality: String, targetCardinality: String) {
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

            overlays = arrayOf(
                    arrayOf("Arrow", object {
                        val width = 20
                        val length = 20
                        val location = 1
                        val cssClass = "front-end-arrow-inheritance"
                    }),
                    arrayOf("Custom", object {
                        val create = { component: dynamic ->
                            nameView.html
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

    private fun drawAssociation(sourceController: View<*>, targetController: View<*>, sourceCardinality: String, targetCardinality: String) {
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

            overlays = arrayOf(
                    arrayOf("Custom", object {
                        val create = { component: dynamic ->
                            nameView.html
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

    private fun drawAggregation(sourceController: View<*>, targetController: View<*>, sourceCardinality: String, targetCardinality: String) {
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

            overlays = arrayOf(
                    arrayOf("Diamond", object {
                        val width = 15
                        val length = 25
                        val location = 1
                        val cssClass = "front-end-arrow-aggregation"
                    }),
                    arrayOf("Custom", object {
                        val create = { component: dynamic ->
                            nameView.html
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

        // Click area
        connections += jsPlumbInstance.connect(jsPlumbConnect {
            source = sourceController.html
            target = targetController.html

            anchor = arrayOf("Top", "Left", "Bottom", "Right")
            connector = arrayOf("Flowchart", object {
                val cornerRadius = 5
            })
            endpoint = "Blank"

            paintStyle = jsPlumbPaintStyle {
                stroke = "transparent"
                strokeWidth = 15
            }
        })


        when (relation.type) {
            Relation.Type.INHERITANCE -> drawInheritance(sourceController, targetController, relation.sourceCardinality, relation.targetCardinality)
            Relation.Type.ASSOCIATION -> drawAssociation(sourceController, targetController, relation.sourceCardinality, relation.targetCardinality)
            Relation.Type.AGGREGATION -> drawAggregation(sourceController, targetController, relation.sourceCardinality, relation.targetCardinality)
        }

        sidebar.setup {
            title("Relation")

            input("Name", name) {
                name = it
            }.also { i ->
                onNameChange {
                    i.value = it
                }
            }
        }

        nameView.onChange {
            name = it
        }
        onNameChange {
            nameView.value = it
        }

        connections.forEach { c ->
            c.bind("click") { conn, event ->
                event.stopPropagation()
                sidebar.display()
            }
            c.bind("contextmenu") { conn, event ->
                (event as? MouseEvent)?.let { e ->
                    e.stopPropagation()
                    e.preventDefault()
                    contextMenu {
                        title = "Relation"
                        addItem(MaterialIcon.DELETE, "Delete") {
                            parent.removeRelation(relation)
                        }
                    }.open(e.clientX.toDouble(), e.clientY.toDouble())
                }
            }
        }
    }
}