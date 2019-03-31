package io.framed.framework.util

import kotlin.js.Date

class HistoryGroup() : HistoryItem {

    constructor(historyItem: HistoryItem): this() {
        items += historyItem
    }

    private var items: List<HistoryItem> = emptyList()

    internal var timeout = Date.now()
    private var groupCount = 0
    private var groupDescription: String? = null

    val isOpen: Boolean
        get() = Date.now() - timeout < 100.0 || groupCount > 0


    fun endGroup() {
        groupCount -= 1
    }

    fun startGroup(description: String) {
        groupCount += 1
        groupDescription = description
    }

    override val description: String
        get() = groupDescription ?: items.first().description

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
        return isOpen || (items.lastOrNull()?.canApply(item) ?: false)
    }

    override fun apply(item: HistoryItem) {
        if (items.isNotEmpty() && items.last().canApply(item)) {
            items.last().apply(item)
        } else {
            items += item
        }
        timeout = Date.now()
    }
}
