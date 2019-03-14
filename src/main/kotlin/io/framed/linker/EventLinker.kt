package io.framed.linker

import de.westermann.kobserve.ReadOnlyProperty
import de.westermann.kobserve.basic.join
import de.westermann.kobserve.basic.mapBinding
import de.westermann.kobserve.basic.property
import io.framed.framework.*
import io.framed.framework.pictogram.*
import io.framed.framework.util.trackHistory
import io.framed.framework.view.CheckBox
import io.framed.framework.view.Sidebar
import io.framed.framework.view.SidebarGroup
import io.framed.framework.view.sidebar
import io.framed.model.*
import kotlin.math.roundToInt

class EventLinker(
        override val model: Event,
        override val parent: ModelLinker<*, *, *>
) : PreviewLinker<Event, IconShape, IconShape> {

    private val typeProperty = property(model::type).trackHistory()
    private val symbolProperty = typeProperty.mapBinding { it.symbol }

    override val nameProperty: ReadOnlyProperty<String> = symbolProperty.mapBinding {
        it?.let { it::class.simpleName } ?: "Unknown"
    }
    override val name: String by nameProperty

    private val descriptionProperty = property(model::desc).trackHistory()

    private val returnEventProperty = property(model::returnEvent).trackHistory()
    var returnEvent by returnEventProperty

    override val pictogram = iconShape(symbolProperty) {
        style {
            background = color(255, 255, 255)
            border {
                style = Border.BorderStyle.SOLID
                width = box(1.0)
                color = box(color(0, 0, 0, 0.3))
                radius = box(20.0)
                double = returnEvent
                returnEventProperty.onChange {
                    double = returnEvent
                }
            }
            padding = box(10.0)
        }
    }

    override val preview = iconShape(symbolProperty) {
        style {
            background = color(255, 255, 255)
            border {
                style = Border.BorderStyle.SOLID
                width = box(1.0)
                color = box(color(0, 0, 0, 0.3))
                radius = box(20.0)
                double = returnEvent
                returnEventProperty.onChange {
                    double = returnEvent
                }
            }
            padding = box(10.0)
        }
    }

    private lateinit var sidebarViewGroup: SidebarGroup

    override val sidebar = sidebar {
        title("Event")

        group("General") {
            select("Type", EventType.values().toList(), typeProperty) {
                it.printableName
            }
            input("Description", descriptionProperty)
            checkBox("Return event", returnEventProperty, CheckBox.Type.SWITCH)
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

    init {
        LinkerManager.setup(this)

        returnEventProperty.onChange {
            parent.redraw(this)
        }
    }

    companion object : LinkerInfoItem {
        override fun canCreateIn(container: ModelElement<*>): Boolean {
            return container is Package || container is Compartment || container is Scene
        }

        override fun isLinkerFor(element: ModelElement<*>): Boolean = element is Event
        override fun isLinkerFor(linker: Linker<*, *>): Boolean = linker is EventLinker

        override fun createModel(): ModelElement<*> = Event()
        override fun createLinker(model: ModelElement<*>, parent: Linker<*, *>, connectionManager: ConnectionManager?): Linker<*, *> {
            if (model is Event && parent is ModelLinker<*,*, *>) {
                return EventLinker(model, parent)
            } else throw UnsupportedOperationException()
        }

        override val name: String = "Event"
    }
}