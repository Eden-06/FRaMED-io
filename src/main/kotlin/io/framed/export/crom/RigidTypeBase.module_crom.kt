@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.export.crom

abstract external class RigidTypeBase : TypeImpl, RigidType {

override fun eBaseStructuralFeatureID(derivedFeatureID: Number, baseClass: Function<*>): Number
    override fun eDerivedStructuralFeatureID_number_Function(baseFeatureID: Number, baseClass: Function<*>): Number

    companion object {
        var eStaticClass: Any
    }
}