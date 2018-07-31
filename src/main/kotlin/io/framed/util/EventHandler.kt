package io.framed.util

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

    /**
     * Add a listener to this event.
     *
     * @see addListener
     *
     * @param listener Listener to add.
     */
    fun on(listener: (event: E) -> Unit) = addListener(listener)

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
}