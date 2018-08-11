package io.framed.util

import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty

/**
 * @author lars
 */
class Property<T : Any>(
        private val kProperty: KMutableProperty0<T>
) {
    operator fun getValue(container: Any, property: KProperty<*>) = get()

    operator fun setValue(container: Any, property: KProperty<*>, value: T) = set(value)

    fun get(): T = kProperty.get()

    fun set(value: T) {
        kProperty.set(value)
        onChange.fire(value)
    }

    val onChange = EventHandler<T>()
}