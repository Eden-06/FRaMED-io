package io.framed.framework.view

import org.w3c.dom.HTMLButtonElement

/**
 * @author lars
 */
class Button : View<HTMLButtonElement>("button") {

    /**
     * Text to display.
     */
    var text: String
        get() = html.textContent ?: ""
        set(value) {
            html.textContent = value
        }

    val primaryProperty by ClassDelegate()
    var primary by primaryProperty
    val alertProperty by ClassDelegate()
    var alert by alertProperty
    val errorProperty by ClassDelegate()
    var error by errorProperty
}

fun ListView.button(init: Button.() -> Unit): Button {
    val view = Button()
    append(view)
    init(view)
    return view
}

fun ListView.button(text: String, onClick: () -> Unit = {}) = button {
    this.text = text
    onClick { onClick() }
}