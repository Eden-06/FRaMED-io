@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.exporter.crom.ecore

abstract external class EStructuralFeatureBase : ETypedElementImpl, EStructuralFeature {
    open var _changeable: Any
    open var _volatile: Any
    open var _transient: Any
    open var _defaultValueLiteral: Any
    open var _unsettable: Any
    open var _derived: Any
    override fun getContainerClass(): Function<*>
    override fun getFeatureID(): Number
    override fun eStaticClass(): EClass
    override fun eInverseAdd(otherEnd: InternalEObject, featureID: Number, msgs: NotificationChain): NotificationChain
    override fun eInverseRemove(otherEnd: InternalEObject, featureID: Number, msgs: NotificationChain): NotificationChain
    open fun basicSetEContainingClass(newobj: EClass, msgs: NotificationChain): NotificationChain
    override fun eGet_number_boolean_boolean(featureID: Number, resolve: Boolean, coreType: Boolean): Any
    override fun eSet_number_any(featureID: Number, newValue: Any)

    companion object {
        var eStaticClass: EClass
    }
}