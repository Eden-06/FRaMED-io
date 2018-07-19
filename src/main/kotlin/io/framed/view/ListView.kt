package io.framed.view

import org.w3c.dom.HTMLDivElement
import kotlin.dom.clear

/**
 * @author lars
 */
class ListView : View<HTMLDivElement>("div") {
    private var children: List<View<*>> = emptyList()
        set(value) {
            html.clear()
            field = value
            field.forEach {
                html.appendChild(it.html)
            }
        }

    operator fun plusAssign(view: View<*>) {
        children += view
    }

    fun insertBegin(view: View<*>) {
        children = listOf(view) + children
    }

    fun clear() {
        children = emptyList()
    }
}