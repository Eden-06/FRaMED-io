package io.framed.framework.view

import de.westermann.kobserve.Property
import de.westermann.kobserve.ReadOnlyProperty
import org.w3c.dom.HTMLDivElement

/**
 * View that represents a sidebar group.
 *
 * @author lars
 */
class SidebarGroup(
        val name: String
) : ViewCollection<View<*>, HTMLDivElement>("div") {

    /**
     * BooleanProperty that describes if this group is collapsed or not.
     */
    val collapsedProperty by ClassDelegate()

    /**
     * Property that describes if this group is collapsed or not.
     */
    var collapsed by collapsedProperty

    /**
     * Create a new input field in this group.
     *
     * @param label The name of the field.
     * @param property The property to read (and write) the field value. If the property is read only, the field will be read only too.
     * @param autocomplete Optional list of possible autocomplete values.
     */
    fun input(label: String, property: ReadOnlyProperty<String>, autocomplete: List<String> = emptyList()): InputView {
        val i = InputView(property)
        i.autocomplete(autocomplete)

        append(ListView().also {
            it += TextView(label)
            it += i
        })
        return i
    }

    /**
     * Create a new input field in this group.
     *
     * @param label The name of the field.
     * @param property The property to read (and write) the field value. If the property is read only, the field will be read only too.
     * @param autocompleter Optional lambda to get a dynamic list of possible autocomplete values.
     */
    fun input(label: String, property: ReadOnlyProperty<String>, autocompleter: (partial: String) -> List<String>): InputView {
        val i = InputView(property)
        i.autocomplete(autocompleter)

        append(ListView().also {
            it += TextView(label)
            it += i
        })
        return i
    }

    /**
     * Create a new dropdown field in this group.
     *
     * @param label The name of the field.
     * @param property The property to read and write the field value.
     * @param transform Transform lambda to get the displayable name for each entry.
     */
    fun <T : Any> select(label: String, values: List<T>, property: Property<T>, transform: (T) -> String): SelectView<T> {
        val i = SelectView(values, property, transform)

        listView {
            textView(label)
            append(i)
        }
        return i
    }

    /**
     * Create a new button in this group.
     *
     * @param label The name of the button.
     * @param action The action when the button is clicked.
     */
    fun button(label: String, action: () -> Unit = {}): Button {
        val b = Button()
        b.text = label
        b.onClick { action() }

        append(ListView().also {
            it += b
        })
        return b
    }

    /**
     * Create a new checkbox in this group.
     *
     * @param label The name of the field.
     * @param property The property to read (and write) the field value. If the property is read only, the field will be read only too.
     * @param type The visual style of this checkbox
     */
    fun checkBox(label: String, property: ReadOnlyProperty<Boolean>, type: CheckBox.Type): CheckBox {
        return CheckBox(label, property).also(this::append).also {
            it.type = type
        }
    }

    /**
     * Create a custom entry in this group.
     *
     * @param init A lambda to init the custom view.
     */
    fun custom(init: ListView.() -> Unit): ListView {
        val view = ListView()
        append(view)
        init(view)
        return view
    }

    /**
     * Set the collapsed state. Defaults to collapsed.
     *
     * @param state Optionally set the collapsed state.
     */
    fun collapse(state: Boolean = true) {
        collapsed = state
    }

    private val collapseView = IconView().also {
        it.icon = MaterialIcon.EXPAND_LESS
        it.classes += "collapse-icon"
    }

    /**
     * Remove the content of this group.
     */
    fun clearContent() {
        children.drop(1).forEach(this::remove)
    }

    init {
        val nameView = TextView()
        nameView.classes += "group-header"
        nameView.text = name

        append(ListView().also {
            it += nameView
            it += collapseView

            it.onClick { event ->
                collapsed = !collapsed
                event.stopPropagation()
            }
        })

        collapsedProperty.onChange {
            if (collapsed) {
                collapseView.classes += "rotate-180"
                //collapseView.icon = MaterialIcon.EXPAND_MORE
            } else {
                collapseView.classes -= "rotate-180"
                //collapseView.icon = MaterialIcon.EXPAND_LESS
            }
        }
    }
}