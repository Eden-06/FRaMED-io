package io.framed.view

import org.w3c.dom.HTMLElement
import kotlin.dom.clear

/**
 * @author lars
 */
abstract class ViewCollection<V : View<*>, T : HTMLElement>(tagName: String) : View<T>(tagName) {

    protected var children: List<V> = emptyList()
        set(value) {
            html.clear()
            field = value
            field.forEach {
                html.appendChild(it.html)
            }
        }

    fun append(view: V) {
        children += view
    }

    operator fun plusAssign(view: V) = append(view)

    fun prepand(view: V) {
        children = listOf(view) + children
    }

    fun remove(view: V) {
        children -= view
    }

    operator fun minusAssign(view: V) = remove(view)

    fun clear() {
        children = emptyList()
    }
}