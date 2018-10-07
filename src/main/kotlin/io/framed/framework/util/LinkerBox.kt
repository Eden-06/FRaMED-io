package io.framed.framework.util

import io.framed.framework.Linker
import io.framed.framework.ModelElement
import io.framed.framework.PreviewLinker
import io.framed.framework.pictogram.BoxShape
import io.framed.framework.pictogram.Shape
import kotlin.reflect.KMutableProperty0

class LinkerBox<M : ModelElement, L : Linker<M, out Shape>>(
        kProperty: KMutableProperty0<List<M>>
) {
    val property: Property<List<M>> = property(kProperty)
    lateinit var view: BoxShape
    var previewBox: BoxShape? = null

    private var backingField by property

    var linkers = emptyList<L>()

    fun add(linker: L) {
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

    fun remove(linker: L) {
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

    operator fun plusAssign(linker: L) = add(linker)
    operator fun minusAssign(linker: L) = remove(linker)

    val onRemove = EventHandler<Unit>()
}