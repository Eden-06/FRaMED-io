package io.framed.framework

import de.westermann.kobserve.ReadOnlyProperty
import de.westermann.kobserve.property.mapBinding
import io.framed.framework.pictogram.ContextEvent
import io.framed.framework.pictogram.Pictogram
import io.framed.framework.pictogram.SidebarEvent
import io.framed.framework.view.ContextMenu
import io.framed.framework.view.MaterialIcon
import io.framed.framework.view.Sidebar
import io.framed.framework.view.contextMenu

/**
 * @author lars
 */
interface Linker<M : ModelElement<M>, P : Pictogram> {
    val pictogram: P

    val model: M

    val id: Long
        get() = model.id

    val nameProperty: ReadOnlyProperty<String>
    val name: String

    val sidebar: Sidebar
    val contextMenu: ContextMenu

    val typeName: String
        get() = this::class.simpleName?.replace("Linker", "") ?: "Unknown"

    fun focus(pictogram: Pictogram = this.pictogram) {
        sidebar.onOpen(SidebarEvent(pictogram))
        sidebar.open()
    }

    fun delete()

    fun Sidebar.onOpen(event: SidebarEvent) {}
    fun ContextMenu.onOpen(event: ContextEvent) {}

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
