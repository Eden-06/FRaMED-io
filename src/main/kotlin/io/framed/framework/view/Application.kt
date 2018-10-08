package io.framed.framework.view

import io.framed.File
import io.framed.framework.Controller
import io.framed.framework.ControllerManager
import io.framed.framework.render.html.HtmlRenderer
import io.framed.framework.util.getCookie
import io.framed.framework.util.setCookie
import io.framed.linker.ContainerLinker
import kotlinx.serialization.json.JSON
import org.w3c.dom.HTMLDivElement
import kotlin.browser.document
import kotlin.browser.window

/**
 * Main view.
 *
 * @author lars
 */
object Application : ViewCollection<View<*>, HTMLDivElement>("div") {

    private lateinit var actionUndo: IconView
    private lateinit var actionRedo: IconView

    private val toolBar = toolBar {

        //Left block
        action(ToolBar.Side.LEFT, MaterialIcon.ZOOM_IN) {

        }
        action(ToolBar.Side.LEFT, MaterialIcon.ZOOM_OUT) {

        }
        separator()
        actionUndo = action(ToolBar.Side.LEFT, MaterialIcon.UNDO) {

        }
        actionRedo = action(ToolBar.Side.LEFT, MaterialIcon.REDO) {

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
    }

    private val menuBar = menuBar {
        menu("File") {
            item("Open") {

            }
            item("Save") {
                ControllerManager.root?.let { root ->
                    val container = (root.linker as ContainerLinker).model

                    val file = File(container, ControllerManager.layers)

                    println(JSON.indented.stringify(file))
                }
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

    val propertyBar = propertyBar {
        onResize {
            workspace.right = clientWidth - it
        }
    }

    lateinit var tabBar: TabBar
    lateinit var workspace: ListView

    private val workspaceContainer = listView {
        classes += "workspace-container"
        workspace = listView {
            classes += "workspace"
        }
        tabBar = tabBar {

        }
    }

    var controllers: Map<Controller, Tab> = emptyMap()

    private lateinit var controller: Controller

    fun loadController(controller: Controller) {
        this.controller = controller

        val tab = controllers[controller] ?: tabBar.tab(controller.tabNameProperty).also { tab ->
            controllers += controller to tab

            tab.onOpen {
                loadController(controller)

                controller.linker.sidebar.open()
                renderer.render(controller.viewModel)
            }

            tab.onClose {
                controllers -= controller
            }
        }

        tab.open()
    }

    val renderer = HtmlRenderer(workspace)

    init {
        Root += this
    }

    fun init() {

    }
}