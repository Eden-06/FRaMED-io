package io.framed.framework.view

import de.westermann.kobserve.EventHandler
import de.westermann.kobserve.Property
import org.w3c.dom.HTMLDivElement

class Tab(
        name: Property<String>
) : ViewCollection<View<*>, HTMLDivElement>("div") {

    fun close() {
        onClose.emit(Unit)
    }

    fun open() {
        if (!selected) {
            selected = true
            onOpen.emit(Unit)
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
                onClose.emit(Unit)
            }
        }

        onClick {
            it.stopPropagation()
            onOpen.emit(Unit)
        }
    }
}
