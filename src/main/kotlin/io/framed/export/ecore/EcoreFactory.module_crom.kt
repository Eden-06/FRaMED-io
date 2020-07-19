@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

import kotlin.js.*
import kotlin.js.Json
import org.khronos.webgl.*
import org.w3c.dom.*
import org.w3c.dom.events.*
import org.w3c.dom.parsing.*
import org.w3c.dom.svg.*
import org.w3c.dom.url.*
import org.w3c.fetch.*
import org.w3c.files.*
import org.w3c.notifications.*
import org.w3c.performance.*
import org.w3c.workers.*
import org.w3c.xhr.*

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