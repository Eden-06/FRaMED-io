package io.framed.framework.util

interface HistoryItem {
    fun undo()
    fun redo()
}