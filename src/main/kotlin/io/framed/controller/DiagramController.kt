package io.framed.controller

import io.framed.arr
import io.framed.async
import io.framed.jsPlumb
import io.framed.model.CompartmentType
import io.framed.model.Diagram
import io.framed.obj
import io.framed.view.NavigationView
import io.framed.view.View

/**
 * @author lars
 */
class DiagramController(
        val diagram: Diagram
) : Controller {
    override val view: View<*>
        get() = navigationView

    private val navigationView = NavigationView()

    var compartments: Map<CompartmentType, View<*>> = emptyMap()

    init {
        val jsPlumbInstance = jsPlumb.getInstance()
        jsPlumbInstance.setContainer(navigationView.container)

        navigationView.zoomListener.on {
            jsPlumbInstance.setZoom(it)
        }
        jsPlumbInstance.setZoom(1.0)

        diagram.compartments.forEach {
            val c = CompartmentTypeController(it)
            navigationView.container.appendChild(c.view.html)
            compartments += it to c.view
        }

        async {
            compartments.values.forEach {
                jsPlumbInstance.draggable(it.html)
            }

            diagram.relations.forEach {
                jsPlumbInstance.connect(obj {
                    source = compartments[it.source]!!.html
                    target = compartments[it.target]!!.html

                    anchor = arr("Top", "Left", "Bottom", "Right")

                    connector = arr("Flowchart", obj { cornerRadius = 5 })

                    endpoint = "Blank"
                    paintStyle = obj {
                        stroke = "black"
                        strokeWidth = 8
                        dashstyle = "1 0.3"
                    }
                })
                jsPlumbInstance.connect(obj {
                    source = compartments[it.source]!!.html
                    target = compartments[it.target]!!.html

                    anchor = arr("Top", "Left", "Bottom", "Right")

                    connector = arr("Flowchart", obj { cornerRadius = 5 })

                    endpoint = "Blank"
                    paintStyle = obj {
                        stroke = "white"
                        strokeWidth = 5.5
                    }
                })
                jsPlumbInstance.connect(obj {
                    source = compartments[it.source]!!.html
                    target = compartments[it.target]!!.html

                    anchor = arr("Top", "Left", "Bottom", "Right")

                    connector = arr("Flowchart", obj { cornerRadius = 5 })

                    endpoint = "Blank"
                    paintStyle = obj {
                        stroke = "black"
                        strokeWidth = 1.5
                    }
                })
            }
        }
    }
}

fun Diagram.controller() = DiagramController(this)