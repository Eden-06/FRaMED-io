@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.export.crom

import io.framed.export.ecore.NotificationChain

abstract external class NaturalTypeBase : RigidTypeImpl, NaturalType {
    open var _tr_extends: Any

    open fun basicSetTr_extends(newobj: NaturalType, msgs: NotificationChain): NotificationChain
companion object {
        var eStaticClass: Any
    }
}