package io.framed.framework.util

import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty

/**
 * @author lars
 */
interface Property<T> {
    operator fun getValue(container: Any, property: KProperty<*>) = get()
    operator fun setValue(container: Any, property: KProperty<*>, value: T) = set(value)

    fun get(): T
    fun set(value: T): Validator.Result

    val editable: Boolean
    val onChange: EventHandler<Unit>
    fun fire() {
        onChange.fire(Unit)
    }
}

class ObjectProperty<T>(
        private val kProperty: KMutableProperty0<T>,
        private val validator: Validator<T>,
        override val editable: Boolean
) : Property<T> {

    override fun get(): T = kProperty.get()

    override fun set(value: T) = validator.validate(value).also { result ->
        if (result != Validator.Result.ERROR && value != kProperty.get()) {
            kProperty.set(value)
            fire()
        }
    }

    override val onChange = EventHandler<Unit>()
}

class ComplexProperty<T>(
        val properties: List<Property<*>>,
        val getter: () -> T,
        val setter: (T) -> Validator.Result,
        override val editable: Boolean
) : Property<T> {
    override fun get(): T = getter()

    override fun set(value: T) = setter(value).also { result ->
        if (result != Validator.Result.ERROR) {
            fire()
        }
    }

    override fun fire() {
        listen = false
        super.fire()
        listen = true
    }

    override val onChange = EventHandler<Unit>()

    var listen = true
    var propagate = true

    init {
        val emitChange = { _: Unit ->
            if (listen) {
                propagate = false
                onChange.fire(Unit)
                propagate = true
            }
        }
        properties.forEach { property ->
            property.onChange += emitChange
        }

        onChange { _ ->
            if (propagate) {
                properties.forEach {
                    it.fire()
                }
            }
        }
    }
}

fun <T> property(
        kProperty: KMutableProperty0<T>,
        validator: Validator<T> = TrueValidator(),
        editable: Boolean = true
) = ObjectProperty(kProperty, validator, editable)

fun <T> property(
        vararg properties: Property<*>,
        getter: () -> T,
        setter: ((T) -> Validator.Result)? = null
) = if (setter == null) {
    ComplexProperty(properties.toList(), getter, { throw NotImplementedError() }, false)
} else {
    ComplexProperty(properties.toList(), getter, setter, true)
}
