package io.framed.view

import io.framed.util.Property
import org.w3c.dom.HTMLDivElement

/**
 * @author lars
 */
class Sidebar(
        val application: Application
) : ViewCollection<View<*>, HTMLDivElement>("div") {

    fun setup(vararg initiator: View<*>, init: Sidebar.() -> Unit) {
        clear()
        initiator.forEach { view ->
            view.onClick {
                it.stopPropagation()
                display()
            }

            (view as? InputView)?.let { input ->
                input.onFocusEnter {
                    display()
                }
            }
        }
        init()
    }

    fun display() {
        application.let { a ->
            a.propertyBar.clear()
            a.propertyBar += this
        }
    }

    fun input(label: String, property: Property<String>): InputView {
        val i = InputView(property)

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

    fun title(text: String): TextView {
        val view = TextView().also {
            it.classes += "header"
        }
        view.text = text
        append(ListView().also {
            it += view
        })
        return view
    }
}