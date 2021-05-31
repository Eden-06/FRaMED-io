package de.westermann.kobserve.property

import de.westermann.kobserve.Binding
import de.westermann.kobserve.Property
import de.westermann.kobserve.ReadOnlyProperty


class FlatMapProperty<R, T>(
    private val transform: (R) -> Property<T>,
    receiver: ReadOnlyProperty<R>
) : FlatMapReadOnlyProperty<R, T>(transform, receiver), Property<T> {

    override fun set(value: T) {
        super.set(value)
        transform(receiver.value).value = value
    }

    override var binding: Binding<T> = Binding.Unbound()
}

fun <T, R> ReadOnlyProperty<R>.flatMapBinding(transform: (R) -> Property<T>): Property<T> =
    FlatMapProperty(transform, this)

fun <T> ReadOnlyProperty<Property<T>>.flatten(): Property<T> =
    FlatMapProperty({ it }, this)
