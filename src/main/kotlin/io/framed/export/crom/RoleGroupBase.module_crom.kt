@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.export.crom

import io.framed.export.ecore.BasicEObjectImpl

abstract external class RoleGroupBase : BasicEObjectImpl, RoleGroup {
    open var _name: Any
    open var _lower: Any
    open var _upper: Any
    open var _incoming: Any
    open var _outgoing: Any
    open var _elements: Any

override fun eBaseStructuralFeatureID(derivedFeatureID: Number, baseClass: Function<*>): Number
    override fun eDerivedStructuralFeatureID_number_Function(baseFeatureID: Number, baseClass: Function<*>): Number

    companion object {
        var eStaticClass: Any
    }
}