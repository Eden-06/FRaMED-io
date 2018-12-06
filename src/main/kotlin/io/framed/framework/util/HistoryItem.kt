package io.framed.framework.util


interface HistoryItem {
    val description: String

    fun undo()

    fun redo()

    fun canApply(item: HistoryItem): Boolean = false
    fun apply(item: HistoryItem) {}
}