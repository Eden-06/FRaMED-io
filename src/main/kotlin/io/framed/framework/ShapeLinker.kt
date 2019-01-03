package io.framed.framework

import io.framed.framework.pictogram.Shape
import io.framed.framework.pictogram.SidebarEvent

/**
 * @author lars
 */
interface ShapeLinker<M : ModelElement<M>, P : Shape> : Linker<M, P> {
    val parent: ShapeLinker<*, *>?

    override fun delete() {
        parent?.let { parent ->
            parent.remove(this)
            parent.sidebar.onOpen(SidebarEvent(parent.pictogram))
            parent.sidebar.open()
        }
    }

    fun remove(linker: ShapeLinker<*, *>): Unit = throw UnsupportedOperationException()
    fun add(model: ModelElement<*>): Unit = throw  UnsupportedOperationException()
}
