package de.westermann.kobserve.set

import de.westermann.kobserve.event.EventHandler

class FilteredSet<T>(
    val parent: ObservableReadOnlySet<T>,
    predicate: (T) -> Boolean
) : ObservableReadOnlySet<T> {

    var predicate: (T) -> Boolean = predicate
        set(value) {
            if (value != field) {
                field = value
                onChange.emit(Unit)
            }
        }

    override val onAdd = EventHandler<T>()
    override val onUpdate = EventHandler<T>()
    override val onRemove = EventHandler<T>()
    override val onChange = EventHandler(parent.onChange)

    override val size: Int
        get() = parent.size

    override fun contains(element: T): Boolean {
        if (!predicate(element)) return false

        for (e in this) {
            if (element == e) {
                return true
            }
        }

        return false
    }

    override fun containsAll(elements: Collection<T>): Boolean {
        return elements.all(this::contains)
    }

    override fun isEmpty() = parent.all(predicate)

    override fun notifyDatasetChanged() {
        parent.notifyDatasetChanged()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as FilteredSet<*>

        if (parent != other.parent) return false
        if (predicate != other.predicate) return false

        return true
    }

    override fun hashCode(): Int {
        var result = parent.hashCode()
        result = 31 * result + predicate.hashCode()
        return result
    }

    override fun iterator(): Iterator<T> {
        return SetIterator()
    }

    init {
        parent.onAdd { element ->
            if (predicate(element)) {
                onAdd.emit(element)
            }
        }

        parent.onUpdate { element ->
            onUpdate.emit(element)
        }

        parent.onRemove { element ->
            onRemove.emit(element)
        }
    }

    private inner class SetIterator : Iterator<T> {

        private val iterator = parent.iterator()

        private var nextElement: T? = null
        private var nextUsed = true

        override fun hasNext(): Boolean {
            return if (nextUsed) {
                while (iterator.hasNext()) {
                    val next = iterator.next()
                    if (predicate(next)) {
                        nextElement = next
                        nextUsed = false
                        return true
                    }
                }
                false
            } else {
                true
            }
        }

        override fun next(): T {
            check(hasNext())
            nextUsed = true
            @Suppress("UNCHECKED_CAST")
            return nextElement as T
        }
    }
}

fun <T> ObservableReadOnlySet<T>.filterObservable(predicate: (T) -> Boolean): ObservableReadOnlySet<T> =
    FilteredSet(this, predicate)
