@file:Suppress(
    "INTERFACE_WITH_SUPERCLASS",
    "OVERRIDING_FINAL_MEMBER",
    "RETURN_TYPE_MISMATCH_ON_OVERRIDE",
    "CONFLICTING_OVERLOADS"
)

package io.framed.exporter.ecore

//abstract external class io.framed.exporter.ecore.AbstractCollection<T>(owner: io.framed.exporter.ecore.InternalEObject = definedExternally, featureId: Number = definedExternally, oppositeFeatureId: Number = definedExternally) : kotlin.collections.io.framed.exporter.ecore.ArrayList<T>, io.framed.exporter.ecore.EcoreEList<T>, kotlin.collections.io.framed.exporter.ecore.Collection<T> {
abstract external class AbstractCollection<T>(
    owner: InternalEObject = definedExternally,
    featureId: Number = definedExternally,
    oppositeFeatureId: Number = definedExternally
) : EcoreEList<T>, Collection<T> {
    open var owner: Any
    open var featureId: Any
    open var oppositeFeatureId: Any
    fun createNotification(
        eventType: Number,
        oldValue: Any,
        newValue: Any,
        index: Number,
        wasSet: Boolean
    ): NotificationImpl

    fun isUnique(): Boolean
    override fun add(element: T): Boolean
    fun addAll(collection: Array<T>): Boolean
    override fun remove(item: T): Boolean
    fun remove_(element: T): T
    override fun isNotificationRequired(): Boolean
    override fun basicAdd(element: T, notifications: NotificationChain): NotificationChain
    override fun basicRemove(element: T, notifications: NotificationChain): NotificationChain
    override fun inverseAdd(element: T, notifications: NotificationChain): NotificationChain
    override fun inverseRemove(element: T, notifications: NotificationChain): NotificationChain
    override fun hasNavigableInverse(): Boolean
    override fun hasInstanceClass(): Boolean
    override fun hasInverse(): Boolean
    override fun getEStructuralFeature(): EStructuralFeature
    override fun getInverseEReference(): EReference
    override fun getInverseFeatureClass(): Function<*>
    override fun dispatchNotification(notification: Notification): Unit
    override fun addUnique(element: T): Unit
    fun sortedBy(lambda: (T: Any) -> Any): Unit
    override fun equals(c: Collection<T>): Boolean
    override fun notEquals(c: Collection<T>): Boolean
    override fun size(): Number
    fun includes___(element: T): Boolean
    override fun excludes(element: T): Boolean
    override fun count(element: T): Number
    override fun includesAll(c: Collection<T>): Boolean
    override fun excludesAll(c: Collection<T>): Boolean
    override fun isEmpty(): Boolean
    override fun notEmpty(): Boolean
    override fun max(): T
    override fun min(): T
    override fun sum(): T
    override fun any(lambda: (element: T) -> Boolean): T
    override fun exists(lambda: (element: T) -> Boolean): Boolean
    override fun forAll(lambda: (element: T) -> Boolean): Boolean

    companion object {
        var EOPPOSITE_FEATURE_BASE: Number
        var NO_FEATURE: Number
    }
}