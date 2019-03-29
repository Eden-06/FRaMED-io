package io.framed.linker

import de.westermann.kobserve.ReadOnlyProperty
import de.westermann.kobserve.basic.join
import de.westermann.kobserve.basic.mapBinding
import de.westermann.kobserve.basic.property
import io.framed.framework.*
import io.framed.framework.pictogram.*
import io.framed.framework.util.trackHistory
import io.framed.framework.view.Sidebar
import io.framed.framework.view.SidebarGroup
import io.framed.framework.view.sidebar
import io.framed.model.Compartment
import io.framed.model.EventType
import io.framed.model.ReturnEvent
import io.framed.model.Scene
import kotlin.math.roundToInt

class ReturnEventLinker(
        override val model: ReturnEvent,
        override val parent: ModelLinker<*, *, *>
) : PreviewLinker<ReturnEvent, IconShape, BoxShape> {

    private val typeProperty = property(model::type).trackHistory()
    private val symbolProperty = typeProperty.mapBinding { it.symbol }

    override val nameProperty: ReadOnlyProperty<String> = typeProperty.mapBinding { it.printableName }
    override val name: String by nameProperty

    private val descriptionProperty = property(model::desc).trackHistory()

    override val pictogram = iconShape(symbolProperty) {
        style {
            background = color(255, 255, 255)
            border {
                style = Border.BorderStyle.SOLID
                width = box(1.0)
                color = box(color(0, 0, 0, 0.3))
                radius = box(20.0)

                double = true
            }
            padding = box(10.0)
        }
    }

    override val preview = boxShape(BoxShape.Position.HORIZONTAL) {
        iconShape(symbolProperty)
        textShape(descriptionProperty)
    }

    private lateinit var sidebarViewGroup: SidebarGroup

    override val sidebar = sidebar {
        title("Event")

        group("General") {
            select("Type", EventType.values().toList(), typeProperty) {
                it.printableName
            }
            input("Description", descriptionProperty)
        }

        sidebarViewGroup = group("Layout") {
            input("Position", pictogram.leftProperty.join(pictogram.topProperty) { left, top ->
                "x=${left.roundToInt()}, y=${top.roundToInt()}"
            })
            input("Size", pictogram.widthProperty.join(pictogram.heightProperty) { width, height ->
                "width=${width.roundToInt()}, height=${height.roundToInt()}"
            })
        }
    }

    override fun Sidebar.onOpen(event: SidebarEvent) {
        sidebarViewGroup.display = event.target == pictogram
    }

    override val contextMenu = defaultContextMenu()

    override fun updateLabelBindings() {
        var label = pictogram.labels.find { it.id == "name" }
        if (label == null) {
            label = Label(id = "name")
            pictogram.labels += label
        }

        if (label.textProperty.isBound) {
            label.textProperty.unbind()
        }
        label.textProperty.bindBidirectional(descriptionProperty)

        super.updateLabelBindings()
    }

    init {
        LinkerManager.setup(this)
    }

    companion object : LinkerInfoItem {
        override fun canCreateIn(container: ModelElement<*>): Boolean {
            return container is Compartment || container is Scene
        }

        override fun isLinkerFor(element: ModelElement<*>): Boolean = element is ReturnEvent
        override fun isLinkerFor(linker: Linker<*, *>): Boolean = linker is ReturnEventLinker

        override fun createModel(): ModelElement<*> = ReturnEvent()
        override fun createLinker(model: ModelElement<*>, parent: Linker<*, *>, connectionManager: ConnectionManager?): Linker<*, *> {
            if (model is ReturnEvent && parent is ModelLinker<*, *, *>) {
                return ReturnEventLinker(model, parent)
            } else throw UnsupportedOperationException()
        }

        override val name: String = "Return event"
    }
}