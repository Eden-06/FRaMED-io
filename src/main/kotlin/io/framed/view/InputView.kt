package io.framed.view

import io.framed.util.EventHandler
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.EventListener
import org.w3c.dom.events.KeyboardEvent

/**
 * @author lars
 */
class InputView : View<HTMLInputElement>("input") {
    var value: String
        get() = html.value
        set(value) {
            html.value = value
        }


    var readOnly: Boolean by AttributeDelegate(Boolean::class, false)
    /**
     * Fires on every user change to the content
     */
    val change = EventHandler<String>()

    init {
        val changeListener = object : EventListener {
            override fun handleEvent(event: Event) {
                change.fire(value)

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