package io.framed.framework.util

import de.westermann.kobserve.Property

class HistoryProperty<T : Any>(
        private val property: Property<T>,
        private val oldValue: T,
        private var newValue: T
) : HistoryItem {

    override val description: String = "Change property"

    override fun undo() {
        property.set(oldValue)
    }

    override fun redo() {
        property.set(newValue)
    }

    @Suppress("UNCHECKED_CAST")
    override fun shouldAdd(item: HistoryItem): Boolean {
        if (item is HistoryProperty<*> && item.property == property) {
            newValue = item.newValue as T
            return false
        }
        return true
    }
}