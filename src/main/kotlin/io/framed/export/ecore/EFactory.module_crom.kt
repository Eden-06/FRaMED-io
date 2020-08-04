@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.export.ecore

external interface EFactory : EModelElement {
    var ePackage: EPackage
    fun createFromString(eDataType: EDataType, literalValue: String): Any
    fun create(eClass: EClass): EObject
    fun convertToString(eDataType: EDataType, instanceValue: Any): String
}