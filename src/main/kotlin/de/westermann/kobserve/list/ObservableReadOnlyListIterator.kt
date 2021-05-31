package de.westermann.kobserve.list

open class ObservableReadOnlyListIterator<T>(
    protected open val list: ObservableReadOnlyList<T>,
    protected var index: Int = 0
) : ListIterator<T> {

    protected var last = -1

    override fun hasNext(): Boolean = index < list.size

    override fun next(): T {
        if (!hasNext()) throw NoSuchElementException()
        last = index++
        return list[last]
    }

    override fun hasPrevious(): Boolean = index > 0

    override fun nextIndex(): Int = index

    override fun previous(): T {
        if (!hasPrevious()) throw NoSuchElementException()

        last = --index
        return list[last]
    }

    override fun previousIndex(): Int = index - 1
}