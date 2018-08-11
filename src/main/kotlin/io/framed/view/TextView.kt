package io.framed.view

import io.framed.util.EventHandler
import io.framed.util.Property
import org.w3c.dom.HTMLSpanElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.EventListener
import org.w3c.dom.events.FocusEvent
import org.w3c.dom.events.KeyboardEvent

/**
 * Represents a html span element.
 *
 * @author lars
 */
class TextView(
        value: String = ""
) : View<HTMLSpanElement>("span") {

    /**
     * Text to display.
     */
    var text: String
        get() = html.textContent ?: ""
        set(value) {
            html.textContent = value
        }

    /**
     * Sets the content to editable.
     */
    var contentEditable: Boolean by AttributeDelegate(Boolean::class, false)

    /**
     * If the content is editable, prevents the insertion of line breaks.
     */
    var singleLine: Boolean by ClassDelegate()

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


    fun bind(property: Property<String>) {
        text = property.get()
        onChange {
            property.set(it)
        }
        onFocusLeave {
            property.set(text.trim())
        }

        property.onChange {
            if (text != it) {
                text = it
            }
        }
    }

    init {
        text = value

        val changeListener = object : EventListener {
            override fun handleEvent(event: Event) {
                onChange.fire(text)

                (event as? KeyboardEvent)?.let { e ->
                    when (e.keyCode) {
                        13, 27 -> blur()
                    }
                }
            }
        }
        html.addEventListener("onchange", changeListener)
        html.addEventListener("keypress", changeListener)

        html.addEventListener("focus", onFocusEnter.eventListener)
        html.addEventListener("blur", onFocusLeave.eventListener)
    }
}

fun ListView.textView(init: TextView.() -> Unit): TextView {
    val view = TextView()
    append(view)
    init(view)
    return view
}

fun ListView.textView(text: String, init: TextView.() -> Unit = {}) = textView {
    this.text = text
    init(this)
}