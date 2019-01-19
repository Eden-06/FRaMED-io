package io.framed.linker

import de.westermann.kobserve.ReadOnlyProperty
import de.westermann.kobserve.basic.join
import de.westermann.kobserve.basic.mapBinding
import de.westermann.kobserve.basic.property
import io.framed.framework.*
import io.framed.framework.pictogram.*
import io.framed.framework.util.trackHistory
import io.framed.framework.view.*
import io.framed.model.Compartment
import io.framed.model.Event
import io.framed.model.EventType
import io.framed.model.Package
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

    override val listPreview = iconShape(symbolProperty) {
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

    override val flatPreview = iconShape(symbolProperty) {
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
    private lateinit var sidebarFlatViewGroup: SidebarGroup

    override val sidebar = sidebar {
        title("Event")

        group("General") {
            select("Type", EventType.values().toList(), typeProperty) {
                it.printableName
            }
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
        sidebarFlatViewGroup = group("Preview layout") {
            input("Position", flatPreview.leftProperty.join(flatPreview.topProperty) { left, top ->
                "x=${left.roundToInt()}, y=${top.roundToInt()}"
            })
            input("Size", flatPreview.widthProperty.join(flatPreview.heightProperty) { width, height ->
                "width=${width.roundToInt()}, height=${height.roundToInt()}"
            })
        }
    }

    override fun Sidebar.onOpen(event: SidebarEvent) {
        sidebarViewGroup.display = event.target == pictogram
        sidebarFlatViewGroup.display = event.target == flatPreview
    }

    override val contextMenu = contextMenu {
        title = "Event"
        addItem(MaterialIcon.DELETE, "Delete") {
            delete()
        }
    }

    init {
        LinkerManager.setup(this)

        returnEventProperty.onChange {
            parent.redraw(this)
        }
    }

    companion object : LinkerInfoItem {
        override fun canCreateIn(container: ModelElement<*>): Boolean {
            return container is Package || container is Compartment
        }

        override fun isLinkerOfType(element: ModelElement<*>): Boolean = element is Event

        override val name: String = "Event"
    }
}