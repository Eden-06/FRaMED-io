package de.westermann.kobserve.list

import de.westermann.kobserve.ReadOnlyProperty
import de.westermann.kobserve.property.property

class FilteredList<T>(
    parent: ObservableReadOnlyList<T>,
    predicate: (T) -> Boolean
) : RelationalList<T>(parent) {

    val predicateProperty = property(predicate)
    var predicate by predicateProperty

    override fun updateRelation() {
        relation.clear()
        for (index in 0 until parent.size) {
            val element = parent[index]
            if (predicate(parent[index])) {
                relation += Relation(index, element.hashCode())
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as FilteredList<*>

        if (parent != other.parent) return false
        if (predicate != other.predicate) return false

        return true
    }

    override fun hashCode(): Int {
        var result = parent.hashCode()
        result = 31 * result + predicate.hashCode()
        return result
    }

    init {
        invalidate()

        predicateProperty.onChange {
            invalidate()
        }
    }
}

fun <T> ObservableReadOnlyList<T>.filterObservable(predicate: (T) -> Boolean): FilteredList<T> =
    FilteredList(this, predicate)

fun <T> ObservableReadOnlyList<T>.filterObservable(predicateProperty: ReadOnlyProperty<(T) -> Boolean>): FilteredList<T> =
    FilteredList(this, predicateProperty.value).also {
        it.predicateProperty.bind(predicateProperty)
    }

fun <T, F> ObservableReadOnlyList<T>.filterObservable(
    filterProperty: ReadOnlyProperty<F>,
    predicate: (element: T, filter: F) -> Boolean
): FilteredList<T> = FilteredList(this) { predicate(it, filterProperty.value) }.also { list ->
    filterProperty.onChange { list.invalidate() }
}

