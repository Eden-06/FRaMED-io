package io.framed.framework

import de.westermann.kobserve.EventHandler
import io.framed.framework.pictogram.ConnectionInfo

interface ConnectionManager {

    val modelLinkers: Set<ModelLinker<*, *, *>>
    val connections: Set<ConnectionLinker<*>>

    val shapes: Set<ShapeLinker<*, *>>
        get() = modelLinkers.flatMap { it.shapeLinkers }.toSet()

    fun add(model: ModelConnection<*>)
    fun remove(linker: ConnectionLinker<*>)

    val onConnectionAdd: EventHandler<ConnectionLinker<*>>
    val onConnectionRemove: EventHandler<ConnectionLinker<*>>

    /*
    fun getShapeById(id: Long): Shape? {
        return shapes.find { it.id == id }?.pictogram
    }
    */
    fun getLinkerById(id: Long): ShapeLinker<*, *>? = shapes.find { it.id == id }

    fun createConnection(source: Long, target: Long)
    fun createConnection(source: Long, target: Long, type: ConnectionInfo): ConnectionLinker<*>

    fun addModel(modelLinker: ModelLinker<*, *, *>)

    fun listConnections(id: Long): List<ConnectionLinker<*>> =
            connections.filter { it.sourceIdProperty.get() == id || it.targetIdProperty.get() == id }

    fun canConnectionStart(source: Long): List<ConnectionInfo> {
        val sourceLinker = getLinkerById(source) ?: return emptyList()

        return LinkerManager.linkerConnectionList.asSequence().filter { it.canStart(sourceLinker) }.map { it.info }.toList()
    }

    fun canConnectionCreate(source: Long, target: Long): List<ConnectionInfo> {
        val sourceLinker = getLinkerById(source) ?: return emptyList()
        val targetLinker = getLinkerById(target) ?: return emptyList()

        return LinkerManager.linkerConnectionList.asSequence().filter { it.canCreate(sourceLinker, targetLinker) }.map { it.info }.toList()
    }

    fun init()
}