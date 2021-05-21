package de.westermann.kobserve.property

import de.westermann.kobserve.Binding
import de.westermann.kobserve.Property
import de.westermann.kobserve.ReadOnlyProperty
import kotlin.reflect.KProperty1


class FlatReceiverProperty<R, T>(
    private val attribute: KProperty1<R, Property<T>>,
    receiver: ReadOnlyProperty<R>
) : FlatReceiverReadOnlyProperty<R, T>(attribute, receiver), Property<T> {

    override fun set(value: T) {
        super.set(value)
        attribute.get(receiver.value).value = value
    }

    override var binding: Binding<T> = Binding.Unbound()
}

fun <R, T> ReadOnlyProperty<R>.flatMapBinding(attribute: KProperty1<R, Property<T>>): Property<T> =
    FlatReceiverProperty(attribute, this)
