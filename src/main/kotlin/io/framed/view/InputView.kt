package io.framed.view

import io.framed.util.EventHandler
import io.framed.util.Property
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.EventListener
import org.w3c.dom.events.FocusEvent
import org.w3c.dom.events.KeyboardEvent

/**
 * Represents html input element.
 *
 * @author lars
 */
class InputView : View<HTMLInputElement>("input") {

    /**
     * Inputs value.
     */
    var value: String
        get() = html.value
        set(value) {
            html.value = value
        }

    /**
     * Set input to readonly.
     */
    var readOnly: Boolean by AttributeDelegate(Boolean::class, false)

    /**
     * Fires on every user change to the content
     */
    val onChange = EventHandler<String>()

    /**
     * Fires on focus leave.
     */
    val onFocusLeave = EventHandler<FocusEvent>()

    /**
     * Fires on focus gain.
     */
    val onFocusEnter = EventHandler<FocusEvent>()

    var invalid by ClassDelegate()

    fun bind(property: Property<String>) {
        value = property.get()
        onChange {
            property.set(it)
        }
        onFocusLeave {
            property.set(value.trim())
        }

        property.onChange {
            if (value != it) {
                value = it
            }
        }
    }

    init {
        val changeListener = object : EventListener {
            override fun handleEvent(event: Event) {
                onChange.fire(value)

                (event as? KeyboardEvent)?.let { e ->
                    when (e.keyCode) {
                        13, 27 -> blur()
                    }
                }
            }
        }

        html.addEventListener("onchange", changeListener)
        html.addEventListener("keyup", changeListener)

        html.addEventListener("focus", onFocusEnter.eventListener)
        html.addEventListener("blur", onFocusLeave.eventListener)
    }
}

fun ListView.inputView(init: InputView.() -> Unit): InputView {
    val view = InputView()
    append(view)
    init(view)
    return view
}