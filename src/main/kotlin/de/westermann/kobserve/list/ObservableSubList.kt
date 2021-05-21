package de.westermann.kobserve.list

class ObservableSubList<T>(
    override val parent: ObservableList<T>,
    range: IntRange
) : ObservableReadOnlySubList<T>(parent, range), ObservableList<T> {

    override fun add(element: T): Boolean {
        parent.add(range.first + range.endInclusive + 1, element)
        range = range.start..range.endInclusive + 1
        return true
    }

    override fun add(index: Int, element: T) {
        if (index !in 0 until size) {
            throw IndexOutOfBoundsException()
        }

        parent.add(range.first + index, element)
        range = range.start..range.endInclusive + 1
    }

    override fun addAll(index: Int, elements: Collection<T>): Boolean {
        if (index !in 0 until size) {
            throw IndexOutOfBoundsException()
        }

        val wasAdded = parent.addAll(range.first + index, elements)
        if (wasAdded) {
            range = range.start..range.endInclusive + elements.size
        }
        return wasAdded
    }

    override fun addAll(elements: Collection<T>): Boolean {
        val wasAdded = parent.addAll(range.first + range.endInclusive + 1, elements)
        if (wasAdded) {
            range = range.start..range.endInclusive + elements.size
        }
        return wasAdded
    }

    override fun clear() {
        for (i in 0 until size) {
            parent.removeAt(range.first)
        }

        range = IntRange.EMPTY
    }

    override fun remove(element: T): Boolean {
        return parent.remove(element)
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        return parent.removeAll(elements)
    }

    override fun removeAt(index: Int): T {
        if (index !in 0 until size) {
            throw IndexOutOfBoundsException()
        }

        return parent.removeAt(range.first + index)
    }

    override fun retainAll(elements: Collection<T>): Boolean {
        return parent.retainAll(elements)
    }

    override fun set(index: Int, element: T): T {
        if (index !in 0 until size) {
            throw IndexOutOfBoundsException()
        }

        return parent.set(range.first + index, element)
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as ObservableSubList<*>

        if (range != other.range) return false
        if (parent != other.parent) return false

        return true
    }

    override fun hashCode(): Int {
        var result = range.hashCode()
        result = 31 * result + parent.hashCode()
        return result
    }

    override fun toString(): String =joinToString(", ", "[", "]")
}
