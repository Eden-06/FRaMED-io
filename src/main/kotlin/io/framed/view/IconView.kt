package io.framed.view

import org.w3c.dom.HTMLSpanElement
import kotlin.dom.clear

/**
 * @author lars
 */
class IconView : View<HTMLSpanElement>("span") {
    var icon: Icon? = null
        set(value) {
            field = value
            html.clear()
            value?.let {
                html.appendChild(it.element)
            }
        }
}