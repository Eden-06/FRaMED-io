package de.westermann.kobserve.property

import de.westermann.kobserve.event.EventHandler
import de.westermann.kobserve.ReadOnlyProperty
import kotlin.reflect.KProperty1

open class ReceiverReadOnlyProperty<R, T>(
    private val attribute: KProperty1<R, T>,
    private val receiver: ReadOnlyProperty<R>
) : ReadOnlyProperty<T> {

    protected open var internal: T = attribute.get(receiver.value)

    override fun get(): T {
        val newValue = attribute.get(receiver.value)
        if (newValue != internal) {
            receiver.onChange.emit(Unit)
        }
        return newValue
    }

    final override val onChange = EventHandler<Unit>()

    override fun invalidate() {
        val newValue = attribute.get(receiver.value)
        if (newValue != internal) {
            internal = newValue
            onChange.emit(Unit)
        }
    }

    init {
        receiver.onChange {
            invalidate()
        }
    }
}

/**
 * Maps the given property to an readonly field attribute.
 * The returned property supports invalidation.
 *
 * @param attribute The readonly field attribute.
 */
fun <T, R> ReadOnlyProperty<R>.mapBinding(attribute: KProperty1<R, T>): ReadOnlyProperty<T> =
    ReceiverReadOnlyProperty(attribute, this)
