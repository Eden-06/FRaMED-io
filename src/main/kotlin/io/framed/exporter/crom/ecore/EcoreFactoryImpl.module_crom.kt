@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.exporter.crom.ecore

import kotlin.js.Date

abstract external class EcoreFactoryImpl : EFactoryImpl, EcoreFactory {
    open var createEAttribute: () -> EAttribute
    open var createEAnnotation: () -> EAnnotation
    open var createEClass: () -> EClass
    open var createEClassifier: () -> EClassifier
    open var createEDataType: () -> EDataType
    open var createEEnum: () -> EEnum
    open var createEEnumLiteral: () -> EEnumLiteral
    open var createEFactory: () -> EFactory
    open var createEModelElement: () -> EModelElement
    open var createENamedElement: () -> ENamedElement
    open var createEObject: () -> EObject
    open var createEOperation: () -> EOperation
    open var createEPackage: () -> EPackage
    open var createEParameter: () -> EParameter
    open var createEReference: () -> EReference
    open var createEStructuralFeature: () -> EStructuralFeature
    open var createETypedElement: () -> ETypedElement
    open var createEStringToStringMapEntry: () -> EStringToStringMapEntry
    open var createEGenericType: () -> EGenericType
    open var createETypeParameter: () -> ETypeParameter
    override fun create(eClass: EClass): EObject
    override fun createFromString(eDataType: EDataType, initialValue: String): Any
    override fun convertToString(eDataType: EDataType, instanceValue: Any): String
    open fun createEBigDecimalFromString(eDataType: EDataType, initialValue: String): Number
    open fun convertEBigDecimalToString(eDataType: EDataType, instanceValue: Any): String
    open fun createEBigIntegerFromString(eDataType: EDataType, initialValue: String): Number
    open fun convertEBigIntegerToString(eDataType: EDataType, instanceValue: Any): String
    open fun createEBooleanFromString(eDataType: EDataType, initialValue: String): Boolean
    open fun convertEBooleanToString(eDataType: EDataType, instanceValue: Any): String
    open fun createEBooleanObjectFromString(eDataType: EDataType, initialValue: String): Boolean
    open fun convertEBooleanObjectToString(eDataType: EDataType, instanceValue: Any): String
    open fun createEByteFromString(eDataType: EDataType, initialValue: String): Number
    open fun convertEByteToString(eDataType: EDataType, instanceValue: Any): String
    open fun createEByteArrayFromString(eDataType: EDataType, initialValue: String): Array<Number>
    open fun convertEByteArrayToString(eDataType: EDataType, instanceValue: Any): String
    open fun createEByteObjectFromString(eDataType: EDataType, initialValue: String): Number
    open fun convertEByteObjectToString(eDataType: EDataType, instanceValue: Any): String
    open fun createECharFromString(eDataType: EDataType, initialValue: String): String
    open fun convertECharToString(eDataType: EDataType, instanceValue: Any): String
    open fun createECharacterObjectFromString(eDataType: EDataType, initialValue: String): String
    open fun convertECharacterObjectToString(eDataType: EDataType, instanceValue: Any): String
    open fun createEDateFromString(eDataType: EDataType, initialValue: String): Date
    open fun convertEDateToString(eDataType: EDataType, instanceValue: Any): String
    open fun createEDoubleFromString(eDataType: EDataType, initialValue: String): Number
    open fun convertEDoubleToString(eDataType: EDataType, instanceValue: Any): String
    open fun createEDoubleObjectFromString(eDataType: EDataType, initialValue: String): Number
    open fun convertEDoubleObjectToString(eDataType: EDataType, instanceValue: Any): String
    open fun createEFloatFromString(eDataType: EDataType, initialValue: String): Number
    open fun convertEFloatToString(eDataType: EDataType, instanceValue: Any): String
    open fun createEFloatObjectFromString(eDataType: EDataType, initialValue: String): Number
    open fun convertEFloatObjectToString(eDataType: EDataType, instanceValue: Any): String
    open fun createEIntFromString(eDataType: EDataType, initialValue: String): Number
    open fun convertEIntToString(eDataType: EDataType, instanceValue: Any): String
    open fun createEIntegerObjectFromString(eDataType: EDataType, initialValue: String): Number
    open fun convertEIntegerObjectToString(eDataType: EDataType, instanceValue: Any): String
    open fun createEJavaClassFromString(eDataType: EDataType, initialValue: String): Function<*>
    open fun convertEJavaClassToString(eDataType: EDataType, instanceValue: Any): String
    open fun createEJavaObjectFromString(eDataType: EDataType, initialValue: String): Any
    open fun convertEJavaObjectToString(eDataType: EDataType, instanceValue: Any): String
    open fun createELongFromString(eDataType: EDataType, initialValue: String): Number
    open fun convertELongToString(eDataType: EDataType, instanceValue: Any): String
    open fun createELongObjectFromString(eDataType: EDataType, initialValue: String): Number
    open fun convertELongObjectToString(eDataType: EDataType, instanceValue: Any): String
    open fun createEShortFromString(eDataType: EDataType, initialValue: String): Number
    open fun convertEShortToString(eDataType: EDataType, instanceValue: Any): String
    open fun createEShortObjectFromString(eDataType: EDataType, initialValue: String): Number
    open fun convertEShortObjectToString(eDataType: EDataType, instanceValue: Any): String
    open fun createEStringFromString(eDataType: EDataType, initialValue: String): String
    open fun convertEStringToString(eDataType: EDataType, instanceValue: Any): String

    companion object {
        var eINSTANCE: EcoreFactory
        fun init(): EcoreFactory
    }
}