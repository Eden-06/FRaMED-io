package io.framed.framework.util

import de.westermann.kobserve.Property
import de.westermann.kobserve.event.EventHandler
import de.westermann.kobserve.event.EventListener
import de.westermann.kobserve.property.property
import io.framed.framework.ConnectionManager
import io.framed.framework.model.ModelElement
import io.framed.framework.linker.PreviewLinker
import io.framed.framework.linker.ShapeLinker
import io.framed.framework.pictogram.BoxShape
import kotlin.js.Date
import kotlin.reflect.KMutableProperty0

sealed class LinkerShapeBox<M : ModelElement<out M>, L : ShapeLinker<out M, *>>(
        private val connectionManager: ConnectionManager?
) {
    private var listeners: List<EventListener<*>> = emptyList()
    var view: BoxShape = BoxShape(-1)
        set(value) {
            for (it in listeners) {
                it.detach()
            }
            field = value
            setIgnore(value)
            listeners = listOf(
                    value.visibleProperty.onChange.reference(this::emitChildrenChanged),
                    value.widthProperty.onChange.reference(this::emitChildrenChanged),
                    value.heightProperty.onChange.reference(this::emitChildrenChanged)
            )
        }
    var previewBox: BoxShape? = null
        set(value) {
            field = value
            value?.let(this::setIgnore)
        }

    private var conditionalBoxes: List<Pair<BoxShape, (L) -> Boolean>> = emptyList()

    private val linkerMap: MutableMap<L, List<EventListener<*>>> = mutableMapOf()

    val linkers: List<L>
        get() = linkerMap.keys.toList()

    protected abstract fun backingContains(model: M): Boolean
    protected abstract fun backingAdd(model: M)
    protected abstract fun backingRemove(model: M)

    private var ignoreTime = 0.0
    private fun setIgnore(shape: BoxShape) {
        val reset: (Unit) -> Unit = { ignoreTime = Date.now() + TIMEOUT }
        shape.layerProperty.onChange(reset)
        shape.onRender(reset)
        reset(Unit)
    }

    private fun emitChildrenChanged(unit: Unit) {
        if (Date.now() > ignoreTime) {
            onChildrenChange.emit(unit)
        } else {
            ignoreTime = Date.now() + TIMEOUT
        }
    }

    val onChildrenChange = EventHandler<Unit>()

    fun addAllPreviews() {
        previewBox?.let { box ->
            linkers.forEach { linker ->
                if (linker is PreviewLinker<*, *, *>) {
                    box += if (box.position == BoxShape.Position.ABSOLUTE) {
                        linker.pictogram
                    } else {
                        linker.preview
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

        val emit = emit@{ unit: Unit ->
            val box = previewBox ?: return@emit
            val sameLayer = box.shapes.all {
                it.layer == box.layer
            }
            if (sameLayer && !box.ignore) {
                emitChildrenChanged(unit)
            }
        }

        linkerMap[linker] = listOf(
                linker.pictogram.widthProperty.onChange.reference(emit),
                linker.pictogram.heightProperty.onChange.reference(emit),
                linker.pictogram.leftProperty.onChange.reference(emit),
                linker.pictogram.topProperty.onChange.reference(emit)
        )
        view += linker.pictogram

        previewBox?.let { box ->

            val prev =
                    if (box.position == BoxShape.Position.ABSOLUTE || linker !is PreviewLinker<*, *, *>) {
                        linker.pictogram
                    } else {
                        linker.preview
                    }

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
    }

    private fun internalRemove(linker: L) {
        backingRemove(linker.model)

        linkerMap.remove(linker)?.forEach { it.detach() }

        view -= linker.pictogram
        for ((box, _) in conditionalBoxes) {
            box -= linker.pictogram
            if (linker is PreviewLinker<*, *, *>) {
                box -= linker.pictogram
                box -= linker.preview
            }
        }

        previewBox?.let { box ->
            if (linker is PreviewLinker<*, *, *>) {
                box -= linker.pictogram
                box -= linker.preview
            }
        }
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
        if (linker in linkers) {
            internalRemove(linker)
            internalAdd(linker)
        }
    }

    operator fun plusAssign(linker: L) = add(linker)
    operator fun minusAssign(linker: L) = remove(linker)
    operator fun contains(linker: L) = linker in linkers

    class SetShapeBox<M : ModelElement<out M>, L : ShapeLinker<out M, *>>(
            kProperty: KMutableProperty0<Set<M>>,
            connectionManager: ConnectionManager? = null
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
            connectionManager: ConnectionManager? = null
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

    companion object {
        private const val TIMEOUT = 500
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
