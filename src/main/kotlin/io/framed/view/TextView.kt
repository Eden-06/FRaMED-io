package io.framed.view

import org.w3c.dom.HTMLSpanElement

/**
 * @author lars
 */
class TextView : View<HTMLSpanElement>("span") {
    var text: String
        get() = html.textContent ?: ""
        set(value) {
            html.textContent = value
        }
}