package io.framed.framework.view

import de.westermann.kobserve.ReadOnlyProperty
import org.w3c.dom.HTMLSpanElement
import kotlin.dom.clear

/**
 * Represents all kinds of icon views.
 *
 * @author lars
 */
class IconView(icon: Icon? = null) : View<HTMLSpanElement>("span") {

    constructor(property: ReadOnlyProperty<out Icon?>) : this(property.get()) {
        bind(property)
    }

    fun bind(property: ReadOnlyProperty<out Icon?>) {
        property.onChange {
            icon = property.get()
        }
    }

    val inactiveProperty by ClassDelegate()
    var inactive by inactiveProperty

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

fun ViewCollection<in IconView, *>.iconView(icon: Icon? = null, init: IconView.() -> Unit = {}) =
        IconView(icon).also(this::append).also(init)

fun ViewCollection<in IconView, *>.iconView(property: ReadOnlyProperty<out Icon?>, init: IconView.() -> Unit = {}) =
        IconView(property).also(this::append).also(init)