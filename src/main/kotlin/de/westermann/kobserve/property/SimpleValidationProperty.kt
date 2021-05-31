package de.westermann.kobserve.property

import de.westermann.kobserve.Binding
import de.westermann.kobserve.Property
import de.westermann.kobserve.ValidationProperty
import de.westermann.kobserve.event.EventHandler
import de.westermann.kobserve.event.listenTo

class SimpleValidationProperty<T>(
    private val property: Property<T>,
    private val validator: (T) -> Boolean
) : ValidationProperty<T> {
    override val onChange = EventHandler<Unit>()

    override fun get(): T = property.get()

    override fun set(value: T) {
        super.set(value)
        validProperty.value = validator(value)
        if (valid) {
            property.set(value)
        }
    }

    override val validProperty = property(true)
    override val valid by validProperty
    override var binding: Binding<T> = Binding.Unbound()

    override fun invalidate() {
        validProperty.value = true
    }

    init {
        onChange.listenTo(property.onChange)
    }
}

/**
 * Apply an validation function to the given property.
 */
fun <T> Property<T>.validate(validator: (T) -> Boolean): ValidationProperty<T> =
    SimpleValidationProperty(this, validator)
