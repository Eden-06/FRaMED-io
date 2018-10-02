package io.framed.view

import io.framed.util.EventHandler
import io.framed.util.Property
import org.w3c.dom.HTMLSelectElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.EventListener
import kotlin.dom.clear

/**
 * @author lars
 */

class SelectView<T : Any>(
        initValues: List<T>,
        initSelected: T
) : View<HTMLSelectElement>("select") {

    constructor(initValues: List<T>, property: Property<T>) : this(initValues, property.get()) {
        bind(property)
    }

    fun bind(property: Property<T>) {
        onChange {
            property.set(it);
        }

        property.onChange {
            selected = property.get()
        }
    }

    var values: List<T> = emptyList()
        set(value) {
            field = value
            html.clear()

            value.forEach {
                html.appendChild(OptionView().also { option ->
                    option.text = it.toString()
                    option.value = it.toString()
                }.html)
            }
        }

    var index: Int
        get() = html.selectedIndex
        set(value) {
            html.selectedIndex = value
        }

    var selected: T
        get() = values[index]
        set(value) {
            index = values.indexOf(value)
        }

    val onChange = EventHandler<T>()

    init {
        this.values = initValues
        this.selected = initSelected


        html.addEventListener("change", object : EventListener {
            override fun handleEvent(event: Event) {
                onChange.fire(selected)
            }
        })
    }
}