package io.framed.render.html

import io.framed.JsPlumbConnection
import io.framed.JsPlumbInstance
import io.framed.jsPlumbConnect
import io.framed.jsPlumbPaintStyle
import io.framed.model.RelationType
import io.framed.picto.ContextEvent
import io.framed.picto.Relation
import io.framed.picto.Shape
import io.framed.picto.SidebarEvent
import io.framed.util.point
import io.framed.view.InputView
import io.framed.view.View
import org.w3c.dom.events.MouseEvent

/**
 * @author lars
 */
class HtmlRelation(
        val relation: Relation,
        val jsPlumbInstance: JsPlumbInstance,
        val renderer: HtmlRenderer
) {

    private var connections: List<JsPlumbConnection> = emptyList()
    fun remove() {
        connections.forEach {
            jsPlumbInstance.deleteConnection(it)
        }
        connections = emptyList()
    }

    val views = relation.labels.map { (shape, position) ->
        InputView(shape.property).also {
            it.draggable = View.DragType.MARGIN
            it.autocomplete = shape.autocomplete
        } to position
    }

    private fun drawInheritance(sourceView: View<*>, targetView: View<*>) {
        connections += jsPlumbInstance.connect(jsPlumbConnect {
            source = sourceView.html
            target = targetView.html

            anchor = arrayOf("Top", "Left", "Bottom", "Right")
            connector = arrayOf("Flowchart", object {
                val cornerRadius = 5
            })
            endpoint = "Blank"

            paintStyle = jsPlumbPaintStyle {
                stroke = "black"
                strokeWidth = 1
            }

            overlays = arrayOf(
                    arrayOf("Arrow", object {
                        val width = 20
                        val length = 20
                        val location = 1
                        val cssClass = "front-end-arrow-inheritance"
                        val foldback = 1.0
                    })
            ) + views.map { (view, position) ->
                arrayOf("Custom", object {
                    val create = { _: dynamic ->
                        view.html
                    }
                    val cssClass = "front-end-label input-view"
                    val location = position
                })
            }
        })
    }

    private fun drawAssociation(sourceView: View<*>, targetView: View<*>) {
        connections += jsPlumbInstance.connect(jsPlumbConnect {
            source = sourceView.html
            target = targetView.html

            anchor = arrayOf("Top", "Left", "Bottom", "Right")
            connector = arrayOf("Flowchart", object {
                val cornerRadius = 5
            })
            endpoint = "Blank"

            paintStyle = jsPlumbPaintStyle {
                stroke = "black"
                strokeWidth = 1
            }

            overlays = views.map { (view, position) ->
                arrayOf("Custom", object {
                    val create = { _: dynamic ->
                        view.html
                    }
                    val cssClass = "front-end-label input-view"
                    val location = position
                })
            }.toTypedArray()
        })
    }

    private fun drawAggregation(sourceView: View<*>, targetView: View<*>) {
        connections += jsPlumbInstance.connect(jsPlumbConnect {
            source = sourceView.html
            target = targetView.html

            anchor = arrayOf("Top", "Left", "Bottom", "Right")
            connector = arrayOf("Flowchart", object {
                val cornerRadius = 5
            })
            endpoint = "Blank"

            paintStyle = jsPlumbPaintStyle {
                stroke = "black"
                strokeWidth = 1
            }

            overlays = arrayOf(
                    arrayOf("Diamond", object {
                        val width = 15
                        val length = 25
                        val location = 1
                        val cssClass = "front-end-arrow-aggregation"
                    })
            ) + views.map { (view, position) ->
                arrayOf("Custom", object {
                    val create = { _: dynamic ->
                        view.html
                    }
                    val cssClass = "front-end-label input-view"
                    val location = position
                })
            }
        })
    }

    fun draw() {
        draw(relation.source.get(), relation.target.get())
    }

    fun draw(sourceShape: Shape, targetShape: Shape) {
        remove()

        val sourceView = renderer[sourceShape]
        val targetView = renderer[targetShape]

        // Click area
        connections += jsPlumbInstance.connect(jsPlumbConnect {
            source = sourceView.html
            target = targetView.html

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


        when (relation.type.get()) {
            RelationType.INHERITANCE -> drawInheritance(sourceView, targetView)
            RelationType.ASSOCIATION -> drawAssociation(sourceView, targetView)
            RelationType.AGGREGATION -> drawAggregation(sourceView, targetView)
        }

        connections.forEach { c ->
            if (relation.hasSidebar) {
                c.bind("click") { _, event ->
                    event.stopPropagation()
                    relation.onSidebar.fire(SidebarEvent(relation))
                }
            }

            if (relation.hasContext) {
                c.bind("contextmenu") { _, event ->
                    (event as? MouseEvent)?.let { e ->
                        e.stopPropagation()
                        e.preventDefault()
                        relation.onContext.fire(ContextEvent(e.point(), relation))
                    }
                }
            }
        }
    }

    init {
        draw()

        relation.source.onChange {
            draw()
        }
        relation.target.onChange {
            draw()
        }
        relation.type.onChange {
            draw()
        }
    }
}