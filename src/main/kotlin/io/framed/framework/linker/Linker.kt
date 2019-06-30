package io.framed.framework.linker

import de.westermann.kobserve.ReadOnlyProperty
import de.westermann.kobserve.property.mapBinding
import io.framed.framework.model.ModelElement
import io.framed.framework.pictogram.ContextEvent
import io.framed.framework.pictogram.Pictogram
import io.framed.framework.pictogram.SidebarEvent
import io.framed.framework.view.ContextMenu
import io.framed.framework.view.MaterialIcon
import io.framed.framework.view.Sidebar
import io.framed.framework.view.contextMenu

/**
 * Base interface for all linker. All linker have to implement this interface.
 *
 * The linker is the central element of this application. The data-model (represented by [ModelElement]) creates
 * a link-tree structure. The view-model (represented by [Pictogram]) creates a tree structure. The linker-model
 * (represented by this interface) is the mediator between this two trees. It consumes the data-model and creates
 * the corresponding view-model. Therefore it also create a tree structure.
 *
 * @author lars
 */
interface Linker<M : ModelElement<M>, P : Pictogram> {

    /**
     * Reference to the view-model
     */
    val pictogram: P

    /**
     * Reference to the data-model
     */
    val model: M

    val id: Long
        get() = model.id

    val nameProperty: ReadOnlyProperty<String>
    val name: String

    val sidebar: Sidebar
    val contextMenu: ContextMenu

    /**
     * Extract the linker name by reflection.
     */
    val typeName: String
        get() = this::class.simpleName?.replace("Linker", "") ?: "Unknown"

    /**
     * Open the sidebar.
     */
    fun focus(pictogram: Pictogram = this.pictogram) {
        sidebar.onOpen(SidebarEvent(pictogram))
        sidebar.open()
    }

    fun delete()

    /**
     * Execute some code before the sidebar gets opened.
     */
    fun Sidebar.onOpen(event: SidebarEvent) {}

    /**
     * Execute some code before the context menu gets opened.
     */
    fun ContextMenu.onOpen(event: ContextEvent) {}

    /**
     * Setup standard entries for context menu to improve consistency.
     * The following entries will be added:
     * - linker name
     * - entries of [block]
     * - all possible add entries
     * - delete
     */
    fun defaultContextMenu(block: ContextMenu.() -> Unit = {}) = contextMenu {
        titleProperty.bind(nameProperty.mapBinding { "$typeName: $it" })

        block()

        if (this@Linker is ShapeLinker<*, *>) {
            LinkerManager.contextMenu(this@Linker, this@contextMenu)
        }

        addItem(MaterialIcon.DELETE, "Delete") {
            delete()
        }
    }

    fun updateLabelBindings() {
        pictogram.labelsProperty.onChange.emit(Unit)
    }
}
