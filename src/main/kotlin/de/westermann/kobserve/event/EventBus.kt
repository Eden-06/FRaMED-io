package de.westermann.kobserve.event

import kotlin.reflect.KClass

open class EventBus {

    private val subscribers: MutableMap<KClass<*>, EventHandler<*>> = mutableMapOf()

    fun <E : Any> handler(type: KClass<E>): EventHandler<E> {
        @Suppress("UNCHECKED_CAST")
        return subscribers.getOrPut(type) {
            EventHandler<E>()
        } as EventHandler<E>
    }

    inline fun <reified E : Any> handler() = handler(E::class)

    fun <E : Any> subscribe(type: KClass<E>, listener: (E) -> Unit): (E) -> Unit {
        handler(type) += listener
        return listener
    }

    inline fun <reified E : Any> subscribe(noinline listener: (E) -> Unit): (E) -> Unit {
        return subscribe(E::class, listener)
    }

    fun <E : Any> subscribeReference(type: KClass<E>, listener: (E) -> Unit): EventListener<E> {
        return handler(type).reference(listener)
    }

    inline fun <reified E : Any> subscribeReference(noinline listener: (E) -> Unit): EventListener<E> {
        return subscribeReference(E::class, listener)
    }

    fun <E : Any> unsubscribe(type: KClass<E>, listener: (E) -> Unit) {
        handler(type) -= listener
    }

    inline fun <reified E : Any> unsubscribe(noinline listener: (E) -> Unit) {
        unsubscribe(E::class, listener)
    }

    fun <E : Any> emit(event: E) {
        for ((type, handler) in subscribers) {
            if (type.isInstance(event)) {
                @Suppress("UNCHECKED_CAST")
                (handler as EventHandler<E>).emit(event)
            }
        }
    }
}

object GlobalEventBus : EventBus()

inline fun <reified E : Any> subscribe(noinline listener: (E) -> Unit): (E) -> Unit {
    return GlobalEventBus.subscribe(listener)
}

inline fun <reified E : Any> subscribeReference(noinline listener: (E) -> Unit): EventListener<E> {
    return GlobalEventBus.subscribeReference(listener)
}

inline fun <reified E : Any> unsubscribe(noinline listener: (E) -> Unit) {
    GlobalEventBus.unsubscribe(listener)
}

fun <E : Any> emit(event: E) {
    GlobalEventBus.emit(event)
}