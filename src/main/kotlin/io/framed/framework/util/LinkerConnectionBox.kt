package io.framed.framework.util

import io.framed.framework.ConnectionLinker
import io.framed.framework.ConnectionManager
import io.framed.framework.ModelConnection
import kotlin.reflect.KMutableProperty0

class LinkerConnectionBox<M : ModelConnection<M>, L : ConnectionLinker<M>>(
        kProperty: KMutableProperty0<Set<M>>,
        val parent: ConnectionManager
) {
    val property: Property<Set<M>> = property(kProperty)

    private var backingField by property

    var linkers = emptySet<L>()

    private fun internalAdd(linker: L) {
        if (!backingField.contains(linker.model)) {
            backingField += linker.model
        }

        parent.onConnectionAdd.fire(linker)

        linkers += linker
    }

    private fun internalRemove(linker: L) {
        backingField -= linker.model
        parent.onConnectionRemove.fire(linker)

        linkers -= linker

        onRemove.fire(Unit)
    }

    fun add(linker: L) {
        val addToHistory = !backingField.contains(linker.model)

        val item = HistoryMethod(linker, this::internalAdd, this::internalRemove, "Add ${linker::class.simpleName} - ${linker.id}")
        item.execute()

        if (addToHistory) {
            History.push(item)
        }
    }

    fun remove(linker: L) {
        val item = HistoryMethod(linker, this::internalRemove, this::internalAdd, "Remove ${linker::class.simpleName} - ${linker.id}")
        item.execute()
        History.push(item)
    }

    operator fun plusAssign(linker: L) = add(linker)
    operator fun minusAssign(linker: L) = remove(linker)

    val onRemove = EventHandler<Unit>()
}