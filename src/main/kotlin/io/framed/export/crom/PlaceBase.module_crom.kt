@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.export.crom

import io.framed.export.ecore.BasicEObjectImpl
import io.framed.export.ecore.NotificationChain

abstract external class PlaceBase : BasicEObjectImpl, Place {
    open var _lower: Any
    open var _upper: Any
    open var _holder: Any

    open fun basicSetHolder(newobj: RoleType, msgs: NotificationChain): NotificationChain
companion object {
        var eStaticClass: Any
    }
}