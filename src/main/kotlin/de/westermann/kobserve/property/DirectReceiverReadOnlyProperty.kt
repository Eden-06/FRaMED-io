package de.westermann.kobserve.property

import de.westermann.kobserve.event.EventHandler
import de.westermann.kobserve.ReadOnlyProperty
import kotlin.reflect.KProperty0

open class DirectReceiverReadOnlyProperty<T>(
    private val attribute: KProperty0<T>
) : ReadOnlyProperty<T> {

    protected open var internal: T = attribute.get()

    override fun get(): T {
        val newValue = attribute.get()
        if (newValue != internal) {
            internal = newValue
            onChange.emit(Unit)
        }
        return newValue
    }

    override fun invalidate() {
        val newValue = attribute.get()
        if (newValue != internal) {
            internal = newValue
            onChange.emit(Unit)
        }
    }

    override val onChange = EventHandler<Unit>()

}

fun <T> property(attribute: KProperty0<T>): ReadOnlyProperty<T> =
    DirectReceiverReadOnlyProperty(attribute)

fun <T> KProperty0<T>.observe(): ReadOnlyProperty<T> = DirectReceiverReadOnlyProperty(this)
