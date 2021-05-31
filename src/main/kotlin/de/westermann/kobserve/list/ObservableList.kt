package de.westermann.kobserve.list

interface ObservableList<T> : ObservableReadOnlyList<T>, MutableList<T> {
    override fun subList(fromIndex: Int, toIndex: Int): ObservableList<T> {
        return ObservableSubList(this, fromIndex until toIndex)
    }

    override fun iterator(): MutableIterator<T> {
        return ObservableListIterator(this)
    }

    override fun listIterator(): MutableListIterator<T> {
        return ObservableListIterator(this)
    }

    override fun listIterator(index: Int): MutableListIterator<T> {
        return ObservableListIterator(this, index)
    }
}
