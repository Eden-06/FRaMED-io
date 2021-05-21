package de.westermann.kobserve.property

import de.westermann.kobserve.event.EventHandler
import de.westermann.kobserve.ReadOnlyProperty
import de.westermann.kobserve.event.listenTo

open class FunctionReadOnlyProperty<T>(
    protected open val functionAccessor: FunctionReadOnlyAccessor<T>
) : ReadOnlyProperty<T> {
    override val onChange = EventHandler<Unit>()

    override fun get(): T = functionAccessor.get()

    fun listenTo(vararg properties: ReadOnlyProperty<*>) {
        properties.forEach {
            onChange.listenTo(it.onChange)
        }
    }

    constructor(
        functionAccessor: FunctionReadOnlyAccessor<T>,
        vararg properties: ReadOnlyProperty<*>
    ) : this(functionAccessor) {
        listenTo(*properties)
    }
}

interface FunctionReadOnlyAccessor<T> {
    /**
     * Perform the get operation of a property.
     */
    fun get(): T
}

/**
 * Create a readonly property that calls the given function accessor on every get operation.
 *
 * @param functionAccessor The function accessor to call.
 * @param properties The new property will listen to their onChange events.
 */
fun <T> property(
    functionAccessor: FunctionReadOnlyAccessor<T>,
    vararg properties: ReadOnlyProperty<*>
): ReadOnlyProperty<T> = FunctionReadOnlyProperty(functionAccessor, *properties)

/**
 * Create a readonly property that calls the given function accessor on every get operation.
 *
 * @param properties The new property will listen to their onChange events.
 * @param accessor The function accessor to call.
 */
fun <T> property(vararg properties: ReadOnlyProperty<*>, accessor: () -> T): ReadOnlyProperty<T> =
    FunctionReadOnlyProperty(object : FunctionReadOnlyAccessor<T> {
        override fun get(): T = accessor()
    }, *properties)

/**
 * Convenient function to map two properties together. The new property will call the given block with both property
 * values as parameter and return the result as its value.
 */
fun <A, B, C> ReadOnlyProperty<A>.join(
    property2: ReadOnlyProperty<B>,
    block: (A, B) -> C
): ReadOnlyProperty<C> {
    return FunctionReadOnlyProperty(object : FunctionReadOnlyAccessor<C> {
        override fun get(): C {
            return block(this@join.value, property2.value)
        }
    }, this, property2)
}
