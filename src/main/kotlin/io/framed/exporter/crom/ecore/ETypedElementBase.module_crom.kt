@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.exporter.crom.ecore

abstract external class ETypedElementBase : ENamedElementImpl, ETypedElement {
    open var _ordered: Any
    open var _unique: Any
    open var _lowerBound: Any
    open var _upperBound: Any
    open var _eType: Any
    open var _eGenericType: Any
    override fun eStaticClass(): EClass
    open fun basicSetEGenericType(newobj: EGenericType, msgs: NotificationChain): NotificationChain
    open fun basicSetEType(newobj: EClassifier, msgs: NotificationChain): NotificationChain
    override fun eGet_number_boolean_boolean(featureID: Number, resolve: Boolean, coreType: Boolean): Any
    override fun eSet_number_any(featureID: Number, newValue: Any)

    companion object {
        var eStaticClass: EClass
    }
}