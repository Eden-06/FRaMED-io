package io.framed.framework.view

import org.w3c.dom.HTMLDivElement

/**
 * @author lars
 */
class Sidebar : ViewCollection<View<*>, HTMLDivElement>("div") {

    fun open() {
        Application.propertyBar.content.clear()
        Application.propertyBar.content += this
    }

    fun group(name: String, init: SidebarGroup.() -> Unit) = SidebarGroup(name).also(init).also(this::append)

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
