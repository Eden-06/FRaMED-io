package io.framed.framework.view

import org.w3c.dom.HTMLDivElement

/**
 * View that represents the sidebar of an diagram element.
 *
 * @author lars
 */
class Sidebar : ViewCollection<View<*>, HTMLDivElement>("div") {

    /**
     * Display this sidebar instance.
     */
    fun open() {
        Application.propertyBar.content.clear()
        Application.propertyBar.content += this
    }

    private var groups: Map<String, SidebarGroup> = emptyMap()

    /**
     * Edit a sidebar group or create a new one if it does not exist.
     */
    fun group(name: String, init: SidebarGroup.() -> Unit): SidebarGroup {
        val group = groups[name] ?: run {
            SidebarGroup(name).also(this::append).also {
                groups += name to it
            }
        }
        group.init()
        return group
    }

    /**
     * That the title of the sidebar.
     */
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
