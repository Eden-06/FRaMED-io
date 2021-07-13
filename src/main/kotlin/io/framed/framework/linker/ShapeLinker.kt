package io.framed.framework.linker

import io.framed.framework.model.ModelElement
import io.framed.framework.pictogram.BoxShape
import io.framed.framework.pictogram.Shape
import io.framed.framework.pictogram.SidebarEvent
import io.framed.framework.view.dialog
import io.framed.framework.view.textView

/**
 * Base interface for all linker that are represented by a graphical shape other than a connection.
 *
 * This interface adds a tree structure to the linker to allow parent children relationships.
 *
 * @author lars
 */
interface ShapeLinker<M : ModelElement, P : Shape> : Linker<M, P> {
    val parent: ShapeLinker<*, *>?

    override fun delete() {
        parent?.let { parent ->
            parent.remove(this)
            parent.sidebar.onOpen(SidebarEvent(parent.pictogram))
            parent.sidebar.open()
        } ?: dialog {
            title = "Delete error"
            contentView.apply {
                textView("Cannot delete root element.")
            }
            addButton("Close")
        }.open()
    }

    fun remove(linker: ShapeLinker<*, *>): Unit = throw UnsupportedOperationException("Remove operation for ${this::class} not implemented.")
    fun add(model: ModelElement): ShapeLinker<*, *> = throw  UnsupportedOperationException("Add operation for ${this::class} not implemented.")

    /**
     * Set of all known type names of this linker and all children.
     */
    val subTypes: Set<String>
        get() = emptySet()

    /**
     * Try to autocomplete the [partial] by known type names.
     */
    fun getTypeSubset(partial: String): List<String> {
        val parent = parent
        return parent?.getTypeSubset(partial)
                ?: subTypes.filter { it.lowercase().contains(partial.lowercase()) && it.isNotEmpty() }.take(8)
    }

    fun updateSize(allowDownsize: Boolean = false) {
        if (allowDownsize) {
            pictogram.autosize = true
        }
        (pictogram as? BoxShape)?.autoSize(allowDownsize || pictogram.autosize)
    }
}
