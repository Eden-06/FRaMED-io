package io.framed.controller

import io.framed.picto.ContextEvent
import io.framed.picto.Picto
import io.framed.picto.SidebarEvent
import io.framed.view.ContextMenu
import io.framed.view.Sidebar

/**
 * @author lars
 */
abstract class Controller<T : Picto>(
        open val parent: Controller<*>?
) {
    abstract val picto: T
    private val sidebar: Sidebar by lazy { internalCreateSidebar().also { createSidebar(it) } }

    protected open fun internalCreateSidebar(): Sidebar = parent?.internalCreateSidebar() ?: throw NotImplementedError()

    protected open fun createSidebar(sidebar: Sidebar) {}

    protected open fun createContextMenu(event: ContextEvent): ContextMenu? = null

    protected open fun prepareSidebar(sidebar: Sidebar, event: SidebarEvent) {}

    fun showSidebar(event: SidebarEvent = SidebarEvent.NONE) {
        prepareSidebar(sidebar, event)
        sidebar.display()
    }

    fun initPicto(picto: Picto) {
        picto.onSidebar {
            showSidebar(it)
        }
        picto.onContext {
            createContextMenu(it)?.open(it.position)
        }
    }
}