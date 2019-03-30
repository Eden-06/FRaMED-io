package io.framed.framework.util

import de.westermann.kobserve.Property

class HistoryGroupingProperty<T>(
        private val grouping: Any,
        property: Property<T>,
        oldValue: T,
        newValue: T
) : HistoryItem {

    val propertyMap = mutableMapOf<Property<T>, Pair<T,T>>()

    override val description: String = "Change property"

    override fun undo() {
        for ((property, data) in propertyMap) {
            property.set(data.first)
        }
    }

    override fun redo() {
        for ((property, data) in propertyMap) {
            property.set(data.second)
        }
    }

    init {
        propertyMap[property] = oldValue to newValue
    }

    @Suppress("UNCHECKED_CAST")
    override fun canApply(item: HistoryItem): Boolean {
        return item is HistoryGroupingProperty<*> && item.grouping == grouping
    }

    @Suppress("UNCHECKED_CAST")
    override fun apply(item: HistoryItem) {
        if (canApply(item) && item is HistoryGroupingProperty<*>) {
            for (entry in item.propertyMap) {
                val property = entry.key as Property<T>
                val data = entry.value as Pair<T, T>

                if (property in propertyMap) {
                    propertyMap[property] = propertyMap[property]!!.first to data.second
                } else {
                    propertyMap[property] = data
                }
            }
        }
    }
}