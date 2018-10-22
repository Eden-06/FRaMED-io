package io.framed.framework

import io.framed.framework.pictogram.ConnectionInfo
import io.framed.framework.pictogram.Shape
import io.framed.framework.util.EventHandler

interface ConnectionManager {

    val modelLinkers: Set<ModelLinker<*, *, *>>
    val connections: Set<ConnectionLinker<*>>

    val shapes: Set<ShapeLinker<*, *>>
        get() = modelLinkers.flatMap { it.shapeLinkers }.toSet()

    fun add(model: ModelConnection<*>)
    fun remove(linker: ConnectionLinker<*>)

    val onConnectionAdd: EventHandler<ConnectionLinker<*>>
    val onConnectionRemove: EventHandler<ConnectionLinker<*>>

    fun getShapeById(id: Long): Shape? {
        return shapes.find { it.id == id }?.pictogram
    }
    fun getLinkerByShape(shape: Shape): ShapeLinker<*, *>? = shapes.find { it.pictogram == shape }

    fun createConnection(source: Shape, target: Shape)
    fun createConnection(source: Shape, target: Shape, type: ConnectionInfo): ConnectionLinker<*>

    fun addModel(modelLinker: ModelLinker<*, *, *>)

    fun listConnections(id: Long): List<ConnectionLinker<*>> =
            connections.filter { it.sourceIdProperty.get() == id || it.targetIdProperty.get() == id }

    fun canConnectionStart(source: Shape): List<ConnectionInfo> {
        val sourceLinker = getLinkerByShape(source) ?: return emptyList()

        return LinkerManager.linkerConnectionList.asSequence().filter { it.canStart(sourceLinker) }.map { it.info }.toList()
    }

    fun canConnectionCreate(source: Shape, target: Shape): List<ConnectionInfo> {
        val sourceLinker = getLinkerByShape(source) ?: return emptyList()
        val targetLinker = getLinkerByShape(target) ?: return emptyList()

        return LinkerManager.linkerConnectionList.asSequence().filter { it.canCreate(sourceLinker, targetLinker) }.map { it.info }.toList()
    }

    fun init()
}