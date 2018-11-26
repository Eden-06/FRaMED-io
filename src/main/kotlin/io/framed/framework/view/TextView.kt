package io.framed.framework.view

import de.westermann.kobserve.EventHandler
import de.westermann.kobserve.Property
import io.framed.framework.util.eventListener
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

    constructor(text: Property<String>):this() {
        bind(text)
    }

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
            val new = property.get()
            if (text != new) {
                text = new
            }
        }
    }

    init {
        text = value

        val changeListener = object : EventListener {
            override fun handleEvent(event: Event) {
                onChange.emit(text)

                (event as? KeyboardEvent)?.let { e ->
                    when (e.keyCode) {
                        13, 27 -> blur()
                    }
                }
            }
        }
        html.addEventListener("onchange", changeListener)
        html.addEventListener("keypress", changeListener)

        html.addEventListener("focus", onFocusEnter.eventListener())
        html.addEventListener("blur", onFocusLeave.eventListener())
    }
}

fun ViewCollection<in TextView, *>.textView(text: String = "", init: TextView.() -> Unit = {}) =
        TextView(text).also(this::append).also(init)

fun ViewCollection<in TextView, *>.textView(text: Property<String>, init: TextView.() -> Unit = {}) =
        TextView(text).also(this::append).also(init)
