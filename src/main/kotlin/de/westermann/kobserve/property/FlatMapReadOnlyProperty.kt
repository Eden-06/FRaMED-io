package de.westermann.kobserve.property

import de.westermann.kobserve.event.EventHandler
import de.westermann.kobserve.event.EventListener
import de.westermann.kobserve.ReadOnlyProperty

open class FlatMapReadOnlyProperty<R, T>(
    private val transform: (R) -> ReadOnlyProperty<T>,
    protected val receiver: ReadOnlyProperty<R>
) : ReadOnlyProperty<T> {

    override fun get(): T {
        return transform(receiver.value).value
    }

    final override val onChange = EventHandler<Unit>()

    private lateinit var reference: EventListener<Unit>

    private fun updateReference() {
        if (this::reference.isInitialized && reference.isAttached) {
            reference.detach()
        }

        reference = transform(receiver.value).onChange.reference {
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

fun <R, T> ReadOnlyProperty<R>.flatMapReadOnlyBinding(transform: (R) -> ReadOnlyProperty<T>): ReadOnlyProperty<T> =
    FlatMapReadOnlyProperty(transform, this)

fun <T> ReadOnlyProperty<ReadOnlyProperty<T>>.flatten(): ReadOnlyProperty<T> =
    FlatMapReadOnlyProperty({ it }, this)

fun <T> ReadOnlyProperty<ReadOnlyProperty<T>>.flattenReadOnly(): ReadOnlyProperty<T> =
    FlatMapReadOnlyProperty({ it }, this)
