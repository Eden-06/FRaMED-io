@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.exporter.ecore

abstract external class EReferenceBase : EStructuralFeatureImpl, EReference {
    open var _containment: Any
    open var _resolveProxies: Any
    open var _eOpposite: Any
    open var _eKeys: Any
    override fun eStaticClass(): EClass
    open fun basicSetEOpposite(newobj: EReference, msgs: NotificationChain): NotificationChain
    override fun eGet_number_boolean_boolean(featureID: Number, resolve: Boolean, coreType: Boolean): Any
    override fun eSet_number_any(featureID: Number, newValue: Any)

    companion object {
        var eStaticClass: EClass
    }
}