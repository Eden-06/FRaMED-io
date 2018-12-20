package io.framed.framework.view

import de.westermann.kobserve.Property
import org.w3c.dom.HTMLDivElement
import kotlin.math.max

class TabBar : ViewCollection<View<*>, HTMLDivElement>("div") {

    var tabs = emptyList<Tab>()

    fun tab(name: Property<String>): Tab {
        val tab = Tab(name)

        tab.onOpen { _ ->
            (tabs - tab).forEach { it.selected = false }
        }
        tab.onClose { _ ->
            if (tab.selected) {
                val index = max(0, tabs.indexOf(tab) - 1)
                remove(tab)
                tabs -= tab

                if (index >= 0 && index < tabs.size) {
                    tabs[index].open()
                }
            } else {
                remove(tab)
                tabs -= tab
            }
        }

        append(tab)
        tabs += tab
        return tab
    }
}

fun ViewCollection<in TabBar, *>.tabBar(init: TabBar.() -> Unit) =
        TabBar().also(this::append).also(init)