package io.framed.framework

import io.framed.Project
import io.framed.framework.linker.ModelLinker
import io.framed.framework.pictogram.Layer
import io.framed.framework.view.Application
import io.framed.linker.ConnectionManagerLinker
import io.framed.linker.PackageLinker

/**
 * Manage all opened controller.
 */
object ControllerManager {

    private lateinit var rootLinker: PackageLinker

    /**
     * Get or set the current project instance.
     */
    var project: Project
        get() = Project(
                rootLinker.model,
                (rootLinker.connectionManager as ConnectionManagerLinker).modelConnections,
                layers
        )
        set(value) {
            rootLinker = PackageLinker(value.root, ConnectionManagerLinker(value.connections))

            layers.clear()
            controllers.clear()
            layers.putAll(value.layer)

            display(rootLinker, true)
        }

    private val controllers: MutableMap<Long, Controller> = mutableMapOf()
    private val layers: MutableMap<Long, Layer> = mutableMapOf()

    /**
     * Display a model linker.
     * This will create (or reuse if already done) a new controller and display it in the application.
     *
     * @param clear Close all opened tabs and clear history.
     */
    fun display(linker: ModelLinker<*, *, *>, clear: Boolean = false) {
        val controller = controllers.getOrPut(linker.id) {
            val layer = layers.getOrPut(linker.id) {
                Layer()
            }
            linker.container.layer = layer
            Controller(linker, layer)
        }
        Application.loadController(controller, clear)
    }
}
