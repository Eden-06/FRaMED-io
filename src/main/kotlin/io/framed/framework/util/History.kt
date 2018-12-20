package io.framed.framework.util

import de.westermann.kobserve.EventHandler
import de.westermann.kobserve.Property
import de.westermann.kobserve.basic.mapBinding
import de.westermann.kobserve.basic.property

object History {
    private var list: List<HistoryItem> = emptyList()
    private val pointerProperty = property(list.size)
    private var pointer by pointerProperty

    private var allowPush = true

    private var createGroup = 0
    private var group: List<HistoryItem> = emptyList()

    fun <V> push(property: Property<V>, oldValue: V, newValue: V = property.get()) =
            push(HistoryProperty(property, oldValue, newValue))

    fun push(historyItem: HistoryItem) {
        if (allowPush) if (createGroup > 0) {
            val top = group.lastOrNull()

            if (top != null && top.canApply(historyItem)) {
                top.apply(historyItem)
            } else {
                group += historyItem
            }
        } else {
            val top = list.getOrNull(pointer - 1)

            if (top != null && top.canApply(historyItem)) {
                top.apply(historyItem)
            } else {
                list = list.subList(0, pointer) + historyItem
                pointer = list.size

                checkOnChange()
            }
        }
    }

    fun clear() {
        list = emptyList()
        pointer = 0
    }

    val canUndoProperty = pointerProperty.mapBinding { it > 0 }
    val canUndo by canUndoProperty

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

    val canRedoProperty = pointerProperty.mapBinding { it < list.size }
    val canRedo by canRedoProperty

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

            onChange.emit(Unit)
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

fun <P : Property<T>, T> P.trackHistory(): P {
    var oldValue = get()

    onChange {
        History.push(this, oldValue)
        oldValue = get()
    }

    return this
}