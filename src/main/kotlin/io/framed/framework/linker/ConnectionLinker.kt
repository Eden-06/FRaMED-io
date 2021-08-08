package io.framed.framework.linker

import de.westermann.kobserve.Property
import io.framed.framework.ConnectionManager
import io.framed.framework.model.ModelConnection
import io.framed.framework.pictogram.Connection
import io.framed.framework.pictogram.ElementInfo
import io.framed.framework.pictogram.Shape
import io.framed.framework.util.History

/**
 * Base interface for all connection linker. All connection linker have to implement this interface.
 * @author lars
 */
interface ConnectionLinker<M : ModelConnection> : Linker<M, Connection> {

    /**
     * Reference to the parent connection manager
     */
    val manager: ConnectionManager

    val sourceIdProperty: Property<Long>

    val targetIdProperty: Property<Long>

    /**
     * Check if this connection can be swapped. The connection info object must match this connection linker.
     * Otherwise the behavior is undefined.
     */
    fun canSwap(info: ElementInfo): Boolean =
            manager.canConnectionCreate(targetIdProperty.get(), sourceIdProperty.get()).contains(info)

    /**
     * Swap the current connection. This function does not perform sanity checks.
     * Only use it in conjunction with [canSwap].
     */
    fun swap() {
        History.group("Swap connection") {
            val h = sourceIdProperty.get()
            sourceIdProperty.set(targetIdProperty.get())
            targetIdProperty.set(h)
        }
    }

    /**
     * Delete this connection instance.
     */
    override fun delete() = manager.remove(this)

    /**
     * List all possible [ElementInfo] objects that are allowed between source and target.
     */
    fun canConvert() = manager.canConnectionCreate(sourceIdProperty.get(), targetIdProperty.get())

    /**
     * Check if the given [shape] is the source or target.
     */
    operator fun contains(shape: Shape): Boolean = sourceIdProperty.get() == shape.id || targetIdProperty.get() == shape.id

    /**
     * Check if the given [id] is the source or target.
     */
    operator fun contains(id: Long): Boolean = sourceIdProperty.get() == id || targetIdProperty.get() == id
    fun enablePortConnection(forSource: Boolean, forTarget: Boolean) {
        throw RuntimeException("Not implemented for this connection!")
    }
}
