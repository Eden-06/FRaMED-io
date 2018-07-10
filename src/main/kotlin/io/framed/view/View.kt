package io.framed.view

import io.framed.util.EventHandler
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.EventListener
import kotlin.browser.document

/**
 * @author lars
 */
abstract class View<V : HTMLElement>(tagName: String) {

    @Suppress("UNCHECKED_CAST")
    val html: V = document.createElement(tagName) as V

    val click = EventHandler<Unit>()

    init {
        html.addEventListener("click", object :EventListener {
            override fun handleEvent(event: Event) {
                click.fire(Unit)
            }
        })
    }
}