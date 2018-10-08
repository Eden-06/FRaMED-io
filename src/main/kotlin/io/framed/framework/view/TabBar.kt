package io.framed.framework.view

import io.framed.framework.util.Property
import org.w3c.dom.HTMLDivElement
import kotlin.math.max
import kotlin.math.min

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

                if (index >= 0) {
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