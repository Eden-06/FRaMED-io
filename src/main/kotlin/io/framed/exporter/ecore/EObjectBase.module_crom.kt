@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.exporter.ecore

abstract external class EObjectBase : BasicEObjectImpl, EObject {
    override fun eIsSet(feature: EStructuralFeature): Boolean
    override fun eContainmentFeature(): EReference
    override fun eContainingFeature(): EStructuralFeature
    override fun eIsProxy(): Boolean
    override fun eClass(): EClass
    override fun eContainer(): EObject
    override fun eInvoke(operation: EOperation, arguments_: EList<Any>): Any
    override fun eSet(feature: EStructuralFeature, newValue: Any)
    override fun eContents(): EList<EObject>
    override fun eResource(): Resource
    override fun eCrossReferences(): EList<EObject>
    override fun eAllContents(): TreeIterator<EObject>
    override fun eGet(vararg args: Any): Any
    override fun eGet_EStructuralFeature_boolean(feature: EStructuralFeature, resolve: Boolean): Any
    override fun eGet_EStructuralFeature(feature: EStructuralFeature): Any
    override fun eUnset(feature: EStructuralFeature)
    override fun eStaticClass(): EClass
    override fun eGet_number_boolean_boolean(featureID: Number, resolve: Boolean, coreType: Boolean): Any
    override fun eSet_number_any(featureID: Number, newValue: Any)

    companion object {
        var eStaticClass: EClass
    }
}