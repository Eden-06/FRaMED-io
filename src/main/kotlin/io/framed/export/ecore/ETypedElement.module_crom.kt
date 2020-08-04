@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.export.ecore

external interface ETypedElement : ENamedElement {
    var ordered: Boolean
    var unique: Boolean
    var lowerBound: Number
    var upperBound: Number
    var many: Boolean
    var required: Boolean
    var eType: EClassifier
    var eGenericType: EGenericType
}