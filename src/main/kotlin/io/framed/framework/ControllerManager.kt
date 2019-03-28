package io.framed.framework

import io.framed.File
import io.framed.framework.pictogram.Layer
import io.framed.framework.view.Application
import io.framed.linker.ConnectionManagerLinker
import io.framed.linker.PackageLinker

object ControllerManager {

    private lateinit var rootLinker: PackageLinker

    var file: File
        get() = File(
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
