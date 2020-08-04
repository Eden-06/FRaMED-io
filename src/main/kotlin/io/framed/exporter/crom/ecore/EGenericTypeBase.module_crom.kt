@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.exporter.crom.ecore

abstract external class EGenericTypeBase : BasicEObjectImpl, EGenericType {
    open var _eUpperBound: Any
    open var _eTypeArguments: Any
    open var _eLowerBound: Any
    open var _eTypeParameter: Any
    open var _eClassifier: Any
    override fun isInstance(obj: Any): Boolean
    override fun eStaticClass(): EClass
    open fun basicSetETypeParameter(newobj: ETypeParameter, msgs: NotificationChain): NotificationChain
    open fun basicSetELowerBound(newobj: EGenericType, msgs: NotificationChain): NotificationChain
    open fun basicSetEClassifier(newobj: EClassifier, msgs: NotificationChain): NotificationChain
    open fun basicSetEUpperBound(newobj: EGenericType, msgs: NotificationChain): NotificationChain
    override fun eGet_number_boolean_boolean(featureID: Number, resolve: Boolean, coreType: Boolean): Any
    override fun eSet_number_any(featureID: Number, newValue: Any)

    companion object {
        var eStaticClass: EClass
    }
}