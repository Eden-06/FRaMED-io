package io.framed.framework

import io.framed.framework.pictogram.BoxShape
import io.framed.framework.pictogram.ConnectionInfo
import io.framed.framework.pictogram.Shape
import io.framed.framework.util.EventHandler
import io.framed.framework.util.Property

/**
 * @author lars
 */
interface ModelLinker<M : ModelElement, P : Shape, R : Shape> : PreviewLinker<M, P, R> {
    val nameProperty: Property<String>
    val name: String
    val content: List<PreviewLinker<*, *, *>>
    val connectable: List<Linker<*, *>>

    val container: BoxShape

    fun getShapeById(id: Long): Shape? =
            connectable.find { it.id == id }?.pictogram as? Shape

    fun getIdByShape(shape: Shape): Long? =
            connectable.find { it.pictogram == shape }?.id


    val connections: List<ConnectionLinker<*>>
    val onConnectionAdd: EventHandler<ConnectionLinker<*>>
    val onConnectionRemove: EventHandler<ConnectionLinker<*>>

    val setPosition: EventHandler<SetPosition>

    fun canConnectionStart(source: Shape): List<ConnectionInfo> {
        val sourceLinker = connectable.find { it.pictogram == source } ?: return emptyList()

        return LinkerManager.linkerConnectionList.asSequence().filter { it.canStart(sourceLinker) }.map { it.info }.toList()
    }

    fun canConnectionCreate(source: Shape, target: Shape): List<ConnectionInfo> {
        val sourceLinker = connectable.find { it.pictogram == source } ?: return emptyList()
        val targetLinker = connectable.find { it.pictogram == target } ?: return emptyList()

        return LinkerManager.linkerConnectionList.asSequence().filter { it.canCreate(sourceLinker, targetLinker) }.map { it.info }.toList()
    }

    fun createConnection(source: Shape, target: Shape)
    fun createConnection(source: Shape, target: Shape, type: ConnectionInfo): ConnectionLinker<*>

    override fun findConnections(shape: Shape): List<ConnectionLinker<*>> = super.findConnections(shape) + connections.filter { shape in it }
}