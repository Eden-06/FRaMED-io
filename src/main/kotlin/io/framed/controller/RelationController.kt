package io.framed.controller

import io.framed.JsPlumbConnection
import io.framed.jsPlumbConnect
import io.framed.jsPlumbPaintStyle
import io.framed.model.Relation
import io.framed.util.Property
import io.framed.util.point
import io.framed.view.*
import org.w3c.dom.events.MouseEvent

/**
 * @author lars
 */
class RelationController(
        private val relation: Relation,
        val parent: ContainerController
) : Controller {

    private val nameProperty = Property(relation::name)
    private val sourceCardinalityProperty = Property(relation::sourceCardinality)
    private val targetCardinalityProperty = Property(relation::targetCardinality)

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

    private val nameView = InputView().apply {
        classes += "front-end-label"
        bind(nameProperty)
    }

    private val sourceCardinalityView = InputView().apply {
        classes += "front-end-label"
        bind(sourceCardinalityProperty)
    }

    private val targetCardinalityView = InputView().apply {
        classes += "front-end-label"
        bind(targetCardinalityProperty)
    }

    private fun drawInheritance(sourceController: View<*>, targetController: View<*>) {
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
                        val foldback = 1.0
                    }),
                    arrayOf("Custom", object {
                        val create = { _: dynamic ->
                            nameView.html
                        }
                        val cssClass = "front-end-label"
                        val location = 0.5
                    }),
                    arrayOf("Custom", object {
                        val create = { _: dynamic ->
                            sourceCardinalityView.html
                        }
                        val cssClass = "front-end-label"
                        val location = 0.1
                    }),
                    arrayOf("Custom", object {
                        val create = { _: dynamic ->
                            targetCardinalityView.html
                        }
                        val cssClass = "front-end-label"
                        val location = 0.9
                    })

            )
        })
    }

    private fun drawAssociation(sourceController: View<*>, targetController: View<*>) {
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
                        val create = { _: dynamic ->
                            nameView.html
                        }
                        val cssClass = "front-end-label"
                        val location = 0.5
                    }),
                    arrayOf("Custom", object {
                        val create = { _: dynamic ->
                            sourceCardinalityView.html
                        }
                        val cssClass = "front-end-label"
                        val location = 0.1
                    }),
                    arrayOf("Custom", object {
                        val create = { _: dynamic ->
                            targetCardinalityView.html
                        }
                        val cssClass = "front-end-label"
                        val location = 0.9
                    })
            )
        })
    }

    private fun drawAggregation(sourceController: View<*>, targetController: View<*>) {
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
                        val create = { _: dynamic ->
                            nameView.html
                        }
                        val cssClass = "front-end-label"
                        val location = 0.5
                    }),
                    arrayOf("Custom", object {
                        val create = { _: dynamic ->
                            sourceCardinalityView.html
                        }
                        val cssClass = "front-end-label"
                        val location = 0.1
                    }),
                    arrayOf("Custom", object {
                        val create = { _: dynamic ->
                            targetCardinalityView.html
                        }
                        val cssClass = "front-end-label"
                        val location = 0.9
                    })
            )
        })
    }

    fun draw() {
        remove()

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
            Relation.Type.INHERITANCE -> drawInheritance(sourceController, targetController)
            Relation.Type.ASSOCIATION -> drawAssociation(sourceController, targetController)
            Relation.Type.AGGREGATION -> drawAggregation(sourceController, targetController)
        }

        connections.forEach { c ->
            c.bind("click") { _, event ->
                event.stopPropagation()
                sidebar.display()
            }
            c.bind("contextmenu") { _, event ->
                (event as? MouseEvent)?.let { e ->
                    e.stopPropagation()
                    e.preventDefault()
                    contextMenu {
                        title = "Relation"
                        addItem(MaterialIcon.DELETE, "Delete") {
                            parent.removeRelation(relation)
                        }
                    }.open(e.point())
                }
            }
        }
    }

    init {
        nameView.draggable = View.DragType.MARGIN
        sourceCardinalityView.draggable = View.DragType.MARGIN
        targetCardinalityView.draggable = View.DragType.MARGIN

        sidebar.setup {
            title("Relation")

            input("Name").bind(nameProperty)
            input("Source cardinality").bind(sourceCardinalityProperty)
            input("Target cardinality").bind(targetCardinalityProperty)

            val types = Relation.Type.values().toList()
            select("Type", types, relation.type) {
                relation.type = it
                draw()
            }

            button("Toggle direction") {
                val source = relation.source
                relation.source = relation.target
                relation.target = source

                draw()
            }
        }

        draw()
    }
}