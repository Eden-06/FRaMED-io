package io.framed.linker

import de.westermann.kobserve.event.EventHandler
import io.framed.framework.*
import io.framed.framework.linker.ConnectionLinker
import io.framed.framework.linker.LinkerManager
import io.framed.framework.linker.ModelLinker
import io.framed.framework.model.ModelConnection
import io.framed.framework.pictogram.ElementInfo
import io.framed.framework.util.LinkerConnectionBox
import io.framed.framework.view.*
import io.framed.model.Connections

class ConnectionManagerLinker(val modelConnections: Connections) : ConnectionManager {

    override var modelLinkers: Set<ModelLinker<*, *, *>> = emptySet()

    override val connections: Set<ConnectionLinker<*>>
        get() = connectionBox.linkers

    private val connectionBox = LinkerConnectionBox<ModelConnection, ConnectionLinker<out ModelConnection>>(modelConnections::connections, this)

    override fun addModel(modelLinker: ModelLinker<*, *, *>) {
        modelLinkers += modelLinker
    }

    override fun add(model: ModelConnection) {
        connectionBox += LinkerManager.createLinker(model, this)
    }

    override fun remove(linker: ConnectionLinker<*>) {
        if (linker in connectionBox) {
            connectionBox -= linker
        }
    }

    override val onConnectionAdd = EventHandler<ConnectionLinker<*>>()
    override val onConnectionRemove = EventHandler<ConnectionLinker<*>>()

    override fun createConnection(source: Long, target: Long) {
        val types = canConnectionCreate(source, target)
        if (types.isEmpty()) return

        if (types.size == 1) {
            createConnection(source, target, types.first())
        } else {
            CyclicChooser(types) { type ->
                tooltipIconView(type.icon, type.name)
                onClick {
                    createConnection(source, target, type)
                }
            }
        }
    }

    override fun createConnection(source: Long, target: Long, type: ElementInfo): ConnectionLinker<*> {
        val model = LinkerManager.createConnectionModelByType(type, source, target)
        val linker = LinkerManager.createLinker<ConnectionLinker<*>>(model, this)
        connectionBox += linker
        return linker
    }

    private var isInit = false
    override fun init() {
        if (!isInit) {
            modelConnections.connections.forEach(this::add)
            isInit = true
        }
    }
}
