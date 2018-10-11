package io.framed.framework.util

class HistoryGroup(
        private val items: List<HistoryItem>
) : HistoryItem {
    override fun undo() {
        items.reversed().forEach { it.undo() }
    }

    override fun redo() {
        items.forEach { it.redo() }
    }
}