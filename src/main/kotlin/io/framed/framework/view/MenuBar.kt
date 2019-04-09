package io.framed.framework.view

import io.framed.framework.util.async
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.events.MouseEvent

class MenuBar : ViewCollection<View<*>, HTMLDivElement>("div") {
    fun menu(name: String, init: Menu.() -> Unit) =
            Menu(name).also(this::append).also(init).also(this::setup)

    private fun setup(menu: Menu) {
        var closeListener: (MouseEvent) -> Unit = {}
        menu.onLabelClick {
            menu.classes += "show"

            async {
                closeListener = Root.onClick.addListener { _ ->
                    menu.classes -= "show"
                    Root.onClick -= closeListener
                }
            }
        }
    }

    init {
        classes += "menu-bar"
    }
}

fun ViewCollection<in MenuBar, *>.menuBar(init: MenuBar.() -> Unit) =
        MenuBar().also(this::append).also(init)