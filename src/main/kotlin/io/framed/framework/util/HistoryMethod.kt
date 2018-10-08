package io.framed.framework.util

import io.framed.framework.Linker
import io.framed.framework.ModelElement
import io.framed.framework.pictogram.Shape

class HistoryMethod<T>(
        val value: T,
        val executeFunction: (T) -> Unit,
        val undoFunction: (T) -> Unit
) : HistoryItem {
    override fun undo() {
        undoFunction(value)
    }

    override fun redo() {
        executeFunction(value)
    }

    fun execute() = redo()
}