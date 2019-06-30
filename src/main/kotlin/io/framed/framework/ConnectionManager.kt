package io.framed.framework

import de.westermann.kobserve.event.EventHandler
import io.framed.framework.linker.ConnectionLinker
import io.framed.framework.linker.LinkerManager
import io.framed.framework.linker.ModelLinker
import io.framed.framework.linker.ShapeLinker
import io.framed.framework.model.ModelConnection
import io.framed.framework.pictogram.ElementInfo

/**
 * Base interface for a connection manager.
 * A connection manager is responsible for creation, storing, deletion and checking of all connections.
 */
interface ConnectionManager {

    val modelLinkers: Set<ModelLinker<*, *, *>>
    val connections: Set<ConnectionLinker<*>>

    val shapes: Set<ShapeLinker<*, *>>
        get() = modelLinkers.flatMap { it.shapeLinkers }.toSet()

    fun add(model: ModelConnection<*>)
    fun remove(linker: ConnectionLinker<*>)

    val onConnectionAdd: EventHandler<ConnectionLinker<*>>
    val onConnectionRemove: EventHandler<ConnectionLinker<*>>

    fun getLinkerById(id: Long): ShapeLinker<*, *>? = shapes.find { it.id == id }

    fun createConnection(source: Long, target: Long)
    fun createConnection(source: Long, target: Long, type: ElementInfo): ConnectionLinker<*>

    fun addModel(modelLinker: ModelLinker<*, *, *>)

    fun listConnections(id: Long): List<ConnectionLinker<*>> =
            connections.filter { it.sourceIdProperty.get() == id || it.targetIdProperty.get() == id }

    fun canConnectionStart(source: Long): List<ElementInfo> {
        val sourceLinker: ShapeLinker<*, *> = getLinkerById(source) ?: return emptyList()

        return LinkerManager.linkerConnectionList.asSequence().filter { it.canStart(sourceLinker) }.map { it.info }.toList()
    }

    fun canConnectionCreate(source: Long, target: Long): List<ElementInfo> {
        val sourceLinker: ShapeLinker<*, *> = getLinkerById(source) ?: return emptyList()
        val targetLinker: ShapeLinker<*, *> = getLinkerById(target) ?: return emptyList()

        return LinkerManager.linkerConnectionList.asSequence()
                .filter { it.canCreate(sourceLinker, targetLinker) }.map { it.info }.toList()
    }

    fun isConnectable(shape: Long): Boolean {
        return LinkerManager.itemLinkerFor(getLinkerById(shape) ?: return false).isConnectable
    }

    fun init()

    fun delete(idList: List<Long>) {
        for (connection in connections) {
            if (connection.id in idList) {
                connection.delete()
            }
        }
    }
}