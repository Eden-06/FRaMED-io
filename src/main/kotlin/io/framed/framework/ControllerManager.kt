package io.framed.framework

import io.framed.framework.pictogram.Layer
import io.framed.framework.view.Application

object ControllerManager {

    var controllers: List<Controller> = emptyList()
    var layers: Map<Long, Layer> = emptyMap()

    val root: Controller?
    get() = controllers.find { it.linker.parent == null }

    fun register(linker: ModelLinker<*, *, *>): Controller {
        val layer = layers[linker.id] ?: run {
            val layer = Layer()
            layers += linker.id to layer
            layer
        }
        val controller = Controller(linker, layer)
        controllers += controller

        return controller
    }

    operator fun get(linker: ModelLinker<*, *, *>) = controllers.find { it.linker == linker }

    fun display(linker: ModelLinker<*,*,*>) {
        Application.loadController(get(linker) ?: register(linker))
    }
}