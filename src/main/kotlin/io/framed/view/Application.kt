package io.framed.view

import io.framed.controller.ContainerController
import io.framed.model.Container
import io.framed.render.html.HtmlRenderer
import io.framed.util.EventHandler
import io.framed.util.getCookie
import io.framed.util.setCookie
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.events.MouseEvent
import kotlin.browser.document
import kotlin.browser.window
import kotlin.math.max

/**
 * Main view.
 *
 * @author lars
 */
class Application : View<HTMLDivElement>("div") {

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
                leftBar.prepand(TextView().apply {
                    onClick {
                        controller = cont
                    }

                    bind(cont.nameProperty)
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
    /*
    private val controlMethodView = IconView().also { icon ->
        rightBar += icon
        icon.icon = MaterialIcon.MOUSE

        icon.onClick {
            icon.icon = if (touchpadControl) {
                MaterialIcon.MOUSE
            } else {
                MaterialIcon.TOUCH_APP
            }

            controller.touchpadControl = touchpadControl
        }
    }
    */
    private val themeIcon = IconView().also { icon ->
        rightBar += icon
        icon.icon = MaterialIcon.PALETTE

        val validity = 24 * 60 * 60

        icon.onClick {
            val isDark = document.getCookie("theme") == "dark"
            dialog {
                title = "Switch to ${if (isDark) "light" else "dark"} theme"
                contentView.textView("To onChange the theme the page must be reloaded. All unsaved changes will go lost.")
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
        it.classes += "property-bar"
    }

    val propertyBarResizer = ListView().also { resizer ->
        html.appendChild(resizer.html)
        resizer.classes += "property-bar-resizer"

        var up: ((MouseEvent) -> Unit) = {}

        resizer.onMouseDown {
            it.preventDefault()
            Root.classes += "resize-ew"
            val move = Root.onMouseMove { e ->
                e.preventDefault()
                val w = (clientWidth - e.clientX).toDouble()

                if (w < 50) {
                    propertyBar.visible = false

                    resizer.right = 0.0
                    resizer.classes += "side"
                    workspace.right = 0.0
                } else {
                    propertyBar.visible = true
                    val newWidth = max(w, 150.0)

                    propertyBar.width = newWidth
                    resizer.right = newWidth
                    resizer.classes -= "side"
                    workspace.right = newWidth
                }

            }
            up = Root.onMouseUp { e ->
                e.preventDefault()
                Root.classes -= "resize-ew"
                Root.onMouseMove.removeListener(move)
                Root.onMouseUp.removeListener(up)
            }
        }
    }

    /*
    val touchpadControl: Boolean
        get() = controlMethodView.icon == MaterialIcon.TOUCH_APP
    */

    private val workspace = ListView().also {
        html.appendChild(it.html)
        it.classes += "workspace"
    }

    private val colorDiv = (document.createElement("div") as HTMLDivElement).also {
        html.appendChild(it)
        it.classList.add("color-box")
    }

    var controller: ContainerController = ContainerController(Container(), this, null)
        set(value) {
            field = value
            value.showSidebar()

            renderer.render(value.viewModel)

            updateToolbar()
        }

    val renderer = HtmlRenderer(workspace)

    val backgroundColor: String
        get() = window.getComputedStyle(colorDiv).backgroundColor
    val textColor: String
        get() = window.getComputedStyle(colorDiv).color
}