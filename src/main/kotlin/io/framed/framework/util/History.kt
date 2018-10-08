package io.framed.framework.util

object History {
    private var list: List<HistoryItem> = emptyList()
    private var pointer: Int = list.size

    private var allowPush = true

    fun <V : Any> push(property: Property<V>, oldValue: V, newValue: V = property.get()) =
            push(HistoryProperty(property, oldValue, newValue))

    fun push(historyItem: HistoryItem) {
        if (allowPush) {
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

    private fun disablePush(block: () -> Unit) {
        allowPush = false
        block()
        allowPush = true
    }

    fun undo() {
        if (!canUndo) {
            throw IllegalStateException()
        }
        disablePush {
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
        disablePush {
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
}

fun <T : Any> Property<T>.trackHistory(): Property<T> {
    var oldValue = get()

    onChange {
        History.push(this, oldValue)
        oldValue = get()
    }
    return this
}