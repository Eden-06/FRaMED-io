package io.framed.framework.view

import de.westermann.kobserve.property.join
import de.westermann.kobserve.property.readOnly
import io.framed.framework.pictogram.BoxShape
import io.framed.framework.pictogram.Shape
import org.w3c.dom.HTMLDivElement
import kotlin.math.roundToInt

/**
 * @author lars
 */
class Sidebar : ViewCollection<View<*>, HTMLDivElement>("div") {

    fun open() {
        Application.propertyBar.content.clear()
        Application.propertyBar.content += this
    }

    private var groups: Map<String, SidebarGroup> = emptyMap()

    fun group(name: String, init: SidebarGroup.() -> Unit): SidebarGroup {
        val group = groups[name] ?: run {
            SidebarGroup(name).also(this::append).also {
                groups += name to it
            }
        }
        group.init()
        return group
    }

    fun title(text: String): TextView {
        val view = TextView().also {
            it.classes += "header"
        }
        view.text = text
        append(ListView().also {
            it += view
        })
        return view
    }
}

/**
 * Create a new sidebar.
 *
 * @param init Builder for the new sidebar.
 *
 * @return The new sidebar.
 */
fun sidebar(init: Sidebar.() -> Unit): Sidebar = Sidebar().also(init)
