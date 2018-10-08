package io.framed.framework.util

import io.framed.framework.Controller

interface HistoryItem {
    //val controller: Controller

    fun undo()

    fun redo()

    fun shouldAdd(item: HistoryItem): Boolean = true
}