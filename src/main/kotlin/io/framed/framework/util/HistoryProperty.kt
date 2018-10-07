package io.framed.framework.util

class HistoryProperty<T:Any>(
        private val property: Property<T>,
        private val oldValue: T,
        private val newValue: T
): HistoryItem {
    override fun undo() {
        property.set(oldValue)
    }

    override fun redo() {
        property.set(newValue)
    }
}