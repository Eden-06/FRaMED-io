@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.exporter.crom.ecore

abstract external class BasicNotifierImpl : Notifier {
    open var _eAdapters: Any
    open var eAdapters: () -> Array<Adapter>
    open var eBasicAdapters: () -> Array<Adapter>
    open var eNotify: (notification: Notification) -> Unit
    open var eNotificationRequired: () -> Boolean
}