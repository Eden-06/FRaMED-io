package io.framed.framework

import io.framed.framework.pictogram.ContextEvent
import io.framed.framework.pictogram.Pictogram
import io.framed.framework.pictogram.Shape
import io.framed.framework.pictogram.SidebarEvent
import io.framed.framework.view.ContextMenu
import io.framed.framework.view.Sidebar

/**
 * @author lars
 */
interface ShapeLinker<M : ModelElement<M>, P : Shape> : Linker<M, P> {
    val parent: ShapeLinker<*, *>?

    override fun delete(): Unit = parent?.remove(this) ?: Unit

    fun remove(linker: ShapeLinker<*, *>): Unit = throw UnsupportedOperationException()
    fun add(model: ModelElement<*>): Unit = throw  UnsupportedOperationException()
}
