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

external open class OrderedSet<T> : AbstractCollection<T> {
    override var isUnique: () -> Boolean
    open var at: (index: Number) -> T
    open var select: (lambda: (element: T) -> Boolean) -> OrderedSet<T>
    open var collect: (lambda: (element: T) -> T2) -> OrderedSet<T2>
    open var collect2: (lambda: (element: T) -> T2) -> OrderedSet<T2>
}