@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.export.ecore

external interface EcoreFactory : EFactory {
    fun createEAttribute(): EAttribute
    fun createEAnnotation(): EAnnotation
    fun createEClass(): EClass
    fun createEClassifier(): EClassifier
    fun createEDataType(): EDataType
    fun createEEnum(): EEnum
    fun createEEnumLiteral(): EEnumLiteral
    fun createEFactory(): EFactory
    fun createEModelElement(): EModelElement
    fun createENamedElement(): ENamedElement
    fun createEObject(): EObject
    fun createEOperation(): EOperation
    fun createEPackage(): EPackage
    fun createEParameter(): EParameter
    fun createEReference(): EReference
    fun createEStructuralFeature(): EStructuralFeature
    fun createETypedElement(): ETypedElement
    fun createEStringToStringMapEntry(): EStringToStringMapEntry
    fun createEGenericType(): EGenericType
    fun createETypeParameter(): ETypeParameter
}