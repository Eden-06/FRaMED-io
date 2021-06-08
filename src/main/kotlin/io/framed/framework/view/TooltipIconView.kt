package io.framed.framework.view

import de.westermann.kobserve.Property
import de.westermann.kobserve.ReadOnlyProperty
import kotlinx.dom.clear
import org.w3c.dom.HTMLSpanElement

/**
 * Represents all kinds of icon views.
 *
 * @author lars
 */
class TooltipIconView(
    icon: Icon? = null,
    toolTipText: String = ""
) : View<HTMLSpanElement>("span") {

    constructor(iconProperty: ReadOnlyProperty<out Icon?>,
                toolTipTextProperty: ReadOnlyProperty<out String>
    ) : this(iconProperty.get(), toolTipTextProperty.get()) {
        bind(iconProperty, toolTipTextProperty)
    }

    fun bind(iconProperty: ReadOnlyProperty<out Icon?>,
             toolTipTextProperty: ReadOnlyProperty<out String>) {
        iconProperty.onChange {
            icon = iconProperty.get()
        }
        toolTipTextProperty.onChange {
            toolTipText = TooltipTextView(toolTipTextProperty.get())
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
            html.appendChild(toolTipText.html)
            value?.let {
                html.appendChild(it.element)
            }

        }

    var toolTipText: TooltipTextView = TooltipTextView(toolTipText)
        set(value) {
            field = value
            html.clear()
            html.appendChild(value.html)
            icon?.let {
                html.appendChild(it.element)
            }

        }

    init {
        classes += "tooltip"
        this.icon = icon
        this.toolTipText = TooltipTextView(toolTipText)
    }
}

class TooltipTextView(
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


    fun bind(property: Property<String>) {
        text = property.get()
    }

    init {
        classes += "tooltiptext"
        text = value
    }
}

fun ViewCollection<in TooltipIconView, *>.tooltipIconView(icon: Icon? = null, toolTipText: String = "", init: TooltipIconView.() -> Unit = {}) =
    TooltipIconView(icon, toolTipText).also(this::append).also(init)

fun ViewCollection<in TooltipIconView, *>.tooltipIconView(property: ReadOnlyProperty<out Icon?>, toolTipTextProperty: ReadOnlyProperty<out String>, init: TooltipIconView.() -> Unit = {}) =
    TooltipIconView(property, toolTipTextProperty).also(this::append).also(init)
