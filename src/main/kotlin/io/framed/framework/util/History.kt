package io.framed.framework.util

class History {
    private var list: List<HistoryItem> = emptyList()
    private var pointer: Int = list.size

    private var allowPush = true

    fun <V : Any> push(property: Property<V>, oldValue: V, newValue: V = property.get()) {
        if (allowPush) {
            list = list.subList(0, pointer) + HistoryProperty(property, oldValue, newValue)
            pointer = list.size
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

fun <T : Any> Property<T>.history(history: History): Property<T> {
    var oldValue = get()

    onChange {
        history.push(this, oldValue)
        oldValue = get()
    }
    return this
}