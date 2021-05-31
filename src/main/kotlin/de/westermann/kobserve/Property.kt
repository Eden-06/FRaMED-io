package de.westermann.kobserve

import kotlin.reflect.KProperty

/**
 * Represents a read and write property of type 'T'.
 */
interface Property<T> : ReadOnlyProperty<T> {

    /**
     * An object that represents the current binding state.
     */
    var binding: Binding<T>

    val isWritable: Boolean
        get() = binding.isWritable

    /**
     * Set the current value.
     */
    fun set(value: T) {
        binding.checkWrite(value)
    }

    /**
     * Convenient access to the get and set method.
     */
    override var value: T
        get() = get()
        set(value) = set(value)

    operator fun setValue(container: Any?, property: KProperty<*>, value: T) = set(value)

    /**
     * Returns the current binding state.
     */
    val isBound: Boolean
        get() = binding.isBound

    /**
     * Bind this property to another readonly property. This property will always have the value of the
     * binding target. A write to the current may throw an `IllegalStateException`.
     *
     * @param target The binding target.
     *
     * @throws IllegalStateException if this property is already bound.
     */
    fun bind(target: ReadOnlyProperty<out T>) {
        if (isBound) {
            throw IllegalStateException("Property is already bound!")
        }

        binding = Binding.ReadOnlyBinding(this, target)
    }

    /**
     * Bind this property to another property bidirectionally. Both properties will sync their values. This property will take the
     * targets value as start value.
     *
     * @param target The binding target.
     *
     * @throws IllegalStateException if this property is already bound.
     */
    fun bindBidirectional(target: Property<T>) {
        if (isBound) {
            throw IllegalStateException("Property is already bound!")
        }

        binding = Binding.BidirectionalBinding(this, target)
    }

    /**
     * Unbind the current binding. Both properties will keep the current state but will no longer listen to each other.
     *
     * @throws IllegalStateException if this property is already unbound.
     */
    fun unbind() {
        if (!isBound) {
            throw IllegalStateException("Property is already bound!")
        }

        binding.unbind()
        binding = Binding.Unbound()
    }
}
