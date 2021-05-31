package de.westermann.kobserve.set

import de.westermann.kobserve.ReadOnlyProperty
import de.westermann.kobserve.event.EventHandler

interface ObservableReadOnlySet<T>: Set<T>, ReadOnlyProperty<ObservableReadOnlySet<T>> {

    val onAdd: EventHandler<T>
    val onUpdate: EventHandler<T>
    val onRemove: EventHandler<T>

    fun notifyItemChanged(element: T) {
        onUpdate.emit(element)
    }

    fun notifyDatasetChanged() {
        for (element in iterator()) {
            onUpdate.emit(element)
        }
    }

    override fun get(): ObservableReadOnlySet<T> {
        return this
    }
}