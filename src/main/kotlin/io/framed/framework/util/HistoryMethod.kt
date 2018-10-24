package io.framed.framework.util

class HistoryMethod<T>(
        val value: T,
        val executeFunction: (T) -> Unit,
        val undoFunction: (T) -> Unit,
        override val description: String
) : HistoryItem {
    override fun undo() {
        undoFunction(value)
    }

    override fun redo() {
        executeFunction(value)
    }

    fun execute() = redo()
}