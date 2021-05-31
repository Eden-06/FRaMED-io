package de.westermann.kobserve.property

import de.westermann.kobserve.Binding
import de.westermann.kobserve.Property
import de.westermann.kobserve.ReadOnlyProperty
import kotlin.reflect.KMutableProperty1

class ReceiverProperty<R, T>(
    private val attribute: KMutableProperty1<R, T>,
    private val receiver: ReadOnlyProperty<R>
) : ReceiverReadOnlyProperty<R, T>(attribute, receiver), Property<T> {

    override var internal: T = attribute.get(receiver.value)

    override fun set(value: T) {
        super.set(value)
        if (internal != value) {
            attribute.set(receiver.value, value)
            receiver.onChange.emit(Unit)
        }
    }

    override var binding: Binding<T> = Binding.Unbound()
}

/**
 * Maps the given property to an mutable field attribute.
 * The returned property supports invalidation.
 *
 * @param attribute The mutable field attribute.
 */
fun <T, R> ReadOnlyProperty<R>.mapBinding(attribute: KMutableProperty1<R, T>): Property<T> =
    ReceiverProperty(attribute, this)
