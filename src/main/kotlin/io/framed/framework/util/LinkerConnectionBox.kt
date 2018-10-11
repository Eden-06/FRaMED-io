package io.framed.framework.util

import io.framed.framework.*
import io.framed.framework.pictogram.Connection
import kotlin.reflect.KMutableProperty0

class LinkerConnectionBox<M : ModelConnection, L : ConnectionLinker<M>>(
        kProperty: KMutableProperty0<List<M>>,
        val parent: ModelLinker<*, *, *>
) {
    val property: Property<List<M>> = property(kProperty)

    private var backingField by property

    var linkers = emptyList<L>()

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

        val item = HistoryMethod(linker, this::internalAdd, this::internalRemove)
        item.execute()

        if (addToHistory) {
            History.push(item)
        }
    }

    fun remove(linker: L) {
        val item = HistoryMethod(linker, this::internalRemove, this::internalAdd)
        item.execute()
        History.push(item)
    }

    operator fun plusAssign(linker: L) = add(linker)
    operator fun minusAssign(linker: L) = remove(linker)

    val onRemove = EventHandler<Unit>()
}
/*


    private fun addRelation(linker: AssociationLinker) {
        if (!model.relations.contains(linker.model)) {
            model.relations += linker.model
        }

        relations += linker
        onConnectionAdd.fire(linker)
    }

    fun removeRelation(linker: AssociationLinker) {
        model.relations -= linker.model
        relations -= linker
        onConnectionRemove.fire(linker)
    }

*/