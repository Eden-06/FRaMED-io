package de.westermann.kobserve.property

import de.westermann.kobserve.event.EventHandler
import de.westermann.kobserve.event.EventListener
import de.westermann.kobserve.ReadOnlyProperty
import kotlin.reflect.KProperty1

open class FlatReceiverReadOnlyProperty<R, T>(
    private val attribute: KProperty1<R, ReadOnlyProperty<T>>,
    protected val receiver: ReadOnlyProperty<R>
) : ReadOnlyProperty<T> {

    override fun get(): T {
        return attribute.get(receiver.value).value
    }

    final override val onChange = EventHandler<Unit>()

    private lateinit var reference: EventListener<Unit>

    private fun updateReference() {
        if (this::reference.isInitialized && reference.isAttached) {
            reference.detach()
        }

        reference = attribute.get(receiver.value).onChange.reference {
            onChange.emit(Unit)
        }
    }

    init {
        receiver.onChange {
            updateReference()
            onChange.emit(Unit)
        }
        updateReference()
    }
}

fun <R, T> ReadOnlyProperty<R>.flatMapBinding(attribute: KProperty1<R, ReadOnlyProperty<T>>): ReadOnlyProperty<T> =
    FlatReceiverReadOnlyProperty(attribute, this)
