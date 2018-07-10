package io.framed.util

/**
 * @author lars
 */
class EventHandler<E : Any> {
    private var listeners: List<(event: E) -> Unit> = emptyList()

    fun addListener(listener: (event: E) -> Unit) {
        listeners += listener
    }

    fun removeListener(listener: (event: E) -> Unit) {
        listeners -= listener
    }

    fun on(listener: (event: E) -> Unit) = addListener(listener)

    fun clearListeners() {
        listeners = emptyList()
    }

    fun fire(event: E) {
        listeners.forEach { it(event) }
    }
}