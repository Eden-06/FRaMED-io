package io.framed.view

import org.w3c.dom.HTMLDivElement

class Menu(
        name: String
) : ViewCollection<View<*>, HTMLDivElement>("div") {

    val label = textView(name)
    val content= listView { }

    fun menu(name: String, init: Menu.() -> Unit) =
            Menu(name).also(content::append).also(init)

    fun item(name: String, onAction: () -> Unit) = content.textView(name) {
        onClick { onAction() }
    }

    val onContentClick = content.onClick
    val onLabelClick = label.onClick

    init {
        classes += "menu"

        iconView(MaterialIcon.ARROW_DROP_DOWN)
    }
}