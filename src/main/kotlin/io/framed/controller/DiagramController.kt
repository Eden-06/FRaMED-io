package io.framed.controller

import io.framed.model.Diagram
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

    init {
        diagram.compartments.forEach {
            val c = CompartmentTypeController(it)
            navigationView.container.appendChild(c.view.html)
        }
    }
}

fun Diagram.controller() = DiagramController(this)