package io.framed.framework.view

import de.westermann.kobserve.Property
import de.westermann.kobserve.ReadOnlyProperty
import org.w3c.dom.HTMLDivElement

class SidebarGroup(
        val name: String
) : ViewCollection<View<*>, HTMLDivElement>("div") {

    val collapsedProperty by ClassDelegate()
    var collapsed by collapsedProperty

    fun input(label: String, property: ReadOnlyProperty<String>, autocomplete: List<String> = emptyList()): InputView {
        val i = InputView(property)
        i.autocomplete(autocomplete)

        append(ListView().also {
            it += TextView(label)
            it += i
        })
        return i
    }
    fun input(label: String, property: ReadOnlyProperty<String>, autocompleter: (patial: String) -> List<String>): InputView {
        val i = InputView(property)
        i.autocomplete(autocompleter)

        append(ListView().also {
            it += TextView(label)
            it += i
        })
        return i
    }

    fun <T : Any> select(label: String, values: List<T>, property: Property<T>, transform: (T) -> String): SelectView<T> {
        val i = SelectView(values, property, transform)

        listView {
            textView(label)
            append(i)
        }
        return i
    }

    fun button(label: String, onClick: () -> Unit = {}): Button {
        val b = Button()
        b.text = label
        b.onClick { onClick() }

        append(ListView().also {
            it += b
        })
        return b
    }

    fun checkBox(label: String, property: Property<Boolean>, type: CheckBox.Type): CheckBox {
        return CheckBox(label, property).also(this::append).also {
            it.type = type
        }
    }

    fun custom(init: ListView.() -> Unit): ListView {
        val view = ListView()
        append(view)
        init(view)
        return view
    }


    fun collapse(state: Boolean = true) {
        collapsed = state
        if (state) {
            collapseView.classes += "rotate-180"
            //collapseView.icon = MaterialIcon.EXPAND_MORE
        } else {
            collapseView.classes -= "rotate-180"
            //collapseView.icon = MaterialIcon.EXPAND_LESS
        }
    }

    private val collapseView = IconView().also {
        it.icon = MaterialIcon.EXPAND_LESS
        it.classes += "collapse-icon"
    }

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
                collapse(!collapsed)
                event.stopPropagation()
            }
        })
    }
}