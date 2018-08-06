package io.framed.util

import org.w3c.dom.events.Event
import org.w3c.dom.events.EventListener

/**
 * Utility class for generic event based patterns.
 *
 * @author lars
 */
class EventHandler<E : Any> {
    private var listeners: List<(event: E) -> Unit> = emptyList()

    /**
     * Add a listener to this event.
     *
     * @param listener Listener to add.
     */
    fun addListener(listener: (event: E) -> Unit): (E) -> Unit {
        listeners += listener
        return listener
    }

    /**
     * Remove a listener from this event.
     *
     * @param listener Listener to remove.
     */
    fun removeListener(listener: (event: E) -> Unit) {
        listeners -= listener
    }

    operator fun invoke(listener: (event: E) -> Unit) = addListener(listener)

    /**
     * Remove all listener from this event.
     */
    fun clearListeners() {
        listeners = emptyList()
    }

    /**
     * Fires a new event. Call all assigned listeners.
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