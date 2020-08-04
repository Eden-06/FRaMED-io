@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.exporter.crom.ecore

external interface NotifyingList<T> : EList<T> {
    fun isNotificationRequired(): Boolean
    fun hasInverse(): Boolean
    fun dispatchNotification(notification: Notification)
    fun basicAdd(item: T, notifications: NotificationChain): NotificationChain
    fun basicRemove(item: T, notifications: NotificationChain): NotificationChain
    fun inverseAdd(item: T, notifications: NotificationChain): NotificationChain
    fun inverseRemove(item: T, notifications: NotificationChain): NotificationChain
}