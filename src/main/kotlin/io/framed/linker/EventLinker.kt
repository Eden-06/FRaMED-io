package io.framed.linker

import io.framed.model.Event
import io.framed.model.EventType
import io.framed.picto.*
import io.framed.util.property
import io.framed.view.ContextMenu
import io.framed.view.MaterialIcon
import io.framed.view.Sidebar
import io.framed.view.contextMenu

class EventLinker(
        val event: Event,
        override val parent: ContainerLinker
) : Linker<IconShape>(event, parent) {

    private val typeProperty = property(event::type)
    val symbolProperty = property(typeProperty,
            getter = {
                typeProperty.get().symbol
            }
    )

    override val picto: IconShape = iconShape(symbolProperty) {
        style {
            background = color(255, 255, 255)
            border {
                style = Border.BorderStyle.SOLID
                width = 1.0
                color = color(0, 0, 0, 0.3)
                radius = 20.0
            }
            padding(10.0)
        }

        hasContext = true
        hasSidebar = true
    }.also(this::initPicto)


    override fun createSidebar(sidebar: Sidebar) = sidebar.setup {
        title("Event")

        group("General") {
            select("Type", EventType.values().toList(), typeProperty)
        }
    }

    override fun createContextMenu(event: ContextEvent): ContextMenu? = contextMenu {
        title = "Event"
        addItem(MaterialIcon.DELETE, "Delete") {
            parent.removeEvent(this@EventLinker.event)
        }
    }
}