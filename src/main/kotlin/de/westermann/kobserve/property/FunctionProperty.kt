package de.westermann.kobserve.property

import de.westermann.kobserve.Binding
import de.westermann.kobserve.Property
import de.westermann.kobserve.ReadOnlyProperty
import de.westermann.kobserve.ValidationProperty

class FunctionProperty<T>(
    override val functionAccessor: FunctionAccessor<T>
) : FunctionReadOnlyProperty<T>(functionAccessor), ValidationProperty<T> {

    override fun set(value: T) {
        super.set(value)
        validProperty.value = functionAccessor.set(value)
    }

    override val validProperty = property(true)
    override val valid by validProperty

    constructor(
        functionAccessor: FunctionAccessor<T>,
        vararg properties: ReadOnlyProperty<*>
    ) : this(functionAccessor) {
        listenTo(*properties)
    }

    override var binding: Binding<T> = Binding.Unbound()
}

interface FunctionAccessor<T> : FunctionReadOnlyAccessor<T> {
    /**
     * Perform the set operation of a property.
     *
     * @param value The new value that should be applied.
     * @return Should return true if the value was accepted. The return value will be the new valid flag.
     */
    fun set(value: T): Boolean
}

/**
 * Create a validation property that calls the given function accessor on every get and set operation.
 *
 * @param functionAccessor The function accessor to call.
 * @param properties The new property will listen to their onChange events.
 */
fun <T> property(
    functionAccessor: FunctionAccessor<T>,
    vararg properties: ReadOnlyProperty<*>
): Property<T> = FunctionProperty(functionAccessor, *properties)
