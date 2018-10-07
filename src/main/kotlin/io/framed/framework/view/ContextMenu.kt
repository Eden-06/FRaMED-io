package io.framed.framework.view

import io.framed.framework.pictogram.ContextEvent
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.events.KeyboardEvent
import kotlin.browser.window
import kotlin.math.min

/**
 * Represents a onContext menu with optional header.
 *
 * @author lars
 */
class ContextMenu : View<HTMLDivElement>("div") {
    private val listView = ListView()

    private val titleView = TextView().also {
        it.visible = false
    }

    lateinit var lastEvent: ContextEvent
    fun getLastEvent(): ContextEvent = lastEvent

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
     * Add an item to the onContext menu.
     *
     * @param icon Icon for the menu entry. `null` means no icon.
     * @param name Name for the menu entry.
     * @param callback Click callback is called when entry is selected.
     */
    fun addItem(icon: Icon?, name: String, callback: (ContextEvent) -> Unit): ListView {
        val l = ListView()
        l += IconView(icon)
        l += TextView(name)
        l.onClick {
            callback(getLastEvent())
        }
        listView += l
        return l
    }

    val isEmpty: Boolean
        get() = listView.isEmpty

    val keyListener = { event: KeyboardEvent ->
        when (event.keyCode) {
            27 -> close()
        }
    }

    /**
     * Open the context menu at the given position, relative to the body.
     */
    fun open(event: ContextEvent) {
        lastEvent = event
        Root += this

        val left = min(window.innerWidth - listView.clientWidth - 8.0, event.position.x)
        val top = min(window.innerHeight - listView.clientHeight - 8.0, event.position.y)

        listView.left = left
        listView.top = top

        Root.onKeyDown += keyListener
    }

    /**
     * Close the onContext menu.
     */
    fun close() {
        Root -= this
        Root.onKeyDown -= keyListener
    }

    init {
        listView += titleView

        html.appendChild(listView.html)

        onClick {
            it.stopPropagation()
            close()
        }
        onContext {
            it.stopPropagation()
            close()
        }
    }
}

/**
 * Create a new context menu.
 *
 * @param init Builder for the new context menu.
 *
 * @return The new context menu.
 */
fun contextMenu(init: ContextMenu.() -> Unit): ContextMenu = ContextMenu().also(init)
