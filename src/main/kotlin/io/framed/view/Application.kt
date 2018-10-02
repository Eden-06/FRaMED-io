package io.framed.view

import io.framed.linker.ContainerLinker
import io.framed.model.Container
import io.framed.render.html.HtmlRenderer
import io.framed.util.EventHandler
import io.framed.util.getCookie
import io.framed.util.setCookie
import org.w3c.dom.HTMLDivElement
import kotlin.browser.document
import kotlin.browser.window

/**
 * Main view.
 *
 * @author lars
 */
class Application : ViewCollection<View<*>, HTMLDivElement>("div") {

    private var toolbarListeners: Map<EventHandler<String>, (String) -> Unit> = emptyMap()
    private fun updateToolbar() {
        positionBar.clear()

        toolbarListeners.forEach { (handler, listener) ->
            handler.removeListener(listener)
        }
        toolbarListeners = emptyMap()

        var c: ContainerLinker? = linker
        while (c != null) {
            c = c.let { cont ->
                positionBar.prepand(TextView().apply {
                    onClick {
                        linker = cont
                    }

                    bind(cont.nameProperty)
                })

                cont.parent
            }
        }
    }


    private val toolBar = toolBar {

        //Left block
        action(ToolBar.Side.LEFT, MaterialIcon.ZOOM_IN) {

        }
        action(ToolBar.Side.LEFT, MaterialIcon.ZOOM_OUT) {

        }
        separator()
        action(ToolBar.Side.LEFT, MaterialIcon.UNDO) {

        }
        action(ToolBar.Side.LEFT, MaterialIcon.REDO) {

        }

        // Right block
        action(ToolBar.Side.RIGHT, MaterialIcon.PALETTE) {
            val isDark = document.getCookie("theme") == "dark"
            dialog {
                title = "Switch to ${if (isDark) "light" else "dark"} theme"
                contentView.textView("To onChange the theme the page must be reloaded. All unsaved changes will go lost.")
                closable = true
                addButton("Reload", true) {
                    document.setCookie("theme", if (isDark) "light" else "dark", 24 * 60 * 60)

                    window.location.reload()
                }
                addButton("Abort")
            }.open()
        }

        /*
        action(ToolBar.Side.RIGHT, MaterialIcon.MOUSE) {iconView ->
            iconView.icon = if (touchpadControl) {
                MaterialIcon.MOUSE
            } else {
                MaterialIcon.TOUCH_APP
            }

            linker.touchpadControl = touchpadControl
        }
        */
    }

    private val menuBar = menuBar {
        menu("File") {
            item("Open") {

            }
            item("Save") {

            }
            item("Reset") {

            }
        }
        menu("Foo") {
            item("Bar 1") {}
            menu("Bar 2") {
                item("Lorem") {}
                item("Ipson") {}
            }
            item("Bar 3") {}
        }
    }

    var propertyBar = propertyBar {
        onResize {
            workspace.right = clientWidth - it
        }
    }

    /*
    val touchpadControl: Boolean
        get() = controlMethodView.icon == MaterialIcon.TOUCH_APP
    */

    private val workspace = listView {
        classes += "workspace"
    }

    private val colorDiv = (document.createElement("div") as HTMLDivElement).also {
        html.appendChild(it)
        it.classList.add("color-box")
    }

    var linker: ContainerLinker = ContainerLinker(Container(), this, null)
        set(value) {
            field = value
            value.showSidebar()

            renderer.render(value.viewModel)

            updateToolbar()
        }

    val renderer = HtmlRenderer(workspace)

    private val positionBar = listView {
        classes += "position-bar"
    }

    val backgroundColor: String
        get() = window.getComputedStyle(colorDiv).backgroundColor
    val textColor: String
        get() = window.getComputedStyle(colorDiv).color
}