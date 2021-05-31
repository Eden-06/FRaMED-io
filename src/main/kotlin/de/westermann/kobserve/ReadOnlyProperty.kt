package de.westermann.kobserve

import de.westermann.kobserve.event.EventHandler
import kotlin.reflect.KProperty

/**
 * Represents a readonly property of type 'T'.
 */
interface ReadOnlyProperty<T> {

    /**
     * Get the current value.
     */
    fun get(): T

    /**
     * Convenient access to the get method.
     */
    val value: T
        get() = get()

    operator fun getValue(container: Any?, property: KProperty<*>) = get()

    /**
     * Change event handler is called whenever the properties value is changed.
     */
    val onChange: EventHandler<Unit>

    /**
     * If the properties uses an external backing field this method checks for external data changes und eventually
     * emits an onChange event. Otherwise this method does nothing.
     */
    fun invalidate() {}
}
