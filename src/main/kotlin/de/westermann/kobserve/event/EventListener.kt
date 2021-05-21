package de.westermann.kobserve.event

/**
 * This interface represents a reference to a listener.
 */
interface EventListener<T> {

    /**
     * Trigger the referencing event listener.
     * This call does not affect the parent event handler.
     *
     * @param event The event listener call parameter.
     */
    fun emit(event: T)

    /**
     * Checks if the referencing event listener is part of the parent event handler.
     */
    val isAttached: Boolean

    /**
     * Add the referencing event listener to parent event handler if it is not already present.
     *
     * @return True if the referencing event listener was added.
     */
    fun attach(): Boolean

    /**
     * Remove the referencing event listener to parent event handler if it is present.
     *
     * @return True if the referencing event listener was removed.
     */
    fun detach(): Boolean
}
