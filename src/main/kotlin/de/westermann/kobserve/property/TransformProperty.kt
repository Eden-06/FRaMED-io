package de.westermann.kobserve.property

import de.westermann.kobserve.ReadOnlyProperty
import de.westermann.kobserve.event.EventHandler

class TransformProperty<R, T>(
    private val transform: (R) -> T,
    private val dependency: ReadOnlyProperty<R>
) : ReadOnlyProperty<T> {

    private var internal: T = transform(dependency.value)

    override fun get(): T = internal

    override val onChange = EventHandler<Unit>()

    override fun invalidate() {
        val newValue = transform(dependency.value)

        if (newValue != internal) {
            internal = newValue
            onChange.emit(Unit)
        }
    }

    init {
        dependency.onChange {
            invalidate()
        }
    }
}

/**
 * Apply a transform function to the given property value and return a readonly property for the transformed value.
 * The returned property supports invalidation.
 */
fun <R, T> ReadOnlyProperty<R>.mapBinding(transform: (R) -> T): ReadOnlyProperty<T> =
    TransformProperty(transform, this)

/**
 * Create a read only view of the receiver property.
 */
fun <T> ReadOnlyProperty<T>.readOnly(): ReadOnlyProperty<T> =
    TransformProperty({ it }, this)
