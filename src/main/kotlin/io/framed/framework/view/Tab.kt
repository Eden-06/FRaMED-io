package io.framed.framework.view

import io.framed.framework.util.EventHandler
import io.framed.framework.util.Property
import org.w3c.dom.HTMLDivElement

class Tab(
        name: Property<String>
) : ViewCollection<View<*>, HTMLDivElement>("div") {

    fun close() {
        onClose.fire(Unit)
    }

    fun open() {
        if (!selected) {
            selected = true
            onOpen.fire(Unit)
        }
    }

    private val closeView: IconView

    var closable
        get() = closeView.visible
        set(value) {
            closeView.visible = value
        }

    var selected by ClassDelegate()

    val onClose = EventHandler<Unit>()
    val onOpen = EventHandler<Unit>()

    init {
        textView(name)
        closeView = iconView(MaterialIcon.CLEAR) {
            onClick {
                it.stopPropagation()
                onClose.fire(Unit)
            }
        }

        onClick {
            it.stopPropagation()
            onOpen.fire(Unit)
        }
    }
}
