package io.framed.view

import io.framed.async
import org.w3c.dom.HTMLDivElement
import kotlin.browser.document
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

    fun addButton(name: String, primary: Boolean = false, onClick: () -> Unit = {}): TextView =
            buttonView.textView(name) {
                click {
                    close()
                    onClick()
                    it.stopPropagation()
                }

                if (primary) {
                    classes += "primary"
                }
            }

    var closable: Boolean = false

    /**
     * Open the dialog.
     */
    fun open() {
        listView.hiddenVisibility = true
        document.body?.appendChild(html)

        async {
            val w = min(listView.clientWidth, clientWidth / 2)
            val changed = w != listView.clientWidth
            listView.width = w.toDouble()
            listView.left = (clientWidth - w) / 2.0
            async {
                listView.top = (clientHeight - listView.clientHeight) / 2.0
                async {
                    listView.hiddenVisibility = false
                }
            }
        }
    }

    /**
     * Close the dialog.
     */
    fun close() {
        document.body?.removeChild(html)
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

        click {
            if (closable) {
                close()
            }
        }
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