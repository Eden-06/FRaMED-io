@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.export.crom

abstract external class ComplexConstraintBase : ConstraintImpl, ComplexConstraint {
    open var _expression: Any
    open var _targets: Any

    companion object {
        var eStaticClass: Any
    }
}