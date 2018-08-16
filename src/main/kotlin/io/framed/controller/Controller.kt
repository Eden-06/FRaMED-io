package io.framed.controller

import io.framed.picto.Picto
import io.framed.picto.Shape
import io.framed.util.Point
import io.framed.view.ContextMenu
import io.framed.view.Sidebar

/**
 * @author lars
 */
abstract class Controller<T:Picto>(
        open val parent: Controller<*>?
) {
    abstract val picto: T
    private val sidebar: Sidebar by lazy { internalCreateSidebar().also { createSidebar(it) } }

    protected open fun internalCreateSidebar(): Sidebar = parent?.internalCreateSidebar() ?: throw NotImplementedError()

    protected open fun createSidebar(sidebar: Sidebar) {}

    protected open fun createContextMenu(position: Point): ContextMenu? = null

    fun showSidebar() {
        sidebar.display()
    }

    fun initPicto(picto: Picto) {
        picto.onSidebar {
            showSidebar()
        }
        picto.onContext {
            createContextMenu(it)?.open(it)
        }
    }
}