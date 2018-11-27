package io.framed.framework.view

import de.westermann.kobserve.basic.property
import de.westermann.kobserve.not
import io.framed.File
import io.framed.framework.Controller
import io.framed.framework.ControllerManager
import io.framed.framework.render.Renderer
import io.framed.framework.render.html.HtmlRenderer
import io.framed.framework.util.*
import io.framed.linker.ConnectionManagerLinker
import io.framed.linker.ContainerLinker
import org.w3c.dom.HTMLDivElement
import kotlin.browser.document
import kotlin.browser.window
import kotlin.math.roundToInt

/**
 * Main view.
 *
 * @author lars
 */
object Application : ViewCollection<View<*>, HTMLDivElement>("div") {

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
    val renderer: Renderer = HtmlRenderer(workspace)

    private var zoomBackingField: Double = 1.0
    val zoomProperty = property(this::zoomBackingField)
    var zoom by zoomProperty

    private val toolBar = toolBar {

        custom(ToolBar.Side.LEFT) {
            selectView(NavigationView.zoomSteps, zoomProperty) { current: Double ->
                "${(current * 100).roundToInt()}%"
            }
        }

        //Left block
        action(ToolBar.Side.LEFT, MaterialIcon.ZOOM_IN) { _ ->
            val nextStep = NavigationView.zoomSteps.asSequence().filter { it > zoom }.min()
                    ?: NavigationView.zoomSteps.max()
            if (nextStep != null) {
                renderer.zoom = nextStep
            }
        }
        action(ToolBar.Side.LEFT, MaterialIcon.ZOOM_OUT) { _ ->
            val nextStep = NavigationView.zoomSteps.asSequence().filter { it < zoom }.max()
                    ?: NavigationView.zoomSteps.min()
            if (nextStep != null) {
                renderer.zoom = nextStep
            }
        }
        separator(ToolBar.Side.LEFT)
        action(ToolBar.Side.LEFT, MaterialIcon.UNDO) { _ ->
            if (History.canUndo) {
                History.undo()
            }
        }.inactiveProperty.bind(!History.canUndoProperty)
        action(ToolBar.Side.LEFT, MaterialIcon.REDO) { _ ->
            if (History.canRedo) {
                History.redo()
            }
        }.inactiveProperty.bind(!History.canRedoProperty)

        // Right block
        action(ToolBar.Side.RIGHT, MaterialIcon.PALETTE) { _ ->
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
                loadLocalFile { content ->
                    File.fromJSON(content)
                }
            }
            item("Save") {
                ControllerManager.root?.let { root ->
                    val linker = (root.linker as ContainerLinker)
                    val connections = linker.connectionManager as ConnectionManagerLinker

                    val file = File(linker.model, connections.modelConnections, ControllerManager.layers)

                    triggerDownload("${file.name}.json", file.toJSON())
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

    var controllers: Map<Controller, Tab> = emptyMap()

    private lateinit var controller: Controller

    fun loadController(controller: Controller) {
        if (this::controller.isInitialized) {
            if (this.controller == controller) {
                return
            }
            History.push(HistoryModelLinker(this.controller.linker, controller.linker))
        }

        this.controller = controller

        val tab = controllers[controller] ?: tabBar.tab(controller.tabNameProperty).also { tab ->
            controllers += controller to tab

            tab.onOpen {
                loadController(controller)

                controller.linker.sidebar.open()
                renderer.render(controller.viewModel)
                zoom = renderer.zoom
            }

            tab.onClose {
                controllers -= controller
            }
        }

        tab.open()
    }


    init {
        Root += this

        renderer.onZoom {
            zoom = it
        }
        zoomProperty.onChange {
            renderer.zoom = zoom
        }
    }

    fun init() {

    }
}