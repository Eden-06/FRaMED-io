package io.framed.view

import org.w3c.dom.HTMLDivElement

/**
 * @author lars
 */
class Sidebar() : ViewCollection<View<*>, HTMLDivElement>("div") {

    var application: Application? = null

    fun setup(vararg initiator: View<*>, init: Sidebar.() -> Unit) {
        clear()
        initiator.forEach {
            it.onClick {
                it.stopPropagation()
                display()
            }

            (it as? InputView)?.let {
                it.onFocusEnter {
                    display()
                }
            }
        }
        init()
    }

    fun display() {
        application?.let { a ->
            a.propertyBar.clear()
            a.propertyBar += this
        }
    }

    fun input(label: String, default: String = "", onchange: (String) -> Unit = {}): InputView {
        val i = InputView()
        i.value = default
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