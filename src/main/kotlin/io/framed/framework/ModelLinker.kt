package io.framed.framework

import io.framed.framework.pictogram.BoxShape
import io.framed.framework.pictogram.Pictogram
import io.framed.framework.pictogram.Shape
import io.framed.framework.util.EventHandler
import io.framed.framework.util.History
import io.framed.framework.util.Property

/**
 * @author lars
 */
interface ModelLinker<M : ModelElement, P : Shape, R : Shape> : PreviewLinker<M, P, R> {
    val nameProperty: Property<String>
    val name: String
    val content: List<PreviewLinker<*, *, *>>
    val connectable: List<Linker<*,*>>

    val container: BoxShape

    fun getShapeById(id: Long): Shape? =
            connectable.find { it.model.id == id }?.pictogram as? Shape

    fun getIdByShape(shape: Shape): Long? =
            connectable.find { it.pictogram == shape }?.model?.id

    fun createConnection(source: Shape, target: Shape)

    val connections: List<ConnectionLinker<*, *>>
    val onConnectionAdd: EventHandler<ConnectionLinker<*,*>>
    val onConnectionRemove: EventHandler<ConnectionLinker<*,*>>

    val setPosition: EventHandler<SetPosition>
}