package io.framed.framework.util

class HistoryGroup(
        private val items: List<HistoryItem>,
        override val description: String
) : HistoryItem {
    override fun undo() {
        items.reversed().forEach {
            it.undo()
        }
    }

    override fun redo() {
        items.forEach {
            it.redo()
        }
    }

    override fun canApply(item: HistoryItem): Boolean {
        if (item is HistoryGroup) {
            if (items.size == item.items.size) {
                return items.mapIndexed { index, historyItem -> historyItem.canApply(item.items[index]) }.all { it }
            }
        } else if (items.size == 1) {
            return items.first().canApply(item)
        }
        return false
    }

    override fun apply(item: HistoryItem) {
        if (item is HistoryGroup) {
            if (items.size == item.items.size) {
                if (items.mapIndexed { index, historyItem -> historyItem.canApply(item.items[index]) }.all { it }) {
                    items.forEachIndexed { index, historyItem -> historyItem.apply(item.items[index]) }
                }
            }
        } else if (items.size == 1) {
            if (items.first().canApply(item)) {
                items.first().apply(item)
            }
        }
    }
}