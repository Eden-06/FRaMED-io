package io.framed.framework.view

import org.w3c.dom.HTMLOptionElement

/**
 * @author lars
 */
class OptionView : View<HTMLOptionElement>("option") {
    var value: String
        get() = html.value
        set(value) {
            html.value = value
        }

    var text: String
        get() = html.text
        set(value) {
            html.text = value
        }

    val index: Int
        get() = html.index

    var selected: Boolean
        get() = html.selected
        set(value) {
            html.selected = value
        }

}