@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.exporter.crom

import io.framed.exporter.ecore.NotificationChain

abstract external class RoleTypeBase : AntiRigidTypeImpl, RoleType {
    open var _tr_extends: Any

    open fun basicSetTr_extends(newobj: RoleType, msgs: NotificationChain): NotificationChain
override fun eBaseStructuralFeatureID(derivedFeatureID: Number, baseClass: Function<*>): Number
    override fun eDerivedStructuralFeatureID_number_Function(baseFeatureID: Number, baseClass: Function<*>): Number

    companion object {
        var eStaticClass: Any
    }
}