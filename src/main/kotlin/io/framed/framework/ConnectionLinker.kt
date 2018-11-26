package io.framed.framework

import de.westermann.kobserve.Property
import io.framed.framework.pictogram.Connection
import io.framed.framework.pictogram.ConnectionInfo
import io.framed.framework.pictogram.Shape
import io.framed.framework.util.History

/**
 * @author lars
 */
interface ConnectionLinker<M : ModelConnection<M>>: Linker<M, Connection> {
    val manager: ConnectionManager

    val sourceIdProperty: Property<Long>

    val targetIdProperty: Property<Long>

    fun canSwap(info: ConnectionInfo): Boolean =
            manager.canConnectionCreate(targetIdProperty.get(), sourceIdProperty.get()).contains(info)

    fun swap() {
        History.group("Swap connection") {
            val h = sourceIdProperty.get()
            sourceIdProperty.set(targetIdProperty.get())
            targetIdProperty.set(h)
        }
    }

    override fun delete() = manager.remove(this)

    fun canConvert() = manager.canConnectionCreate(sourceIdProperty.get(), targetIdProperty.get())
    operator fun contains(shape: Shape): Boolean = sourceIdProperty.get() == shape.id || targetIdProperty.get() == shape.id
    operator fun contains(id: Long): Boolean = sourceIdProperty.get() == id || targetIdProperty.get() == id
}
