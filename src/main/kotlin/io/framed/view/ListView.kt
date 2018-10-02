package io.framed.view

import org.w3c.dom.HTMLDivElement

/**
 * Represents a html div element.
 *
 * @author lars
 */
class ListView : ViewCollection<View<*>, HTMLDivElement>("div") {}

fun ViewCollection<in ListView, *>.listView(init: ListView.() -> Unit) =
        ListView().also(this::append).also(init)