package de.westermann.kobserve.list

class ObservableListIterator<T>(
    override val list: ObservableList<T>,
    index: Int = 0
) : ObservableReadOnlyListIterator<T>(list, index), MutableListIterator<T> {

    override fun remove() {
        check(last != -1) { "Call next() or previous() before removing element from the iterator." }

        list.removeAt(last)
        index = last
        last = -1
    }

    override fun add(element: T) {
        list.add(index, element)
        index++
        last = -1
    }

    override fun set(element: T) {
        check(last != -1) { "Call next() or previous() before updating element value with the iterator." }
        list[last] = element
    }

}