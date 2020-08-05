@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.exporter.crom

import io.framed.exporter.ecore.NotificationChain

abstract external class RelationshipBase : RelationImpl, Relationship {
    open var _name: Any
    open var _direction: Any
    open var _first: Any
    open var _second: Any
    open var _tr_constraints: Any

    open fun basicSetFirst(newobj: Place, msgs: NotificationChain): NotificationChain
    open fun basicSetSecond(newobj: Place, msgs: NotificationChain): NotificationChain
override fun eBaseStructuralFeatureID(derivedFeatureID: Number, baseClass: Function<*>): Number
    override fun eDerivedStructuralFeatureID_number_Function(baseFeatureID: Number, baseClass: Function<*>): Number

    companion object {
        var eStaticClass: Any
    }
}