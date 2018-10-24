package io.framed.framework.util

object History {
    private var list: List<HistoryItem> = emptyList()
    private var pointer: Int = list.size

    private var allowPush = true

    private var createGroup = 0
    private var group: List<HistoryItem> = emptyList()

    fun <V : Any> push(property: Property<V>, oldValue: V, newValue: V = property.get()) =
            push(HistoryProperty(property, oldValue, newValue))

    fun push(historyItem: HistoryItem) {
        if (allowPush) if (createGroup > 0) {
            val top = group.lastOrNull()

            if (top == null || top.shouldAdd(historyItem)) {
                group += historyItem
            }
        } else {
            val top = list.getOrNull(pointer - 1)

            if (top == null || top.shouldAdd(historyItem)) {
                list = list.subList(0, pointer) + historyItem
                pointer = list.size

                checkOnChange()
            }
        }
    }

    val canUndo: Boolean
        get() = pointer > 0

    fun undo() {
        if (!canUndo) {
            throw IllegalStateException()
        }
        ignore {
            list[pointer - 1].undo()
            pointer -= 1

            checkOnChange()
        }
    }

    val canRedo: Boolean
        get() = pointer < list.size

    fun redo() {
        if (!canRedo) {
            throw IllegalStateException()
        }
        ignore {
            list[pointer].redo()
            pointer += 1

            checkOnChange()
        }
    }

    val onChange = EventHandler<Unit>()

    private var lastCanRedo = false
    private var lastCanUndo = false
    private fun checkOnChange() {
        if (canUndo != lastCanUndo || canRedo != lastCanRedo) {
            lastCanUndo = canUndo
            lastCanRedo = canRedo

            onChange.fire(Unit)
        }
    }

    private fun startGroup(description: String) {
        if (createGroup == 0) {
            groupDescription = description
        }
        createGroup += 1
    }

    private fun endGroup() {
        createGroup -= 1
        if (createGroup == 0 && group.isNotEmpty()) {
            push(HistoryGroup(group, groupDescription))
            group = emptyList()
        }
    }

    var groupDescription = ""
    fun group(description: String, block: () -> Unit) {
        startGroup(description)
        block()
        endGroup()
    }

    fun ignore(block: () -> Unit) {
        allowPush = false
        block()
        allowPush = true
    }
}

fun <T : Any> Property<T>.trackHistory(): Property<T> {
    var oldValue = get()

    onChange {
        History.push(this, oldValue)
        oldValue = get()
    }
    return this
}