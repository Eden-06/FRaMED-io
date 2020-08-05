@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.exporter.ecore

//abstract external class io.framed.exporter.ecore.AbstractCollection<T>(owner: io.framed.exporter.ecore.InternalEObject = definedExternally, featureId: Number = definedExternally, oppositeFeatureId: Number = definedExternally) : kotlin.collections.io.framed.exporter.ecore.ArrayList<T>, io.framed.exporter.ecore.EcoreEList<T>, kotlin.collections.io.framed.exporter.ecore.Collection<T> {
abstract external class AbstractCollection<T>(owner: InternalEObject = definedExternally, featureId: Number = definedExternally, oppositeFeatureId: Number = definedExternally) : EcoreEList<T>, Collection<T> {
    open var owner: Any
    open var featureId: Any
    open var oppositeFeatureId: Any
    open var createNotification: (eventType: Number, oldValue: Any, newValue: Any, index: Number, wasSet: Boolean) -> NotificationImpl
    open var isUnique: () -> Boolean
    open var add: (element: T) -> Boolean
    open var addAll: (collection: Array<T>) -> Boolean
    open var remove: (item: T) -> Boolean
    open var remove_: (element: T) -> T
    open var isNotificationRequired: () -> Boolean
    open var basicAdd: (element: T, notifications: NotificationChain) -> NotificationChain
    open var basicRemove: (element: T, notifications: NotificationChain) -> NotificationChain
    open var inverseAdd: (element: T, notifications: NotificationChain) -> NotificationChain
    open var inverseRemove: (element: T, notifications: NotificationChain) -> NotificationChain
    open var hasNavigableInverse: () -> Boolean
    open var hasInstanceClass: () -> Boolean
    open var hasInverse: () -> Boolean
    open var getEStructuralFeature: () -> EStructuralFeature
    open var getInverseEReference: () -> EReference
    open var getInverseFeatureClass: () -> Function<*>
    open var dispatchNotification: (notification: Notification) -> Unit
    open var addUnique: (element: T) -> Unit
    open var sortedBy: (lambda: (T: Any) -> Any) -> Unit
    open var equals: (c: Collection<T>) -> Boolean
    open var notEquals: (c: Collection<T>) -> Boolean
    open var size: () -> Number
    open var includes___: (element: T) -> Boolean
    open var excludes: (element: T) -> Boolean
    open var count: (element: T) -> Number
    open var includesAll: (c: Collection<T>) -> Boolean
    open var excludesAll: (c: Collection<T>) -> Boolean
    open var isEmpty: () -> Boolean
    open var notEmpty: () -> Boolean
    open var max: () -> T
    open var min: () -> T
    open var sum: () -> T
    open var any: (lambda: (element: T) -> Boolean) -> T
    open var exists: (lambda: (element: T) -> Boolean) -> Boolean
    open var forAll: (lambda: (element: T) -> Boolean) -> Boolean

    companion object {
        var EOPPOSITE_FEATURE_BASE: Number
        var NO_FEATURE: Number
    }
}