package io.framed.framework.view

import io.framed.framework.util.EventHandler
import io.framed.framework.util.Property
import org.w3c.dom.HTMLSelectElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.EventListener
import kotlin.dom.clear

/**
 * @author lars
 */

class SelectView<T : Any>(
        initValues: List<T>,
        initSelected: T,
        val transform: (T) -> String
) : View<HTMLSelectElement>("select") {

    constructor(initValues: List<T>, property: Property<T>, transform: (T) -> String) : this(initValues, property.get(), transform) {
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
                    option.text = transform(it)
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

fun <T : Any> ViewCollection<in SelectView<*>, *>.selectView(
        initValues: List<T>,
        initSelected: T,
        transform: (T) -> String
) = SelectView(initValues, initSelected, transform).also(this::append)

fun <T : Any> ViewCollection<in SelectView<*>, *>.selectView(
        initValues: List<T>,
        property: Property<T>,
        transform: (T) -> String
) = SelectView(initValues, property, transform).also(this::append)
