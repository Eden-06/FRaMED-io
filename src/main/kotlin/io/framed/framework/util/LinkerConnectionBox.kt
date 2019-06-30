package io.framed.framework.util

import de.westermann.kobserve.Property
import de.westermann.kobserve.event.EventHandler
import de.westermann.kobserve.property.property
import io.framed.framework.linker.ConnectionLinker
import io.framed.framework.ConnectionManager
import io.framed.framework.model.ModelConnection
import kotlin.reflect.KMutableProperty0

class LinkerConnectionBox<M : ModelConnection<out M>, L : ConnectionLinker<out M>>(
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

        linkers += linker

        parent.onConnectionAdd.emit(linker)
    }

    private fun internalRemove(linker: L) {
        backingField -= linker.model

        linkers -= linker

        parent.onConnectionRemove.emit(linker)
        onRemove.emit(Unit)
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

    operator fun contains(linker: L): Boolean = linker in linkers

    val onRemove = EventHandler<Unit>()
}