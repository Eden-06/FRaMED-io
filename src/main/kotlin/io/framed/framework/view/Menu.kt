package io.framed.framework.view

import org.w3c.dom.HTMLDivElement
import org.w3c.dom.set

class Menu(
        name: String
) : ViewCollection<View<*>, HTMLDivElement>("div") {

    val label = textView(name)
    val content = listView { }

    fun menu(name: String, init: Menu.() -> Unit) =
            Menu(name).also(content::append).also(init)

    fun item(icon: Icon? = null, name: String, shortcut: Shortcut? = null, onAction: () -> Unit) = content.listView {
        onClick { onAction() }

        if (icon != null) {
            iconView(icon)
        }
        textView(name)
        if (shortcut != null) {
            Root.shortcut(shortcut, onAction)
            html.dataset["hint"] = shortcut.toString()
        }
    }

    fun separator() {
        if (!content.isEmpty) {
            content.last().classes += "separator"
        }
    }

    val onContentClick = content.onClick
    val onLabelClick = label.onClick

    init {
        classes += "menu"

        iconView(MaterialIcon.ARROW_DROP_DOWN)
    }
}