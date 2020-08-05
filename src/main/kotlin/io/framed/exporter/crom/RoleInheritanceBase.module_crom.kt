@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.exporter.crom

import io.framed.exporter.ecore.NotificationChain

abstract external class RoleInheritanceBase : InheritanceImpl, RoleInheritance {
    open var _super: Any
    open var _sub: Any

    open fun basicSetSuper(newobj: RoleType, msgs: NotificationChain): NotificationChain
    open fun basicSetSub(newobj: RoleType, msgs: NotificationChain): NotificationChain
companion object {
        var eStaticClass: Any
    }
}