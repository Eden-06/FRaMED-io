package de.westermann.kobserve.list

import de.westermann.kobserve.ReadOnlyProperty
import de.westermann.kobserve.event.EventHandler

interface ObservableReadOnlyList<T> : List<T>, ReadOnlyProperty<ObservableReadOnlyList<T>> {
    val onAdd: EventHandler<ListAddEvent<T>>
    val onUpdate: EventHandler<ListUpdateEvent<T>>
    val onRemove: EventHandler<ListRemoveEvent>

    fun notifyItemChanged(index: Int) {
        onUpdate.emit(ListUpdateEvent(index, index, get(index)))
    }

    fun notifyDatasetChanged() {
        for ((i,e) in withIndex()) {
            onUpdate.emit(ListUpdateEvent(i, i, e))
        }
    }

    override fun subList(fromIndex: Int, toIndex: Int): ObservableReadOnlyList<T> {
        return ObservableReadOnlySubList(this, fromIndex until toIndex)
    }

    override fun iterator(): Iterator<T> {
        return ObservableReadOnlyListIterator(this)
    }

    override fun listIterator(): ListIterator<T> {
        return ObservableReadOnlyListIterator(this)
    }

    override fun listIterator(index: Int): ListIterator<T> {
        return ObservableReadOnlyListIterator(this, index)
    }

    override fun get(): ObservableReadOnlyList<T> {
        return this
    }
}

data class ListAddEvent<T>(
    val index: Int,
    val element: T
)

data class ListUpdateEvent<T>(
    val oldIndex: Int,
    val newIndex: Int,
    val element: T
)

data class ListRemoveEvent(
    val index: Int
)
