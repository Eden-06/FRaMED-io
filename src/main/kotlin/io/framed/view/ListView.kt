package io.framed.view

import org.w3c.dom.HTMLDivElement
import kotlin.dom.clear

/**
 * Represents a html div element.
 *
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

    /**
     * Add view at the end of the list.
     */
    operator fun plusAssign(view: View<*>) {
        children += view
    }

    /**
     * Add view at the beginning of the list.
     */
    fun insertBegin(view: View<*>) {
        children = listOf(view) + children
    }

    /**
     * Remove view from the list.
     */
    operator fun minusAssign(view: View<*>) {
        children -= view
    }

    /**
     * Remove all views from the list.
     */
    fun clear() {
        children = emptyList()
    }
}