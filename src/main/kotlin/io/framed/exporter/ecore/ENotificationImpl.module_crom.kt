@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.exporter.ecore

abstract external class ENotificationImpl(notifier: InternalEObject, eventType: Number, featureID: Number, oldValue: Any, newValue: Any, position: Number = definedExternally) : NotificationImpl {
    open var notifier: InternalEObject
    open var featureID: Number
    open var feature: EStructuralFeature
    open var getFeature: () -> Any
    override fun getNotifier(): Any
}