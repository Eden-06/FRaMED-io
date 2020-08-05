@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.exporter.crom

import io.framed.exporter.ecore.BasicEObjectImpl
import io.framed.exporter.ecore.InternalEObject
import io.framed.exporter.ecore.NotificationChain

abstract external class PartBase : BasicEObjectImpl, Part {
    open var _lower: Any
    open var _upper: Any
    open var _role: Any

    open fun eInverseAdd(otherEnd: InternalEObject, featureID: Number, msgs: NotificationChain): NotificationChain
    open fun eInverseRemove(otherEnd: InternalEObject, featureID: Number, msgs: NotificationChain): NotificationChain
    open fun basicSetRole(newobj: AbstractRole, msgs: NotificationChain): NotificationChain
    open fun basicSetWhole(newobj: CompartmentType, msgs: NotificationChain): NotificationChain
companion object {
        var eStaticClass: Any
    }
}