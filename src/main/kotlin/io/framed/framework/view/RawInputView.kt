package io.framed.framework.view

import de.westermann.kobserve.Property
import de.westermann.kobserve.ReadOnlyProperty
import de.westermann.kobserve.ValidationProperty
import de.westermann.kobserve.event.EventHandler
import de.westermann.kobserve.property.property
import io.framed.framework.util.bind
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
            val oldValue = html.value
            val oldSelection = html.selectionStart
            val oldLength = oldValue.length

            html.value = value
            lastValue = value
            if (sizeMatch) {
                size = max(value.length, 1)
            }

            if (oldSelection != null) {
                val pos: Int
                if (value.endsWith("()") && value.dropLast(2) == oldValue) {
                    pos = oldLength
                } else {
                    pos = value.length - oldLength + oldSelection
                }
                html.setSelectionRange(pos, pos)
            }
        }

    val readOnlyProperty = property(false).also { property ->
        property.onChange {
            if (property.value) {
                html.setAttribute("readonly", "true")
            } else {
                html.removeAttribute("readonly")
            }
        }
    }

    /**
     * Set input to readonly.
     */
    var readOnly: Boolean by readOnlyProperty

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

    val invalidProperty by ClassDelegate()
    var invalid by invalidProperty

    fun bind(property: ReadOnlyProperty<String>) {
        value = property.get()

        readOnly = !(property is Property<String> && property.isWritable)

        onChange {
            if (property is Property<String> && property.isWritable) {
                property.set(it)
            }
        }
        onFocusEnter {
            readOnly = !(property is Property<String> && property.isWritable)
        }
        onFocusLeave {
            if (property is Property<String> && property.isWritable) {
                property.set(value.trim())
            }
        }

        property.onChange {
            value = property.get()
        }

        if (property is ValidationProperty<String>) {
            property.validProperty.onChange {
                invalid = !property.valid
            }
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
                    onChange.emit(value)
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

        onFocusEnter.bind(html, "focus")
        onFocusLeave.bind(html, "focusout")
    }
}
