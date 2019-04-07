package io.framed.framework

import io.framed.framework.pictogram.ConnectionInfo

interface LinkerInfoConnection {
    val info: ConnectionInfo

    fun canStart(source: Linker<*, *>): Boolean
    fun canCreate(source: Linker<*, *>, target: Linker<*, *>): Boolean

    fun isLinkerFor(element: ModelConnection<*>): Boolean
    fun isLinkerFor(linker: Linker<*, *>): Boolean

    fun createModel(source: Long, target: Long): ModelConnection<*>
    fun createLinker(model: ModelConnection<*>, connectionManager: ConnectionManager): ConnectionLinker<*>

    infix fun Linker<*, *>.isSibling(other: Linker<*, *>) =
            this is ShapeLinker<*, *> && other is ShapeLinker<*, *> && parent == other.parent

    private tailrec fun Linker<*, *>.getAncestors(accumulator: List<ShapeLinker<*, *>> = emptyList()): List<ShapeLinker<*, *>> {
        if (this !is ShapeLinker<*, *>) return emptyList()
        val parent = parent
        @Suppress("IfThenToElvis")
        return if (parent == null) {
            listOf(this)
        } else {
            parent.getAncestors(accumulator + this)
        }
    }

    val Linker<*, *>.ancestors
        get() = getAncestors()

    infix fun Linker<*, *>.isParentAncestorOf(other: Linker<*, *>) =
            this is ShapeLinker<*, *> && other is ShapeLinker<*, *> && parent in other.ancestors

    val Linker<*, *>.parent: ShapeLinker<*, *>?
        get() = (this as? ShapeLinker<*, *>)?.parent

    infix fun Linker<*, *>.isAncestorOf(other: Linker<*, *>) = this in other.ancestors
}