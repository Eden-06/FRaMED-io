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