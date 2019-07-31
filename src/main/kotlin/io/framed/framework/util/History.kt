package io.framed.framework.util

import de.westermann.kobserve.Property
import de.westermann.kobserve.event.EventHandler
import de.westermann.kobserve.property.mapBinding
import de.westermann.kobserve.property.property
import kotlin.js.Date

/**
 * Global history access.
 */
object History {

    private var list: List<HistoryGroup> = emptyList()
    private val pointerProperty = property(list.size)
    private var pointer by pointerProperty

    private var ignoreTime = 0.0
    private var ignore = false

    /**
     * Add a history item to the current queue.
     */
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

    /**
     * Clear the history queue.
     */
    fun clear() {
        list = emptyList()
        pointer = 0
    }

    /**
     * BooleanProperty that describes if their is an history item in the queue that can be undone.
     */
    val canUndoProperty = pointerProperty.mapBinding { it > 0 }

    /**
     * Boolean that describes if their is an history item in the queue that can be undone.
     */
    val canUndo by canUndoProperty

    /**
     * Undo the current history item in the queue.
     *
     * @throws IllegalStateException if their is no item that can be undone.
     */
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

    /**
     * BooleanProperty that describes if their is an history item in the queue that can be redone.
     */
    val canRedoProperty = pointerProperty.mapBinding { it < list.size }

    /**
     * Boolean that describes if their is an history item in the queue that can be redone.
     */
    val canRedo by canRedoProperty

    /**
     * Redo the current history item in the queue.
     *
     * @throws IllegalStateException if their is no item that can be redone.
     */
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

    /**
     * Fires when the redo or undo state is changing.
     */
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

    /**
     * Start a grouping block. All push operations within this block will be grouped to one history item.
     */
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

    /**
     * Start an ignoring block. All push operations within this block will be ignored and will not affect the queue.
     */
    fun ignore(block: () -> Unit) {
        ignoreTime = Date.now()
        ignore = true

        block()

        ignore = false
    }
}

/**
 * Helper function that listens to the @receiver and tracks it's changes to the history.
 */
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
