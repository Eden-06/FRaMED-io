package io.framed.framework.view

import de.westermann.kobserve.Property
import de.westermann.kobserve.event.EventHandler
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
        get() = closeView.display
        set(value) {
            closeView.display = value
        }

    val selectedProperty by ClassDelegate()
    var selected by selectedProperty

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
