package io.framed.view

import org.w3c.dom.HTMLDivElement

/**
 * Represents a html div element.
 *
 * @author lars
 */
class ListView : ViewCollection<View<*>, HTMLDivElement>("div") {}
fun ListView.listView(init: ListView.() -> Unit): ListView {
    val view = ListView()
    append(view)
    init(view)
    return view
}