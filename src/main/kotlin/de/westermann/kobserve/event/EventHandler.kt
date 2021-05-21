package de.westermann.kobserve.event

/**
 * This class represents a simple event handler who manages listeners for an event of type 'E'.
 */
class EventHandler<E>() {

    private val listeners: MutableMap<(E) -> Unit, EventListener<E>?> = mutableMapOf()

    /**
     * Add an event listener to this handler if it is not already present.
     *
     * @param listener The event listener to attach.
     *
     * @return The event listener that was added or null if it was already present.
     */
    fun addListener(listener: (E) -> Unit): ((E) -> Unit) {
        if (listener !in listeners) {
            listeners[listener] = null
            onAttach()
        }

        return listener
    }

    /**
     * Remove an event listener from this handler.
     *
     * @param listener The event listener to detach.
     */
    fun removeListener(listener: (E) -> Unit) {
        if (listener in listeners) {
            listeners -= listener
            onDetach()
        }
    }

    /**
     * Remove all event listeners from this handler.
     */
    fun clearListeners() {
        if (listeners.isNotEmpty()) {
            listeners.clear()
            onDetach()
        }
    }

    /**
     * Emit an event to all assigned event listeners.
     *
     * @param event The event to emit.
     */
    fun emit(event: E) {
        for (listener in listeners.keys) {
            listener(event)
        }
    }

    /**
     * @see addListener
     */
    operator fun invoke(listener: (E) -> Unit) {
        addListener(listener)
    }

    /**
     * @see addListener
     */
    operator fun plusAssign(listener: (E) -> Unit) {
        addListener(listener)
    }

    /**
     * @see removeListener
     */
    operator fun minusAssign(listener: (E) -> Unit) {
        removeListener(listener)
    }

    /**
     * Add an event listener to this handler if it is not already present.
     *
     * @param listener The event listener to attach.
     *
     * @return A reference object to the added listener or null if it was already present.
     */
    fun reference(listener: (E) -> Unit): EventListener<E> {
        addListener(listener)

        var reference = listeners[listener]
        if (reference == null) {
            reference = Listener(listener)
            listeners[listener] = reference
        }

        return reference
    }

    /**
     * Returns the count of assigned event listeners.
     */
    val size: Int
        get() = listeners.size

    operator fun contains(element: (E) -> Unit): Boolean {
        return listeners.contains(element)
    }

    fun isEmpty(): Boolean {
        return listeners.isEmpty()
    }

    operator fun iterator(): Iterator<(E) -> Unit> {
        return listeners.keys.iterator()
    }

    var onAttach: () -> Unit = {}
    var onDetach: () -> Unit = {}

    constructor(vararg dependencies: EventHandler<out E>) : this() {
        dependencies.forEach {
            it.addListener(this::emit)
        }
    }

    private inner class Listener(
        private val listener: (E) -> Unit
    ) : EventListener<E> {

        override fun emit(event: E) {
            listener(event)
        }

        /**
         * Checks if the referencing event listener is part of the parent event handler.
         */
        override val isAttached: Boolean
            get() = listener in listeners

        /**
         * Add the referencing event listener to parent event handler if it is not already present.
         *
         * @return True if the referencing event listener was added.
         */
        override fun attach(): Boolean {
            if (isAttached) {
                return false
            }

            addListener(listener)
            listeners[listener] = this
            return true
        }

        /**
         * Remove the referencing event listener to parent event handler if it is present.
         *
         * @return True if the referencing event listener was removed.
         */
        override fun detach(): Boolean {
            if (!isAttached) {
                return false
            }

            removeListener(listener)
            return true
        }
    }
}

/**
 * Utility function that allows simple event binding of an unit event to another generic event.
 *
 * @param handler An generic event handler to listen to.
 * @receiver The unit event handler that should listen.
 */
fun EventHandler<Unit>.listenTo(handler: EventHandler<*>) {
    handler {
        emit(Unit)
    }
}

/**
 * Combine two common event handler to listen two both simultaneously.
 */
infix fun <T> EventHandler<out T>.and(other: EventHandler<out T>): EventHandler<T> =
    EventHandler(this, other)

/**
 * Combine two common event handler to listen two both simultaneously.
 */
fun <T> EventHandler<out T>.and(other: EventHandler<out T>, listener: (T) -> Unit): EventHandler<T> =
    EventHandler(this, other).also { it += listener }

/**
 * Combine two common event handler to listen two both simultaneously.
 */
operator fun <T> EventHandler<out T>.plus(other: EventHandler<out T>): EventHandler<T> =
    EventHandler(this, other)
