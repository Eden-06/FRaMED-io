package io.framed.framework.view

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
        if (children.contains(view)) {
            children -= view
            html.removeChild(view.html)
        }
    }

    fun toForeground(view: V) {
        if (view in children && children.indexOf(view) < children.size - 1) {
            remove(view)
            append(view)
        }
    }

    fun toBackground(view: V) {
        if (view in children && children.indexOf(view) > 0) {
            remove(view)
            prepand(view)
        }
    }

    fun first(): V = children.first()
    fun last(): V = children.last()

    operator fun minusAssign(view: V) = remove(view)

    val isEmpty: Boolean
        get() = children.isEmpty()

    fun clear() {
        children = emptyList()
        html.clear()
    }

    operator fun contains(view: V) = children.contains(view)
}