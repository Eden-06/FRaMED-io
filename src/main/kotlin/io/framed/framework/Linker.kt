package io.framed.framework

import io.framed.framework.pictogram.ContextEvent
import io.framed.framework.pictogram.Pictogram
import io.framed.framework.pictogram.SidebarEvent
import io.framed.framework.util.History
import io.framed.framework.view.ContextMenu
import io.framed.framework.view.Sidebar

/**
 * @author lars
 */
interface Linker<M : ModelElement, P : Pictogram> {
    val pictogram: P

    val model: M
    val parent: Linker<*, *>?

    val id: Long
        get() = model.id

    val history: History
        get() = parent?.history ?: TODO()

    val sidebar: Sidebar
    val contextMenu: ContextMenu

    fun focus() {
        sidebar.open()
    }

    fun Sidebar.onOpen(event: SidebarEvent) {}
    fun ContextMenu.onOpen(event: ContextEvent) {}
}

/*
protected open fun prepareSidebar(sidebar: Sidebar, event: SidebarEvent) {}


fun showSidebar(event: SidebarEvent = SidebarEvent.NONE) {
    prepareSidebar(sidebar, event)
    sidebar.display()
}

fun initPictogram(pictogram: Pictogram) {
    pictogram.onSidebar {
        showSidebar(it)
    }
    pictogram.onContext {
        createContextMenu(it)?.open(it.position)
    }
}

(model as? ModelElementMetadata)?.metadata?.let { metadata ->
    val creationProperty = property(metadata::creationDate)
    val creationStringProperty = property(creationProperty,
            getter = {
                creationProperty.get().toUTCString()
            }
    )
    val modifiedProperty = property(metadata::modifiedDate)
    val modifiedStringProperty = property(modifiedProperty,
            getter = {
                modifiedProperty.get().toUTCString()
            }
    )
    val authorProperty = property(metadata::author)

    group("Metadata") {
        input("Creation date", creationStringProperty)
        input("Modified date", modifiedStringProperty)
        input("Author", authorProperty)

        collapse()
    }
}
*/