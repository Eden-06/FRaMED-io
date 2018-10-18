package io.framed.framework.view

import io.framed.framework.util.EventHandler
import io.framed.framework.util.Property
import io.framed.framework.util.Validator
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.EventListener
import org.w3c.dom.events.FocusEvent
import org.w3c.dom.events.KeyboardEvent
import kotlin.math.max

/**
 * Represents html input element.
 *
 * @author lars
 */
class RawInputView() : View<HTMLInputElement>("input") {

    /**
     * Inputs value.
     */
    var value: String
        get() = html.value
        set(value) {
            html.value = value
            lastValue = value
            if (sizeMatch) {
                size = max(value.length, 1)
            }
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
        var hasFocus = false
        value = property.get()
        onChange {
            hasFocus = true
            invalid = property.set(it) != Validator.Result.VALID
        }
        onFocusLeave {
            hasFocus = false
            invalid = property.set(value.trim()) != Validator.Result.VALID
        }

        property.onChange {
            if (!hasFocus) {
                value = property.get()
            }
        }

        if (!property.editable) {
            readOnly = true
        }
    }

    var size by AttributeDelegate(Int::class, 0)

    private var sizeMatch = false
    fun sizeMatchText() {
        sizeMatch = true
        size = max(value.length, 1)
        onChange {
            size = max(value.length, 1)
        }
    }


    private var lastValue: String = value

    init {
        val changeListener = object : EventListener {
            override fun handleEvent(event: Event) {
                if (value != lastValue) {
                    onChange.fire(value)
                    lastValue = value
                }

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
