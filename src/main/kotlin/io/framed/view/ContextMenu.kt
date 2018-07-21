package io.framed.view

import org.w3c.dom.HTMLDivElement
import kotlin.browser.document

/**
 * Represents a context menu with optional header.
 *
 * @author lars
 */
class ContextMenu : View<HTMLDivElement>("div") {
    private val listView = ListView()

    private val titleView = TextView().also {
        it.visible = false
    }

    /**
     * Header content of the content menu. Blank title will remove the header.
     */
    var title: String
        get() = titleView.text
        set(value) {
            titleView.text = value
            titleView.visible = value.isNotBlank()
        }

    /**
     * Add an item to the context menu.
     *
     * @param icon Icon for the menu entry. `null` means no icon.
     * @param name Name for the menu entry.
     * @param callback Click callback is called when entry is selected.
     */
    fun addItem(icon: Icon?, name: String, callback: () -> Unit) {
        val l = ListView()
        l += IconView(icon)
        l += TextView(name)
        l.click.on {
            callback()
        }
        listView += l
    }

    /**
     * Open the context menu at the given position, relative to the body.
     *
     * @param x Left position in px.
     * @param y Top position in px.
     */
    fun open(x: Double, y: Double) {
        document.body?.appendChild(html)

        listView.left = x
        listView.top = y
    }

    /**
     * Close the context menu.
     */
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

/**
 * Create a new context menu.
 *
 * @param init Build for the new context menu.
 *
 * @return The new context menu.
 */
fun contextMenu(init: ContextMenu.() -> Unit): ContextMenu {
    val cm = ContextMenu()
    init(cm)
    return cm
}