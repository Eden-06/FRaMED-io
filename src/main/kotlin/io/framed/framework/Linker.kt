package io.framed.framework

import io.framed.framework.pictogram.ContextEvent
import io.framed.framework.pictogram.Pictogram
import io.framed.framework.pictogram.SidebarEvent
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

    val sidebar: Sidebar
    val contextMenu: ContextMenu

    fun focus() {
        sidebar.open()
    }

    fun Sidebar.onOpen(event: SidebarEvent) {}
    fun ContextMenu.onOpen(event: ContextEvent) {}
}
