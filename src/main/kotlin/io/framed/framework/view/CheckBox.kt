package io.framed.framework.view

import de.westermann.kobserve.Property
import de.westermann.kobserve.ReadOnlyProperty
import de.westermann.kobserve.event.EventHandler
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLLabelElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.EventListener

/**
 * Represents html input element.
 *
 * @author lars
 */
class CheckBox(label: String, property: ReadOnlyProperty<Boolean>) : View<HTMLDivElement>("div") {

    private val checkbox = createView<HTMLInputElement>("input")
    private val htmlLabel = createView<HTMLLabelElement>("label")

    var label: String
        get() = htmlLabel.textContent ?: ""
        set(value) {
            htmlLabel.textContent = value
        }

    val onChange = EventHandler<Boolean>()

    var type = Type.CHECKBOX
        set(value) {
            Type.values().forEach {
                classes -= "type-${it.name.lowercase()}"
            }
            classes += "type-${value.name.lowercase()}"
            field = value
        }

    var state: Boolean
        get() = checkbox.checked
        set(value) {
            if (value != checkbox.checked) {
                checkbox.checked = value
            }
        }

    var readOnly: Boolean
        get() = checkbox.disabled
        set(value) {
            checkbox.disabled = value
        }

    init {
        html.appendChild(checkbox)
        html.appendChild(htmlLabel)

        checkbox.type = "checkbox"
        checkbox.addEventListener("click", object : EventListener {
            override fun handleEvent(event: Event) {
                onChange.emit(state)
            }
        })

        val id = "checkbox-${lastId++}"
        classes += "type-${Type.CHECKBOX.name.lowercase()}"

        checkbox.id = id
        htmlLabel.htmlFor = id

        this.label = label

        state = property.value
        property.onChange {
            state = property.value
        }
        if (property is Property<Boolean>) {
            onChange(property::set)
        } else {
            readOnly = true
        }
    }

    companion object {
        private var lastId = 0
    }

    enum class Type {

        /**
         * A classical checkbox with a check mark.
         */
        CHECKBOX,

        /**
         * A modern on/off switch.
         */
        SWITCH
    }
}

fun ViewCollection<in CheckBox, *>.checkBox(label: String, property: Property<Boolean>, init: CheckBox.() -> Unit) =
        CheckBox(label, property).also(this::append).also(init)