package de.westermann.kobserve.set

import de.westermann.kobserve.event.EventHandler

class TransformSet<P, T>(
    private val parent: ObservableReadOnlySet<P>,
    private val transform: (P) -> T
) : ObservableReadOnlySet<T> {

    override val onAdd = EventHandler<T>()
    override val onUpdate = EventHandler<T>()
    override val onRemove = EventHandler<T>()
    override val onChange = EventHandler(parent.onChange)

    override val size: Int
        get() = parent.size

    override fun contains(element: T): Boolean {
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

    override fun isEmpty(): Boolean = parent.isEmpty()

    override fun notifyDatasetChanged() {
        parent.notifyDatasetChanged()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as TransformSet<*, *>

        if (parent != other.parent) return false
        if (transform != other.transform) return false

        return true
    }

    override fun hashCode(): Int {
        var result = parent.hashCode()
        result = 31 * result + transform.hashCode()
        return result
    }

    override fun iterator(): Iterator<T> {
        return SetIterator()
    }

    init {
        parent.onAdd { element ->
            onAdd.emit(transform(element))
        }

        parent.onUpdate { element ->
            onUpdate.emit(transform(element))
        }

        parent.onRemove { element ->
            onRemove.emit(transform(element))
        }
    }

    private inner class SetIterator : Iterator<T> {

        private val iterator = parent.iterator()

        override fun hasNext(): Boolean = iterator.hasNext()

        override fun next(): T {
            check(hasNext())
            return transform(iterator.next())
        }
    }
}

fun <P, T> ObservableReadOnlySet<P>.mapObservable(transform: (P) -> T): ObservableReadOnlySet<T> =
    TransformSet(this, transform)
