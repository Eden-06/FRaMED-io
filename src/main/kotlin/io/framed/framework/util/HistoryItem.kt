package io.framed.framework.util


interface HistoryItem {
    val description: String

    fun undo()

    fun redo()

    fun shouldAdd(item: HistoryItem): Boolean = true
}