@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.export.ecore

abstract external class OrderedSet<T> : AbstractCollection<T> {
    override var isUnique: () -> Boolean
    open var at: (index: Number) -> T
    open var select: (lambda: (element: T) -> Boolean) -> OrderedSet<T>
    open var collect: (lambda: (element: T) -> Any) -> OrderedSet<Any>
    open var collect2: (lambda: (element: T) -> Any) -> OrderedSet<Any>
}