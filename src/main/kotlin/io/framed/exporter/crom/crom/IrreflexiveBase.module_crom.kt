@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.exporter.crom.crom

abstract external class IrreflexiveBase : IntraRelationshipConstraintImpl, Irreflexive {

    companion object {
        var eStaticClass: Any
    }
}