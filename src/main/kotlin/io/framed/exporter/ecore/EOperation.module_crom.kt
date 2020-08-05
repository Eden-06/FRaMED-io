@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.exporter.ecore

external interface EOperation : ETypedElement {
    var eContainingClass: EClass
    var eTypeParameters: OrderedSet<ETypeParameter>
    var eParameters: OrderedSet<EParameter>
    var eExceptions: OrderedSet<EClassifier>
    var eGenericExceptions: OrderedSet<EGenericType>
    fun isOverrideOf(someOperation: EOperation): Boolean
    fun getOperationID(): Number
}