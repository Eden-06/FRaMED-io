@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.exporter.crom.ecore

external interface EStructuralFeature : ETypedElement {
    var changeable: Boolean
    var volatile: Boolean
    var transient: Boolean
    var defaultValueLiteral: String
    var defaultValue: Any
    var unsettable: Boolean
    var derived: Boolean
    var eContainingClass: EClass
    fun getContainerClass(): Function<*>
    fun getFeatureID(): Number
}