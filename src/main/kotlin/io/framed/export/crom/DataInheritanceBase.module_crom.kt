@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.export.crom

import io.framed.export.ecore.NotificationChain

abstract external class DataInheritanceBase : InheritanceImpl, DataInheritance {
    open var _super: Any
    open var _sub: Any
    open fun basicSetSuper(newobj: DataType, msgs: NotificationChain): NotificationChain
    open fun basicSetSub(newobj: DataType, msgs: NotificationChain): NotificationChain

    companion object {
        var eStaticClass: Any
    }
}