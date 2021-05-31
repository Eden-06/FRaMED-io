package de.westermann.kobserve.list

import de.westermann.kobserve.event.EventHandler

class TransformList<P, T>(
    private val parent: ObservableReadOnlyList<P>,
    private val useCache: Boolean,
    private val transform: (P) -> T
) : ObservableReadOnlyList<T> {

    override val onAdd: EventHandler<ListAddEvent<T>> = EventHandler()
    override val onUpdate: EventHandler<ListUpdateEvent<T>> = EventHandler()
    override val onRemove: EventHandler<ListRemoveEvent> = EventHandler(parent.onRemove)
    override val onChange: EventHandler<Unit> = EventHandler(parent.onChange)

    override val size: Int
        get() = parent.size

    private val cache: MutableMap<Int, T> = mutableMapOf()
    private val isCacheComplete: Boolean
        get() = cache.size == parent.size

    override fun contains(element: T): Boolean {
        if (useCache) {
            if (cache.values.contains(element)) {
                return true
            }
            if (isCacheComplete) {
                return false
            }
        }

        for (i in 0 until size) {
            if (element == get(i)) {
                return true
            }
        }

        return false
    }

    override fun containsAll(elements: Collection<T>): Boolean {
        if (useCache) {
            if (cache.values.containsAll(elements)) {
                return true
            }
            if (isCacheComplete) {
                return false
            }
        }

        return elements.all(this::contains)
    }

    override fun get(index: Int): T {
        return if (useCache) {
            cache.getOrPut(index) { transform(parent[index]) }
        } else transform(parent[index])
    }

    override fun indexOf(element: T): Int {
        for (i in 0 until size) {
            if (element == get(i)) {
                return i
            }
        }
        return -1
    }

    override fun isEmpty(): Boolean = parent.isEmpty()

    override fun lastIndexOf(element: T): Int {
        for (i in (0 until size).reversed()) {
            if (element == get(i)) {
                return i
            }
        }
        return -1
    }

    override fun notifyItemChanged(index: Int) {
        parent.notifyItemChanged(index)
    }

    override fun notifyDatasetChanged() {
        parent.notifyDatasetChanged()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as TransformList<*, *>

        if (parent != other.parent) return false
        if (transform != other.transform) return false

        return true
    }

    override fun hashCode(): Int {
        var result = parent.hashCode()
        result = 31 * result + transform.hashCode()
        return result
    }

    init {
        parent.onAdd { (index, _) ->
            if (useCache) {
                cache.keys.filter {
                    it >= index
                }.sortedDescending().forEach {
                    val oldCache = cache[it]
                    if (oldCache != null) {
                        cache[it + 1] = oldCache
                    }
                    cache.remove(it)
                }
            }

            onAdd.emit(ListAddEvent(index, get(index)))
        }

        parent.onUpdate { (oldIndex, newIndex, _)->
            if (useCache) {
                cache.clear() // TODO
            }
            onUpdate.emit(ListUpdateEvent(oldIndex, newIndex, get(newIndex)))
        }

        parent.onRemove { (index) ->
            if (useCache) {
                cache.remove(index)
                cache.keys.filter {
                    it > index
                }.sorted().forEach {
                    val oldCache = cache[it]
                    if (oldCache != null) {
                        cache[it - 1] = oldCache
                    }
                    cache.remove(it)
                }
            }
            onRemove.emit(ListRemoveEvent(index))
        }
    }
}

fun <P, T> ObservableReadOnlyList<P>.mapObservable(
    useCache: Boolean = false,
    transform: (P) -> T
): ObservableReadOnlyList<T> =
    TransformList(this, useCache, transform)
