package io.framed.framework.view

import de.westermann.kobserve.basic.FunctionAccessor
import de.westermann.kobserve.basic.mapBinding
import de.westermann.kobserve.basic.property
import de.westermann.kobserve.not
import io.framed.File
import io.framed.framework.Controller
import io.framed.framework.ControllerManager
import io.framed.framework.render.html.HtmlRenderer
import io.framed.framework.util.*
import io.framed.linker.ConnectionManagerLinker
import io.framed.linker.ContainerLinker
import org.w3c.dom.HTMLDivElement
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
    val renderer = HtmlRenderer(workspace)

    private var zoomBackingField: Double = 1.0
    val zoomProperty = property(this::zoomBackingField)
    var zoom by zoomProperty

    val zoomStringProperty = property(object : FunctionAccessor<String> {
        override fun set(value: String): Boolean {
            value.replace("%", "").trim().toIntOrNull()?.let {
                zoom = it / 100.0
            }
            return true
        }

        override fun get(): String = "${(zoom * 100).roundToInt()}%"

    }, zoomProperty)

    private val toolBar = toolBar {

        custom(ToolBar.Side.LEFT) {
            inputView(zoomStringProperty) {
                autocomplete = NavigationView.zoomSteps.map {
                    "${(it * 100).roundToInt()}%"
                }
                autocompleteMatch = false
                size = 4
                onFocusLeave {
                    value = zoomStringProperty.value
                }
                input.html.style.textAlign = "center"
            }
        }

        //Left block
        action(ToolBar.Side.LEFT, MaterialIcon.ZOOM_IN, "Zoom in", Shortcut("+", Shortcut.Modifier.CTRL)) { _ ->
            val nextStep = NavigationView.zoomSteps.asSequence().filter { it > zoom }.min()
                    ?: NavigationView.zoomSteps.max()
            if (nextStep != null) {
                renderer.zoom = nextStep
            }
        }
        action(ToolBar.Side.LEFT, MaterialIcon.ZOOM_OUT, "Zoom out", Shortcut("-", Shortcut.Modifier.CTRL)) { _ ->
            val nextStep = NavigationView.zoomSteps.asSequence().filter { it < zoom }.max()
                    ?: NavigationView.zoomSteps.min()
            if (nextStep != null) {
                renderer.zoom = nextStep
            }
        }

        Root.shortcut(Shortcut("0", Shortcut.Modifier.CTRL)) {
            renderer.zoom = 1.0
        }

        separator(ToolBar.Side.LEFT)
        action(ToolBar.Side.LEFT, MaterialIcon.UNDO, "Undo", Shortcut("Z", Shortcut.Modifier.CTRL)) { _ ->
            if (History.canUndo) {
                History.undo()
            }
        }.inactiveProperty.bind(!History.canUndoProperty)
        action(ToolBar.Side.LEFT, MaterialIcon.REDO, "Redo", Shortcut("Z", Shortcut.Modifier.CTRL, Shortcut.Modifier.SHIFT)) { _ ->
            if (History.canRedo) {
                History.redo()
            }
        }.inactiveProperty.bind(!History.canRedoProperty)

        // Right block
        /*
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
        */
        action(ToolBar.Side.RIGHT, MaterialIcon.GRID_ON, "Toggle grid") {
            renderer.navigationView.renderGrid = !renderer.navigationView.renderGrid
        }.inactiveProperty.bind(!renderer.navigationView.renderGridProperty)

        action(ToolBar.Side.RIGHT, MaterialIcon.BORDER_INNER, "Toggle snap to grid") {
            renderer.snapToGrid = !renderer.snapToGrid
        }.inactiveProperty.bind(!renderer.snapToGridProperty)

        action(ToolBar.Side.RIGHT, MaterialIcon.FILTER_CENTER_FOCUS, "Toggle snap to view") {
            renderer.snapToView = !renderer.snapToView
        }.inactiveProperty.bind(!renderer.snapToViewProperty)
    }

    private val menuBar = menuBar {
        menu("File") {
            item(null, "Open", Shortcut("O", Shortcut.Modifier.CTRL)) {
                loadLocalFile { content ->
                    File.fromJSON(content)
                }
            }
            item(null, "Save", Shortcut("S", Shortcut.Modifier.CTRL)) {
                ControllerManager.root?.let { root ->
                    val linker = (root.linker as ContainerLinker)
                    val connections = linker.connectionManager as ConnectionManagerLinker

                    val file = File(linker.model, connections.modelConnections, ControllerManager.layers)

                    triggerDownload("${file.name}.json", file.toJSON())
                }
            }
            item(null, "Reset") {

            }
        }
        menu("Edit") {
            item(MaterialIcon.UNDO, "Undo", Shortcut("Z", Shortcut.Modifier.CTRL)) {
                if (History.canUndo) {
                    History.undo()
                }
            }.bindCssClass("inactive", !History.canUndoProperty)
            item(MaterialIcon.REDO, "Redo", Shortcut("Z", Shortcut.Modifier.CTRL, Shortcut.Modifier.SHIFT)) {
                if (History.canRedo) {
                    History.redo()
                }
            }.bindCssClass("inactive", !History.canRedoProperty)

            separator()

            item(MaterialIcon.CONTENT_CUT, "Cut", Shortcut("X", Shortcut.Modifier.CTRL)) {
                //TODO
            }.bindCssClass("inactive", renderer.selectedViewSizeProperty.mapBinding { it == 0 })
            item(MaterialIcon.CONTENT_COPY, "Copy", Shortcut("C", Shortcut.Modifier.CTRL)) {
                //TODO
            }.bindCssClass("inactive", renderer.selectedViewSizeProperty.mapBinding { it == 0 })
            item(MaterialIcon.CONTENT_PASTE, "Paste", Shortcut("V", Shortcut.Modifier.CTRL)) {
                //TODO
            }.bindCssClass("inactive", property(true))
            item(MaterialIcon.DELETE, "Delete", Shortcut("Delete")) {
                renderer.deleteSelected()
            }.bindCssClass("inactive", renderer.selectedViewSizeProperty.mapBinding { it == 0 })

            separator()
            item(null, "Select all", Shortcut("A", Shortcut.Modifier.CTRL)) {
                renderer.selectAll()
            }
        }
        menu("Foo") {
            item(null, "Bar 1") {}
            menu("Bar 2") {
                item(null, "Lorem") {}
                item(null, "Ipson") {}
            }
            item(null, "Bar 3") {}
        }
    }

    val propertyBar = propertyBar {
        onResize {
            workspaceContainer.right = clientWidth - it
            async {
                renderer.navigationView.resize()
            }
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