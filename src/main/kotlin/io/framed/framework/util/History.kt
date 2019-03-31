package io.framed.framework.util

import de.westermann.kobserve.EventHandler
import de.westermann.kobserve.Property
import de.westermann.kobserve.basic.mapBinding
import de.westermann.kobserve.basic.property
import kotlin.js.Date

object History {

    private var list: List<HistoryGroup> = emptyList()
    private val pointerProperty = property(list.size)
    private var pointer by pointerProperty

    private var ignoreTime = 0.0
    private var ignore = false

    fun push(historyItem: HistoryItem) {
        val top = list.getOrNull(pointer - 1)

        val now = Date.now()
        val diff = now - ignoreTime
        if (ignore || diff < 200.0) {
            ignoreTime = Date.now()
            if (top != null && top.isOpen) {
                top.timeout = now
            }
            return
        }

        if (top != null && top.canApply(historyItem)) {
            top.apply(historyItem)
        } else {
            list = list.subList(0, pointer) + HistoryGroup(historyItem)
            pointer = list.size

            checkOnChange()
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

    fun group(description: String, block: () -> Unit) {
        val top = list.getOrNull(pointer - 1)

        val group = if (top != null && top.isOpen) {
            top
        } else {
            val group = HistoryGroup()
            list = list.subList(0, pointer) + group
            pointer = list.size
            checkOnChange()
            group
        }

        group.startGroup(description)

        block()

        group.endGroup()
    }

    fun ignore(block: () -> Unit) {
        ignoreTime = Date.now()
        ignore = true

        block()

        ignore = false
    }
}

fun <P : Property<T>, T> P.trackHistory(grouping: Any? = null): P {
    var oldValue = get()

    onChange {
        val newValue = get()
        if (grouping == null) {
            History.push(HistoryProperty(this, oldValue, newValue))
        } else {
            History.push(HistoryGroupingProperty(grouping, this, oldValue, newValue))
        }
        oldValue = newValue
    }

    return this
}
