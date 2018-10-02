package io.framed.view

import io.framed.util.Property
import org.w3c.dom.HTMLSpanElement
import kotlin.dom.clear

/**
 * Represents all kinds of icon views.
 *
 * @author lars
 */
class IconView(icon: Icon? = null) : View<HTMLSpanElement>("span") {

    constructor(property: Property<out Icon>) : this(property.get()) {
        bind(property)
    }

    fun bind(property: Property<out Icon>) {
        property.onChange {
            icon = property.get()
        }
    }

    /**
     * The icon to display.
     */
    var icon: Icon? = null
        set(value) {
            field = value
            html.clear()
            value?.let {
                html.appendChild(it.element)
            }
        }

    init {
        this.icon = icon
    }
}

fun ViewCollection<in IconView, *>.iconView(icon: Icon? = null, init: IconView.() -> Unit = {})=
        IconView(icon).also(this::append).also(init)