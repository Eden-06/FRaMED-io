package de.westermann.kobserve.list

import de.westermann.kobserve.event.EventHandler

abstract class RelationalList<T>(
    protected val parent: ObservableReadOnlyList<T>
) : ObservableReadOnlyList<T> {

    override val onAdd = EventHandler<ListAddEvent<T>>()
    override val onUpdate = EventHandler<ListUpdateEvent<T>>()
    override val onRemove = EventHandler<ListRemoveEvent>()

    override val onChange = EventHandler<Unit>()

    protected val relation: MutableList<Relation> = mutableListOf()

    abstract fun updateRelation()

    override fun invalidate() {
        val oldRelation = relation.toMutableList()

        updateRelation()

        val newRelation = relation.toList()

        val hasListChanged = oldRelation != newRelation

        while (true) {
            val firstToRemove = oldRelation.indexOfFirst { it !in newRelation }

            if (firstToRemove >= 0) {
                onRemove.emit(ListRemoveEvent(firstToRemove))
                oldRelation.removeAt(firstToRemove)
                continue
            }

            break
        }

        for ((newIndex, value) in newRelation.withIndex()) {
            val oldIndex = oldRelation.indexOf(value)

            if (oldIndex == newIndex) {
                continue
            }

            if (oldIndex < 0) {
                onAdd.emit(ListAddEvent(newIndex, get(newIndex)))
            } else {
                onUpdate.emit(ListUpdateEvent(oldIndex, newIndex, get(newIndex)))
                oldRelation.removeAt(oldIndex)
            }
            oldRelation.add(newIndex, value)
        }

        if (hasListChanged) {
            onChange.emit(Unit)
        }
    }

    override val size: Int
        get() = relation.size

    override fun contains(element: T): Boolean {
        for (elem in iterator()) {
            if (elem == element) {
                return true
            }
        }
        return false
    }

    override fun containsAll(elements: Collection<T>): Boolean {
        var notFound = elements.toList()

        for (elem in iterator()) {
            notFound -= elem

            if (notFound.isEmpty()) {
                return true
            }
        }
        return false
    }

    override fun get(index: Int): T {
        return parent[relation[index].index]
    }

    override fun indexOf(element: T): Int {
        var index = 0
        for (elem in iterator()) {
            if (elem == element) {
                return index
            }
            index += 1
        }
        return -1
    }

    override fun isEmpty(): Boolean = relation.isEmpty()

    override fun lastIndexOf(element: T): Int {
        var index = 0
        var lastIndex = -1
        for (elem in iterator()) {
            if (elem == element) {
                lastIndex = index
            }
            index += 1
        }
        return lastIndex
    }

    override fun notifyItemChanged(index: Int) {
        parent.notifyItemChanged(index)
    }

    override fun notifyDatasetChanged() {
        parent.notifyDatasetChanged()
    }

    init {
        parent.onAdd {
            invalidate()
        }
        parent.onUpdate {
            invalidate()
        }
        parent.onRemove {
            invalidate()
        }
    }

    override fun toString(): String = joinToString(", ", "[", "]")

    data class Relation(val index: Int, val hash: Int)
}
