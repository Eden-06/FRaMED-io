@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.exporter.ecore

external interface EMap<K, V> {
    fun get(key: Any): V
    fun put(key: K, value: V): V
    fun containsKey(key: Any): Boolean
}