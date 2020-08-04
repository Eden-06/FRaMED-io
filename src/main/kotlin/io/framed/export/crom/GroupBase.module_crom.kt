@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.export.crom

import io.framed.export.ecore.BasicEObjectImpl

abstract external class GroupBase : BasicEObjectImpl, Group {
    open var _name: Any
    open var _elements: Any
    open var _relations: Any

    companion object {
        var eStaticClass: Any
    }
}