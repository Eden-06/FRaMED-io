@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.export.ecore

abstract external class EStructuralFeatureImpl : EStructuralFeatureBase {
    open var featureID: Number
    open fun setFeatureID(id: Number)
    override fun getFeatureID(): Number
    open var containerClass_: Any
    open var setContainerClass: (f: Function<*>) -> Unit
    override fun getContainerClass(): Function<*>
}