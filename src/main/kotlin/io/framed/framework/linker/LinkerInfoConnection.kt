package io.framed.framework.linker

import io.framed.framework.ConnectionManager
import io.framed.framework.model.ModelConnection
import io.framed.framework.pictogram.ElementInfo

/**
 * Interface for connection information.
 *
 * Typically the companion object of a [ConnectionLinker] will implement this interface to specify
 * helper methods and constraints.
 */
interface LinkerInfoConnection {

    /**
     * Static information for this connection type.
     */
    val info: ElementInfo

    /**
     * Check if this connection can be started at the given [source].
     */
    fun canStart(source: Linker<*, *>): Boolean

    /**
     * Check if this connection can be created at the given [source] and [target].
     */
    fun canCreate(source: Linker<*, *>, target: Linker<*, *>): Boolean

    /**
     * Check if a given model element belongs to this connection type.
     */
    fun isLinkerFor(element: ModelConnection): Boolean

    /**
     * Check if a given linker instance belongs to this connection type.
     */
    fun isLinkerFor(linker: Linker<*, *>): Boolean

    /**
     * Create a model instance of this connection type between [source] and [target].
     */
    fun createModel(source: Long, target: Long): ModelConnection

    /**
     * Create a linker instance of this connection type based on the [model].
     */
    fun createLinker(model: ModelConnection, connectionManager: ConnectionManager): ConnectionLinker<*>

    /**
     * Check if two linker are siblings.
     */
    infix fun Linker<*, *>.isSibling(other: Linker<*, *>) =
            this is ShapeLinker<*, *> && other is ShapeLinker<*, *> && parent == other.parent

    // FIXME: This function does not work with nested elements and RoleGroups.
    infix fun Linker<*, *>.isConnectable(other: Linker<*, *>) : Boolean {
        return this is ShapeLinker<*, *> &&
                other is ShapeLinker<*, *> &&
                (parent == other.parent ||
                        this.ancestors.contains(other.parent) ||
                        other.ancestors.contains(parent))
    }

    val Linker<*, *>.ancestors
        get() = getAncestors()

    infix fun Linker<*, *>.isParentAncestorOf(other: Linker<*, *>) =
            this is ShapeLinker<*, *> && other is ShapeLinker<*, *> && parent in other.ancestors

    val Linker<*, *>.parent: ShapeLinker<*, *>?
        get() = (this as? ShapeLinker<*, *>)?.parent

    infix fun Linker<*, *>.isAncestorOf(other: Linker<*, *>) = this in other.ancestors
}

private tailrec fun Linker<*, *>.getAncestors(accumulator: List<ShapeLinker<*, *>> = emptyList()): List<ShapeLinker<*, *>> {
    if (this !is ShapeLinker<*, *>) return emptyList()
    val parent = this.parent

    @Suppress("IfThenToElvis")
    return if (parent == null) {
        accumulator
    } else {
        parent.getAncestors(accumulator + parent)
    }
}
