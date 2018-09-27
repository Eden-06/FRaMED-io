package io.framed.view

import org.w3c.dom.HTMLDivElement

/**
 * @author lars
 */
class Sidebar(
        val application: Application
) : ViewCollection<View<*>, HTMLDivElement>("div") {

    fun setup(vararg initiator: View<*>, init: Sidebar.() -> Unit) {
        clear()
        initiator.forEach { view ->
            view.onClick {
                it.stopPropagation()
                display()
            }

            (view as? InputView)?.let { input ->
                input.onFocusEnter {
                    display()
                }
            }
        }
        init()
    }

    fun display() {
        application.let { a ->
            a.propertyBar.clear()
            a.propertyBar += this
        }
    }

    fun group(name: String, init: SidebarGroup.() -> Unit): SidebarGroup {
        val view = SidebarGroup(name).also(init)
        append(view)
        return view
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