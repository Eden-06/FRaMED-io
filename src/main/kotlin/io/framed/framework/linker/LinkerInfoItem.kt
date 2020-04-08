package io.framed.framework.linker

import io.framed.framework.ConnectionManager
import io.framed.framework.model.ModelElement
import io.framed.framework.pictogram.ElementInfo

/**
 * Interface for element information.
 *
 * Typically the companion object of a [ShapeLinker] will implement this interface to specify
 * helper methods and constraints.
 */
interface LinkerInfoItem {

    /**
     * Static information for this model type.
     */
    val info: ElementInfo

    /**
     * Check if this model type can be placed inside of a [container].
     */
    fun canCreateIn(container: ModelElement): Boolean

    /**
     * Check if a given model element belongs to this model type.
     */
    fun isLinkerFor(element: ModelElement): Boolean

    /**
     * Check if a given linker instance belongs to this model type.
     */
    fun isLinkerFor(linker: Linker<*, *>): Boolean

    /**
     * Create a model instance of this model type.
     */
    fun createModel(): ModelElement

    /**
     * Create a linker instance of this model type based on the [model].
     */
    fun createLinker(model: ModelElement, parent: Linker<*, *>, connectionManager: ConnectionManager?): Linker<*, *>

    /**
     * Specifies if this model type can be source or target of a connection.
     */
    val isConnectable: Boolean
        get() = true
}
