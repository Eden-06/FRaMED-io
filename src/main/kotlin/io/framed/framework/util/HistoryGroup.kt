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
}