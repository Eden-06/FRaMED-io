@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.exporter.crom.crom

import io.framed.exporter.crom.ecore.InternalEObject
import io.framed.exporter.crom.ecore.NotificationChain

abstract external class OperationBase : TypedElementImpl, Operation {
    open var _operation: Any
    open var _params: Any

    open fun eInverseAdd(otherEnd: InternalEObject, featureID: Number, msgs: NotificationChain): NotificationChain
    open fun eInverseRemove(otherEnd: InternalEObject, featureID: Number, msgs: NotificationChain): NotificationChain
    open fun basicSetOwner(newobj: Type, msgs: NotificationChain): NotificationChain
companion object {
        var eStaticClass: Any
    }
}