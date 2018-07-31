package io.framed.view

import io.framed.util.EventHandler
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
    val change = EventHandler<String>()

    /**
     * Fires on focus leave.
     */
    val focusLeave = EventHandler<FocusEvent>()

    /**
     * Fires on focus gain.
     */
    val focusEnter = EventHandler<FocusEvent>()

    var invalid by ClassDelegate()

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
        html.addEventListener("keyup", changeListener)

        val focusListener = object : EventListener {
            override fun handleEvent(event: Event) {
                (event as? FocusEvent)?.let {
                    focusEnter.fire(it)
                }
            }
        }
        val blurListener = object : EventListener {
            override fun handleEvent(event: Event) {
                (event as? FocusEvent)?.let {
                    focusLeave.fire(it)
                }
            }
        }
        html.addEventListener("focus", focusListener)
        html.addEventListener("blur", blurListener)
    }
}

fun ListView.inputView(init: InputView.() -> Unit): InputView {
    val view = InputView()
    append(view)
    init(view)
    return view
}