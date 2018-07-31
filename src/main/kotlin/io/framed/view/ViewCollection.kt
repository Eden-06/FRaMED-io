package io.framed.view

import org.w3c.dom.HTMLElement
import kotlin.dom.clear

/**
 * @author lars
 */
abstract class ViewCollection<V : View<*>, T : HTMLElement>(view: T) : View<T>(view) {

    constructor(tagName: String) : this(createView<T>(tagName))

    protected var children: List<V> = emptyList()

    fun append(view: V) {
        children += view
        html.appendChild(view.html)
    }

    operator fun plusAssign(view: V) = append(view)

    fun prepand(view: V) {
        children = listOf(view) + children
        html.insertBefore(view.html, html.firstChild)
    }

    fun remove(view: V) {
        children -= view
        html.removeChild(view.html)
    }

    operator fun minusAssign(view: V) = remove(view)

    fun clear() {
        children = emptyList()
        html.clear()
    }
}