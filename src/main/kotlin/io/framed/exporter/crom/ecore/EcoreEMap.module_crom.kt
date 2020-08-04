@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.exporter.crom.ecore

open external class EcoreEMap<K, V>(entryEClass: EClass, entryClass: Function<*>, owner: InternalEObject, featureID: Number) : EMap<K, V> {
    open var entryEClass: EClass
    open var entryClass: Function<*>
    open var hashmap: Any
    override fun get(key: Any): V
    override fun put(key: K, value: V): V
    open fun set(value: Any)
    override fun containsKey(key: Any): Boolean
    open fun putAll(map: Any)
    open fun isEmpty(): Boolean
}