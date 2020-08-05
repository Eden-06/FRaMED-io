@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.exporter.ecore

external interface Collection<T> {
    fun add(element: T): Boolean
    fun remove(element: T): Boolean
    fun equals(c: Collection<T>): Boolean
    fun notEquals(c: Collection<T>): Boolean
    fun size(): Number
    fun excludes(element: T): Boolean
    fun count(element: T): Number
    fun includesAll(c: Collection<T>): Boolean
    fun excludesAll(c: Collection<T>): Boolean
    fun isEmpty(): Boolean
    fun notEmpty(): Boolean
    fun max(): T
    fun min(): T
    fun sum(): T
    fun any(lambda: (element: T) -> Boolean): T
    fun exists(lambda: (element: T) -> Boolean): Boolean
    fun forAll(lambda: (element: T) -> Boolean): Boolean
}