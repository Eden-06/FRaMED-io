@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.exporter.crom

import io.framed.exporter.ecore.NotificationChain

abstract external class CompartmentTypeBase : RigidTypeImpl, CompartmentType {
    open var _parts: Any
    open var _relationships: Any
    open var _constraints: Any
    open var _tr_extends: Any
    open var _contains: Any
    open var _fulfillments: Any
    open fun basicSetTr_extends(newobj: CompartmentType, msgs: NotificationChain): NotificationChain


    companion object {
        var eStaticClass: Any
    }
}