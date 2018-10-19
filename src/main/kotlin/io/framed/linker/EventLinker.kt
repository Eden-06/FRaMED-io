package io.framed.linker

import io.framed.framework.Linker
import io.framed.framework.LinkerInfoItem
import io.framed.framework.LinkerManager
import io.framed.framework.PreviewLinker
import io.framed.framework.pictogram.*
import io.framed.framework.util.property
import io.framed.framework.util.trackHistory
import io.framed.framework.view.*
import io.framed.model.Event
import io.framed.model.EventType

class EventLinker(
        override val model: Event,
        override val parent: ContainerLinker
) : PreviewLinker<Event, IconShape, IconShape> {

    private val typeProperty = property(model::type).trackHistory()
    private val symbolProperty = property(typeProperty,
            getter = {
                typeProperty.get().symbol
            }
    )

    override val pictogram: IconShape = iconShape(symbolProperty) {
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
    }

    override val preview: IconShape = iconShape(symbolProperty) {
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
    }

    override val sidebar = sidebar {
        title("Event")

        group("General") {
            select("Type", EventType.values().toList(), typeProperty) {
                it.printableName
            }
        }
    }

    override val contextMenu = contextMenu {
        title = "Event"
        addItem(MaterialIcon.DELETE, "Delete") {
            delete()
        }
    }

    init {
        LinkerManager.setup(this)
    }

    companion object : LinkerInfoItem {
        override fun canCreate(container: Linker<*, *>): Boolean = container is ContainerLinker
        override val name: String = "Event"
    }
}