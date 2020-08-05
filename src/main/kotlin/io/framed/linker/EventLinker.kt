package io.framed.linker

import de.westermann.kobserve.ReadOnlyProperty
import de.westermann.kobserve.property.mapBinding
import de.westermann.kobserve.property.property
import io.framed.framework.*
import io.framed.framework.linker.*
import io.framed.framework.model.ModelElement
import io.framed.framework.pictogram.*
import io.framed.framework.util.advanced
import io.framed.framework.util.trackHistory
import io.framed.framework.view.*
import io.framed.model.*

class EventLinker(
        override val model: Event,
        override val parent: ModelLinker<*, *, *>
) : PreviewLinker<Event, IconShape, BoxShape> {

    private val typeProperty = property(model::type).trackHistory()
    private val symbolProperty = typeProperty.mapBinding { it.symbol }
    private val symbolNoBlankProperty = typeProperty.mapBinding {
        if (it == EventType.STANDARD) FramedIcon.EVENT else it.symbol
    }

    override val nameProperty: ReadOnlyProperty<String> = typeProperty.mapBinding { it.printableName }
    override val name: String by nameProperty

    private val descriptionProperty = property(model::desc).trackHistory()

    override val pictogram = iconShape(symbolProperty) {
        style {
            background = color(255, 255, 255)
            border {
                style = Border.BorderStyle.SOLID
                width = box(Border.DEFAULT_WIDTH)
                color = box(color(0, 0, 0, 0.3))
                radius = box(30.0)
            }
            padding = box(10.0, 10.0)
        }
    }

    override val preview = boxShape(BoxShape.Position.HORIZONTAL) {
        iconShape(symbolNoBlankProperty)
        textShape(descriptionProperty)

        style {
            padding = box(0.0, 0.0, 0.0, 30.0)
        }

        ignoreLabels = true
    }

    override val sidebar = sidebar {
        title("Event")

        group("General") {
            select("io.framed.exporter.crom.Type", EventType.values().toList(), typeProperty) {
                it.printableName
            }
            input("Description", descriptionProperty)
        }

        advanced(pictogram)
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

        override val info = ElementInfo("Event", FramedIcon.EVENT)

        override fun canCreateIn(container: ModelElement): Boolean {
            return container is Package || container is Compartment || container is Scene
        }

        override fun isLinkerFor(element: ModelElement): Boolean = element is Event
        override fun isLinkerFor(linker: Linker<*, *>): Boolean = linker is EventLinker

        override fun createModel(): ModelElement = Event()
        override fun createLinker(model: ModelElement, parent: Linker<*, *>, connectionManager: ConnectionManager?): Linker<*, *> {
            if (model is Event && parent is ModelLinker<*, *, *>) {
                return EventLinker(model, parent)
            } else throw IllegalArgumentException("Cannot create ${info.name} linker for model element ${model::class}")
        }
    }
}