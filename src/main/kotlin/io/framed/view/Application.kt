package io.framed.view

import io.framed.controller.ContainerController
import io.framed.model.Container
import org.w3c.dom.HTMLDivElement
import kotlin.dom.clear

/**
 * Main view.
 *
 * @author lars
 */
class Application : View<HTMLDivElement>("div") {

    /**
     * Uml part to draw.
     */
    var controller: ContainerController = ContainerController(Container(), null)
        set(value) {
            field = value
            value.application = this
            value.touchpadControl = touchpadControl

            workspace.clear()
            workspace.appendChild(value.view.html)

            updateToolbar()
        }

    private fun updateToolbar() {
        leftBar.clear()

        var c: ContainerController? = controller
        while (c != null) {
            c = c.let { cont ->
                leftBar.insertBegin(TextView(cont.container.name).apply {
                    click.on {
                        controller = cont
                    }
                })

                cont.parent
            }
        }
    }

    private val toolbar = ListView().also {
        html.appendChild(it.html)
        it.classes += "toolbar"
    }

    private val leftBar = ListView().also {
        toolbar += it
        it.classes += "left-bar"
    }
    private val rightBar = ListView().also {
        toolbar += it
        it.classes += "right-bar"
    }
    private val controlMethodView = IconView().also { icon ->
        rightBar += icon
        icon.icon = MaterialIcon.MOUSE

        icon.click.on {
            icon.icon = if (touchpadControl) {
                MaterialIcon.MOUSE
            } else {
                MaterialIcon.TOUCH_APP
            }

            controller.touchpadControl = touchpadControl
        }
    }

    val touchpadControl: Boolean
        get() = controlMethodView.icon == MaterialIcon.TOUCH_APP

    private val workspace = createView<HTMLDivElement>().also {
        html.appendChild(it)
        it.classList.add("workspace")
    }
}