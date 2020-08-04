@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.export.crom

import io.framed.export.ecore.InternalEObject
import io.framed.export.ecore.NotificationChain

abstract external class AttributeBase : TypedElementImpl, Attribute {
    open fun eInverseAdd(otherEnd: InternalEObject, featureID: Number, msgs: NotificationChain): NotificationChain
    open fun eInverseRemove(otherEnd: InternalEObject, featureID: Number, msgs: NotificationChain): NotificationChain
    open fun basicSetOwner(newobj: Type, msgs: NotificationChain): NotificationChain

    companion object {
        var eStaticClass: Any
    }
}