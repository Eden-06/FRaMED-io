package io.framed.view

import io.framed.controller.ContainerController
import io.framed.getCookie
import io.framed.model.Container
import io.framed.setCookie
import io.framed.util.EventHandler
import org.w3c.dom.HTMLDivElement
import kotlin.browser.document
import kotlin.browser.window
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
            workspace.appendChild(value.navigationView.html)

            updateToolbar()
        }

    private var toolbarListeners: Map<EventHandler<String>, (String) -> Unit> = emptyMap()
    private fun updateToolbar() {
        leftBar.clear()

        toolbarListeners.forEach { (handler, listener) ->
            handler.removeListener(listener)
        }
        toolbarListeners = emptyMap()

        var c: ContainerController? = controller
        while (c != null) {
            c = c.let { cont ->
                leftBar.prepand(TextView(cont.container.name).apply {
                    click {
                        controller = cont
                    }

                    toolbarListeners += cont.nameChange to cont.nameChange {
                        text = it
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
    private val themeIcon = IconView().also { icon ->
        rightBar += icon
        icon.icon = MaterialIcon.PALETTE

        val validity = 24 * 60 * 60

        icon.click.on {
            val isDark = document.getCookie("theme") == "dark"
            dialog {
                title = "Switch to ${if (isDark) "light" else "dark"} theme"
                contentView.textView("To change the theme the page must be reloaded. All unsaved changes will go lost.")
                closable = true
                addButton("Reload", true) {
                    document.setCookie("theme", if (isDark) "light" else "dark", validity)

                    window.location.reload()
                }
                addButton("Abort")
            }.open()
        }
    }

    val propertyBar = ListView().also {
        html.appendChild(it.html)
        it.classes += "propertyBar"
    }

    val touchpadControl: Boolean
        get() = controlMethodView.icon == MaterialIcon.TOUCH_APP

    private val workspace = createView<HTMLDivElement>().also {
        html.appendChild(it)
        it.classList.add("workspace")
    }

    private val colorDiv = (document.createElement("div") as HTMLDivElement).also {
        html.appendChild(it)
        it.classList.add("color-box")
    }

    val backgroundColor: String
        get() = window.getComputedStyle(colorDiv).backgroundColor
    val textColor: String
        get() = window.getComputedStyle(colorDiv).color
}