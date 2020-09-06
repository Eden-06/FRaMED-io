@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.exporter.ecore

external class Set<T> : AbstractCollection<T> {
    var isUnique: () -> Boolean
    open var select: (lambda: (element: T) -> Boolean) -> Set<T>
    open var asSequence: () -> Set<T>
}