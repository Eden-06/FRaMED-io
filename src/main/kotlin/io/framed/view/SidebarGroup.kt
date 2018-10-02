package io.framed.view

import io.framed.util.Property
import org.w3c.dom.HTMLDivElement

class SidebarGroup(
        val name: String
) : ViewCollection<View<*>, HTMLDivElement>("div") {

    var collapsed by ClassDelegate()

    fun input(label: String, property: Property<String>, autocomplete: List<String> = emptyList()): InputView {
        val i = InputView(property)
        i.autocomplete = autocomplete

        append(ListView().also {
            it += TextView(label)
            it += i
        })
        return i
    }

    fun <T : Any> select(label: String, values: List<T>, selected: T, onchange: (T) -> Unit = {}): SelectView<T> {
        val i = SelectView(values, selected)
        i.onChange(onchange)

        append(ListView().also {
            it += TextView(label)
            it += i
        })
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