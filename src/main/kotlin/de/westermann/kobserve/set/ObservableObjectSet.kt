package de.westermann.kobserve.set

import de.westermann.kobserve.event.EventHandler

class ObservableObjectSet<T>(
    private val set: MutableSet<T>
) : ObservableSet<T> {

    override val onAdd = EventHandler<T>()
    override val onUpdate = EventHandler<T>()
    override val onRemove = EventHandler<T>()

    override val onChange = EventHandler<Unit>()

    private fun emitOnAdd(element: T) {
        onAdd.emit(element)
        onChange.emit(Unit)
    }

    private fun emitOnUpdate(element: T) {
        onUpdate.emit(element)
        onChange.emit(Unit)
    }

    private fun emitOnRemove(element: T) {
        onRemove.emit(element)
        onChange.emit(Unit)
    }

    override val size: Int
        get() = set.size

    override fun add(element: T): Boolean {
        val isAdded = set.add(element)
        if (isAdded) {
            emitOnAdd(element)
        }
        return isAdded
    }

    override fun addAll(elements: Collection<T>): Boolean {
        var isChanged = false

        for (element in elements) {
            if (add(element)) {
                isChanged = true
            }
        }

        return isChanged
    }

    override fun clear() {
        while (isNotEmpty()) {
            remove(first())
        }
    }

    override fun contains(element: T): Boolean {
        return set.contains(element)
    }

    override fun containsAll(elements: Collection<T>): Boolean {
        return set.containsAll(elements)
    }

    override fun isEmpty(): Boolean {
        return set.isEmpty()
    }

    override fun remove(element: T): Boolean {
        val isRemoved = set.remove(element)
        if (isRemoved) {
            emitOnRemove(element)
        }
        return isRemoved
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        var isChanged = false

        for (element in elements) {
            if (remove(element)) {
                isChanged = true
            }
        }

        return isChanged
    }

    override fun retainAll(elements: Collection<T>): Boolean {
        var isChanged = false

        val iterator = set.iterator()
        while (iterator.hasNext()) {
            val element = iterator.next()
            if (element !in elements) {
                iterator.remove()
                emitOnRemove(element)
                isChanged = true
            }
        }

        return isChanged
    }

    override fun toString(): String {
        return set.toString()
    }

    override fun notifyItemChanged(element: T) {
        emitOnUpdate(element)
    }

    override fun iterator(): MutableIterator<T> {
        return Iterator()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as ObservableObjectSet<*>

        if (set != other.set) return false

        return true
    }

    override fun hashCode(): Int {
        return set.hashCode()
    }

    private inner class Iterator : MutableIterator<T> {
        private val iterator = set.iterator()

        private var firstElement = false
        private var lastElement: T? = null

        override fun hasNext(): Boolean = iterator.hasNext()

        override fun next(): T {
            check(hasNext())
            val element = iterator.next()
            firstElement = true
            lastElement = element
            return element
        }

        override fun remove() {
            check(firstElement)
            iterator.remove()
            @Suppress("UNCHECKED_CAST")
            emitOnRemove(lastElement as T)
        }

    }
}

fun <T> setProperty(set: MutableSet<T>): ObservableSet<T> = ObservableObjectSet(set)
fun <T> MutableSet<T>.observe(): ObservableSet<T> = ObservableObjectSet(this)
fun <T> observableSetOf(vararg elements: T): ObservableSet<T> =
    ObservableObjectSet(mutableSetOf(*elements))
