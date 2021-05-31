package de.westermann.kobserve.property

import de.westermann.kobserve.Binding
import de.westermann.kobserve.event.EventHandler
import de.westermann.kobserve.Property

class ObjectProperty<T>(initValue: T) : Property<T> {
    private var internal: T = initValue

    override fun set(value: T) {
        super.set(value)
        if (internal != value) {
            internal = value
            onChange.emit(Unit)
        }
    }

    override fun get(): T = internal

    override val onChange = EventHandler<Unit>()
    override var binding: Binding<T> = Binding.Unbound()
}

/**
 * Create an observable property with the given initial value. The property stores the value internally.
 *
 * @param initValue The initial value of the property.
 */
fun <T> property(initValue: T): Property<T> = ObjectProperty(initValue)

/**
 * Create an observable property with the given initial value. The property stores the value internally.
 *
 * @receiver The initial value of the property.
 */
fun <T> T.observe(): Property<T> = ObjectProperty(this)
