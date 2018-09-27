package io.framed.controller

import io.framed.model.Model
import io.framed.picto.ContextEvent
import io.framed.picto.Picto
import io.framed.picto.SidebarEvent
import io.framed.util.property
import io.framed.view.ContextMenu
import io.framed.view.Sidebar

/**
 * @author lars
 */
abstract class Controller<T : Picto>(
        model: Model,
        open val parent: Controller<*>?
) {

    private val creationProperty = property(model.metadata::creationDate)
    private val creationStringProperty = property(creationProperty,
            getter = {
                creationProperty.get().toUTCString()
            }
    )

    private val modifiedProperty = property(model.metadata::modifiedDate)
    private val modifiedStringProperty = property(modifiedProperty,
            getter = {
                modifiedProperty.get().toUTCString()
            }
    )

    private val authorProperty = property(model.metadata::author)

    abstract val picto: T
    private val sidebar: Sidebar by lazy {
        internalCreateSidebar().apply {
            createSidebar(this)

            group("Metadata") {
                input("Creation date", creationStringProperty)
                input("Modified date", modifiedStringProperty)
                input("Author", authorProperty)

                collapse()
            }
        }
    }

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