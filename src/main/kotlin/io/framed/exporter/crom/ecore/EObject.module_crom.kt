@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.exporter.crom.ecore

import io.framed.export.ecore.*

external interface EObject : Notifier {
    fun eIsSet(feature: EStructuralFeature): Boolean
    fun eContainmentFeature(): EReference
    fun eIsProxy(): Boolean
    fun eContainingFeature(): EStructuralFeature
    fun eClass(): EClass
    fun eContainer(): EObject
    fun eInvoke(operation: EOperation, arguments_: EList<Any>): Any
    fun eSet(feature: EStructuralFeature, newValue: Any)
    fun eResource(): Resource
    fun eContents(): EList<EObject>
    fun eCrossReferences(): EList<EObject>
    fun eAllContents(): TreeIterator<EObject>
    fun eGet(vararg args: Any): Any
    fun eGet_EStructuralFeature(feature: EStructuralFeature): Any
    fun eGet_EStructuralFeature_boolean(feature: EStructuralFeature, resolve: Boolean): Any
    fun eUnset(feature: EStructuralFeature)
}