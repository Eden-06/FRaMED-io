package io.framed.view

import io.framed.util.EventHandler
import org.w3c.dom.HTMLSpanElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.EventListener
import org.w3c.dom.events.KeyboardEvent

/**
 * @author lars
 */
class TextView : View<HTMLSpanElement>("span") {
    var text: String
        get() = html.textContent ?: ""
        set(value) {
            html.textContent = value
        }

    /**
     * Sets the content to editable
     */
    var contentEditable: Boolean by AttributeDelegate(Boolean::class, false)

    /**
     * If the content is editable, prevents the insertion of line breaks
     */
    var singleLine: Boolean by ClassDelegate()

    /**
     * Fires on every user change to the content
     */
    val change = EventHandler<String>()

    init {
        val changeListener = object : EventListener {
            override fun handleEvent(event: Event) {
                change.fire(text)

                (event as? KeyboardEvent)?.let { e ->
                    when (e.keyCode) {
                        13, 27 -> blur()
                    }
                }
            }
        }
        html.addEventListener("change", changeListener)
        html.addEventListener("keypress", changeListener)
    }
}