package io.framed.framework.view

import io.framed.framework.util.async
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.events.KeyboardEvent
import kotlin.math.min

/**
 * @author lars
 */
class Dialog : View<HTMLDivElement>("div") {
    private val listView = ListView()

    private val titleView = TextView()
    val contentView = ListView()
    private val buttonView = ListView()

    /**
     * Header content of the content menu. Blank title will remove the header.
     */
    var title: String
        get() = titleView.text
        set(value) {
            titleView.text = value
            titleView.visible = value.isNotBlank()
        }

    var primaryListener: (() -> Unit)? = null

    fun addButton(name: String, primary: Boolean = false, onClick: () -> Unit = {}): Button =
            buttonView.button {
                text = name
                onClick {
                    close()
                    onClick()
                    it.stopPropagation()
                }

                if (primary) {
                    this.primary = true
                    primaryListener = onClick
                }
            }.also(buttonView::toBackground)

    var closable: Boolean = false

    private var k: (KeyboardEvent) -> Unit = {}

    /**
     * Open the dialog.
     */
    fun open() {
        listView.hiddenVisibility = true
        Root += this

        async {
            val w = min(listView.clientWidth, clientWidth / 2)
            listView.width = w.toDouble()
            listView.left = (clientWidth - w) / 2.0
            async {
                listView.top = (clientHeight - listView.clientHeight) / 2.0
                async {
                    listView.hiddenVisibility = false
                }
            }
        }

        k = Root.onKeyUp.addListener {
            when (it.keyCode) {
                27 -> if (closable) {
                    close()
                }
                13 -> primaryListener?.let {
                    close()
                    it()
                }
            }
        }!!
    }

    /**
     * Close the dialog.
     */
    fun close() {
        Root -= this
        Root.onKeyUp.removeListener(k)
    }

    init {
        html.appendChild(listView.html)
        listView.classes += "dialog-window"

        titleView.classes += "dialog-title"
        listView += titleView

        contentView.classes += "dialog-content"
        listView += contentView

        buttonView.classes += "dialog-button"
        listView += buttonView

        onClick {
            if (closable) {
                close()
            }
        }

        listView.onClick { it.stopPropagation() }
    }
}

/**
 * Create a new dialog.
 *
 * @param init Builder for the new dialog.
 *
 * @return The new dialog.
 */
fun dialog(init: Dialog.() -> Unit): Dialog {
    val cm = Dialog()
    init(cm)
    return cm
}