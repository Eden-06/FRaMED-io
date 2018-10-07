package io.framed.framework.util

import org.w3c.dom.events.Event
import org.w3c.dom.events.EventListener

/**
 * Utility class for generic model based patterns.
 *
 * @author lars
 */
class EventHandler<E> {
    private var listeners: Set<(event: E) -> Unit> = emptySet()

    /**
     * Add a listener to this model.
     *
     * @param listener Listener to add.
     */
    fun addListener(listener: (event: E) -> Unit): (E) -> Unit {
        listeners += listener
        return listener
    }

    operator fun plusAssign(listener: (event: E) -> Unit) {
        addListener(listener)
    }

    /**
     * Remove a listener from this model.
     *
     * @param listener Listener to remove.
     */
    fun removeListener(listener: (event: E) -> Unit) {
        listeners -= listener
    }

    operator fun minusAssign(listener: (event: E) -> Unit) = removeListener(listener)

    operator fun invoke(listener: (event: E) -> Unit) = addListener(listener)

    /**
     * Remove all listener from this model.
     */
    fun clearListeners() {
        listeners = emptySet()
    }

    /**
     * Fires a new model. Call all assigned listeners.
     *
     * @param event Event to fire
     */
    fun fire(event: E) {
        listeners.forEach { it(event) }
    }

    @Suppress("UNCHECKED_CAST")
    val eventListener: EventListener
        get() = object : EventListener {
            override fun handleEvent(event: Event) {
                (event as? E)?.let {
                    fire(it)
                }
            }
        }
}