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
    override fun canApply(item: HistoryItem): Boolean {
        return item is HistoryProperty<*> && item.property == property
    }

    @Suppress("UNCHECKED_CAST")
    override fun apply(item: HistoryItem) {
        if (canApply(item)) {
            newValue = (item as HistoryProperty<T>).newValue
        }
    }
}