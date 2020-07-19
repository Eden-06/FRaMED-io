@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

import kotlin.js.*
import kotlin.js.Json
import org.khronos.webgl.*
import org.w3c.dom.*
import org.w3c.dom.events.*
import org.w3c.dom.parsing.*
import org.w3c.dom.svg.*
import org.w3c.dom.url.*
import org.w3c.fetch.*
import org.w3c.files.*
import org.w3c.notifications.*
import org.w3c.performance.*
import org.w3c.workers.*
import org.w3c.xhr.*

external open class EcoreEMap<K, V>(entryEClass: EClass, entryClass: Function<*>, owner: InternalEObject, featureID: Number) : EMap<K, V> {
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