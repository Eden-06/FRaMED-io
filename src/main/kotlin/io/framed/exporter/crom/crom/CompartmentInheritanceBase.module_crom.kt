@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.exporter.crom.crom

import io.framed.exporter.crom.ecore.NotificationChain

abstract external class CompartmentInheritanceBase : InheritanceImpl, CompartmentInheritance {
    open var _super: Any
    open var _sub: Any
    open fun basicSetSuper(newobj: CompartmentType, msgs: NotificationChain): NotificationChain
    open fun basicSetSub(newobj: CompartmentType, msgs: NotificationChain): NotificationChain

    companion object {
        var eStaticClass: Any
    }
}