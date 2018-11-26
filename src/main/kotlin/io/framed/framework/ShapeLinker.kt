package io.framed.framework

import io.framed.framework.pictogram.Shape

/**
 * @author lars
 */
interface ShapeLinker<M : ModelElement<M>, P : Shape> : Linker<M, P> {
    val parent: ShapeLinker<*, *>?

    override fun delete(): Unit = parent?.remove(this) ?: Unit

    fun remove(linker: ShapeLinker<*, *>): Unit = throw UnsupportedOperationException()
    fun add(model: ModelElement<*>): Unit = throw  UnsupportedOperationException()
}
