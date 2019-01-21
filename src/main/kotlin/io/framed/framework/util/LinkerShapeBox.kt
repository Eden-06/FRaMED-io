package io.framed.framework.util

import de.westermann.kobserve.Property
import de.westermann.kobserve.basic.property
import io.framed.framework.ConnectionManager
import io.framed.framework.ModelElement
import io.framed.framework.PreviewLinker
import io.framed.framework.ShapeLinker
import io.framed.framework.pictogram.BoxShape
import kotlin.reflect.KMutableProperty0

sealed class LinkerShapeBox<M : ModelElement<out M>, L : ShapeLinker<out M, *>>(
        private val connectionManager: ConnectionManager?
) {
    lateinit var view: BoxShape
    var previewBox: BoxShape? = null

    private var conditionalBoxes: List<Pair<BoxShape, (L) -> Boolean>> = emptyList()

    var linkers = emptySet<L>()

    protected abstract fun backingContains(model: M): Boolean
    protected abstract fun backingAdd(model: M)
    protected abstract fun backingRemove(model: M)

    fun addAllPreviews() {
        previewBox?.let { box ->
            linkers.forEach { linker ->
                if (linker is PreviewLinker<*, *, *>) {
                    box += if (box.position == BoxShape.Position.ABSOLUTE) {
                        linker.flatPreview
                    } else {
                        linker.listPreview
                    }
                }
            }
        }
    }

    fun conditionalContainer(container: BoxShape, predicate: (L) -> Boolean) {
        conditionalBoxes += container to predicate
    }

    private fun internalAdd(linker: L) {
        if (!backingContains(linker.model)) {
            backingAdd(linker.model)
        }

        view += linker.pictogram

        previewBox?.let { box ->

            val prev = if (linker is PreviewLinker<*, *, *>) {
                if (box.position == BoxShape.Position.ABSOLUTE) {
                    linker.flatPreview
                } else {
                    linker.listPreview
                }
            } else return@let

            var added = false
            for ((b, cond) in conditionalBoxes) {
                if (cond(linker)) {
                    b += prev
                    added = true
                }
            }
            if (!added) {
                box += prev
            }
        }
        linkers += linker
    }

    private fun internalRemove(linker: L) {
        backingRemove(linker.model)
        view -= linker.pictogram
        for ((box, _) in conditionalBoxes) {
            box -= linker.pictogram
            if (linker is PreviewLinker<*, *, *>) {
                box -= linker.flatPreview
                box -= linker.listPreview
            }
        }

        previewBox?.let { box ->
            if (linker is PreviewLinker<*, *, *>) {
                box -= linker.flatPreview
                box -= linker.listPreview
            }
        }
        linkers -= linker
    }

    fun add(linker: L) {
        val addToHistory = !backingContains(linker.model)

        val item = HistoryMethod(linker, this::internalAdd, this::internalRemove, "Add ${linker::class.simpleName} - ${linker.id}")
        item.execute()

        if (addToHistory) {
            History.push(item)

            //TODO add auto layout
        }
    }

    fun remove(linker: L) {
        History.group("Remove ${linker::class.simpleName} - ${linker.id}") {
            connectionManager?.listConnections(linker.id)?.forEach {
                it.delete()
            }
            val item = HistoryMethod(linker, this::internalRemove, this::internalAdd, "Remove ${linker::class.simpleName} - ${linker.id}")
            item.execute()
            History.push(item)
        }
    }

    fun redraw(linker: L) {
        internalRemove(linker)
        internalAdd(linker)
    }

    operator fun plusAssign(linker: L) = add(linker)
    operator fun minusAssign(linker: L) = remove(linker)

    class SetShapeBox<M : ModelElement<out M>, L : ShapeLinker<out M, *>>(
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

    class ListShapeBox<M : ModelElement<out M>, L : ShapeLinker<out M, *>>(
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

fun <M : ModelElement<out M>, L : ShapeLinker<out M, *>> shapeBox(
        kProperty: KMutableProperty0<List<M>>,
        connectionManager: ConnectionManager? = null,
        init: (LinkerShapeBox<M, L>) -> Unit = {}
): LinkerShapeBox<M, L> = LinkerShapeBox.ListShapeBox<M, L>(kProperty, connectionManager).also(init)

fun <M : ModelElement<out M>, L : ShapeLinker<out M, *>> shapeBox(
        kProperty: KMutableProperty0<Set<M>>,
        connectionManager: ConnectionManager? = null,
        init: (LinkerShapeBox<M, L>) -> Unit = {}
): LinkerShapeBox<M, L> = LinkerShapeBox.SetShapeBox<M, L>(kProperty, connectionManager).also(init)
