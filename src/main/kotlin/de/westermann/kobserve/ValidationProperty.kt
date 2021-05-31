package de.westermann.kobserve

/**
 * Represents a read and write property of type 'T'.
 * A set call performs a validation of the given value and sets the valid flag. The given value is only accepted if it
 * is valid.
 */
interface ValidationProperty<T> : Property<T> {

    /**
     * Observable value of the valid state of the last set operation.
     */
    val validProperty: ReadOnlyProperty<Boolean>

    /**
     * Returns the valid state of the last set operation.
     */
    val valid: Boolean
}
