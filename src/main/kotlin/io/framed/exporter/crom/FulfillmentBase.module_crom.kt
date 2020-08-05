@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.exporter.crom

import io.framed.exporter.ecore.NotificationChain

abstract external class FulfillmentBase : RelationImpl, Fulfillment {
    open var _filled: Any
    open var _filler: Any
    open fun basicSetFiller(newobj: Type, msgs: NotificationChain): NotificationChain
    open fun basicSetFilled(newobj: AbstractRole, msgs: NotificationChain): NotificationChain

    companion object {
        var eStaticClass: Any
    }
}