@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.exporter.crom

import io.framed.exporter.ecore.NotificationChain

abstract external class RoleConstraintBase : ConstraintImpl, RoleConstraint {
    open var _first: Any
    open var _second: Any

    open fun basicSetFirst(newobj: AbstractRole, msgs: NotificationChain): NotificationChain
    open fun basicSetSecond(newobj: AbstractRole, msgs: NotificationChain): NotificationChain
companion object {
        var eStaticClass: Any
    }
}