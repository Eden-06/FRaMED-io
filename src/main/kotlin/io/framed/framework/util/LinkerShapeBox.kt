package io.framed.framework.util

import io.framed.framework.*
import io.framed.framework.pictogram.BoxShape
import io.framed.framework.pictogram.Shape
import kotlin.reflect.KMutableProperty0

sealed class LinkerShapeBox<M : ModelElement<M>, L : ShapeLinker<M, *>>(
        private val connectionManager: ConnectionManager?
) {
    lateinit var view: BoxShape
    var previewBox: BoxShape? = null

    var linkers = emptySet<L>()

    protected abstract fun backingContains(model: M): Boolean
    protected abstract fun backingAdd(model: M)
    protected abstract fun backingRemove(model: M)

    private fun internalAdd(linker: L) {
        if (!backingContains(linker.model)) {
            backingAdd(linker.model)
        }

        view += linker.pictogram

        previewBox?.let { box ->
            if (linker is PreviewLinker<*, *, *>) {
                box += linker.flatPreview
            }
        }
        linkers += linker
    }

    private fun internalRemove(linker: L) {
        backingRemove(linker.model)
        view -= linker.pictogram

        previewBox?.let { box ->
            if (linker is PreviewLinker<*, *, *>) {
                box -= linker.flatPreview
            }
        }
        linkers -= linker

        onRemove.fire(Unit)
    }

    fun add(linker: L) {
        val addToHistory = !backingContains(linker.model)

        val item = HistoryMethod(linker, this::internalAdd, this::internalRemove)
        item.execute()

        if (addToHistory) {
            History.push(item)
        }
    }

    fun remove(linker: L) {
        History.group {
            connectionManager?.listConnections(linker.id)?.forEach {
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

    class SetShapeBox<M : ModelElement<M>, L : ShapeLinker<M, *>>(
            kProperty: KMutableProperty0<Set<M>>,
            private val connectionManager: ConnectionManager? = null
    ) : LinkerShapeBox<M, L>(connectionManager) {

        val property: Property<Set<M>> = property(kProperty)
        private var backingField by property

        override fun backingContains(model: M): Boolean = model in backingField

        override fun backingAdd(model: M) {
            backingField += model
        }

        override fun backingRemove(model: M) {
            backingField -= model
        }
    }

    class ListShapeBox<M : ModelElement<M>, L : ShapeLinker<M, *>>(
            kProperty: KMutableProperty0<List<M>>,
            private val connectionManager: ConnectionManager? = null
    ) : LinkerShapeBox<M, L>(connectionManager) {

        val property: Property<List<M>> = property(kProperty)
        private var backingField by property

        override fun backingContains(model: M): Boolean = model in backingField

        override fun backingAdd(model: M) {
            backingField += model
        }

        override fun backingRemove(model: M) {
            backingField -= model
        }
    }
}

fun <M : ModelElement<M>, L : ShapeLinker<M, *>> shapeBox(
        kProperty: KMutableProperty0<List<M>>,
        connectionManager: ConnectionManager? = null,
        init: (LinkerShapeBox<M, L>) -> Unit = {}
): LinkerShapeBox<M, L> = LinkerShapeBox.ListShapeBox<M, L>(kProperty, connectionManager).also(init)

fun <M : ModelElement<M>, L : ShapeLinker<M, *>> shapeBox(
        kProperty: KMutableProperty0<Set<M>>,
        connectionManager: ConnectionManager? = null,
        init: (LinkerShapeBox<M, L>) -> Unit = {}
): LinkerShapeBox<M, L> = LinkerShapeBox.SetShapeBox<M, L>(kProperty, connectionManager).also(init)