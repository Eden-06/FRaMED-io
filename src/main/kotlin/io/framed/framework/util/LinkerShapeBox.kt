package io.framed.framework.util

import io.framed.framework.Linker
import io.framed.framework.ModelElement
import io.framed.framework.PreviewLinker
import io.framed.framework.pictogram.BoxShape
import io.framed.framework.pictogram.Shape
import kotlin.reflect.KMutableProperty0

class LinkerShapeBox<M : ModelElement, L : Linker<M, out Shape>>(
        kProperty: KMutableProperty0<List<M>>
) {
    val property: Property<List<M>> = property(kProperty)
    lateinit var view: BoxShape
    var previewBox: BoxShape? = null

    private var backingField by property

    var linkers = emptyList<L>()

    private fun internalAdd(linker: L) {
        if (!backingField.contains(linker.model)) {
            backingField += linker.model
        }

        view += linker.pictogram

        previewBox?.let { box ->
            if (linker is PreviewLinker<*, *, *>) {
                box += linker.preview
            }
        }
        linkers += linker
    }

    private fun internalRemove(linker: L) {
        backingField -= linker.model
        view -= linker.pictogram

        previewBox?.let { box ->
            if (linker is PreviewLinker<*, *, *>) {
                box -= linker.preview
            }
        }
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
        History.group {
            linker.findConnections(linker.pictogram).forEach {
                it.delete()
            }
            val item = HistoryMethod(linker, this::internalRemove, this::internalAdd)
            item.execute()
            History.push(item)
        }
    }

    operator fun plusAssign(linker: L) = add(linker)
    operator fun minusAssign(linker: L) = remove(linker)

    val onRemove = EventHandler<Unit>()
}