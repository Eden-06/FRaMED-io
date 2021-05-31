package de.westermann.kobserve.property

import de.westermann.kobserve.Binding
import de.westermann.kobserve.Property
import kotlin.reflect.KMutableProperty0

class DirectReceiverProperty<T>(
    private val attribute: KMutableProperty0<T>
) : DirectReceiverReadOnlyProperty<T>(attribute), Property<T> {

    override fun set(value: T) {
        super.set(value)
        if (internal != value) {
            internal = value
            attribute.set(value)
            onChange.emit(Unit)
        }
    }

    override var binding: Binding<T> = Binding.Unbound()
}

fun <T> property(attribute: KMutableProperty0<T>): Property<T> =
    DirectReceiverProperty(attribute)

fun <T> KMutableProperty0<T>.observe(): Property<T> = DirectReceiverProperty(this)
