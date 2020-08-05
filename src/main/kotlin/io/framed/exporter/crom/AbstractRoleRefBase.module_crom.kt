@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.exporter.crom

import io.framed.exporter.ecore.BasicEObjectImpl
import io.framed.exporter.ecore.NotificationChain

abstract external class AbstractRoleRefBase : BasicEObjectImpl, AbstractRoleRef {
    open var _ref: Any
    open fun basicSetRef(newobj: AbstractRole, msgs: NotificationChain): NotificationChain

    companion object {
        var eStaticClass: Any
    }
}