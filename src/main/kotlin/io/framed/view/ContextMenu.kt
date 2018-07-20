package io.framed.view

import org.w3c.dom.HTMLDivElement
import kotlin.browser.document

/**
 * @author lars
 */
class ContextMenu : View<HTMLDivElement>("div") {
    private val listView = ListView()

    private val titleView = TextView().also {
        it.visible = false
    }
    var title: String
        get() = titleView.text
        set(value) {
            titleView.text = value
            titleView.visible = value.isNotBlank()
        }

    fun addItem(icon: Icon?, name: String, callback: () -> Unit) {
        val l = ListView()
        l += IconView(icon)
        l += TextView(name)
        l.click.on {
            callback()
        }
        listView += l
    }

    fun open(x: Double, y: Double) {
        document.body?.appendChild(html)

        listView.left = x
        listView.top = y
    }

    fun close() {
        document.body?.removeChild(html)
    }

    init {
        listView += titleView

        html.appendChild(listView.html)

        click.on {
            it.stopPropagation()
            close()
        }
        context.on {
            it.stopPropagation()
            close()
        }
    }
}

fun contextMenu(init: ContextMenu.() -> Unit): ContextMenu {
    val cm = ContextMenu()
    init(cm)
    return cm
}