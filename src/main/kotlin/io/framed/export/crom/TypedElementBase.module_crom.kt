@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.export.crom

import io.framed.export.ecore.BasicEObjectImpl
import io.framed.export.ecore.NotificationChain

abstract external class TypedElementBase : BasicEObjectImpl, TypedElement {
    open var _name: Any
    open var _type: Any

    open fun basicSetType(newobj: RigidType, msgs: NotificationChain): NotificationChain
companion object {
        var eStaticClass: Any
    }
}