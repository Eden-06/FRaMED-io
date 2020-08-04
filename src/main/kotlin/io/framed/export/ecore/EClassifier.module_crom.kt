@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.export.ecore

external interface EClassifier : ENamedElement {
    var instanceClassName: String
    var instanceClass: Function<*>
    var defaultValue: Any
    var instanceTypeName: String
    var ePackage: EPackage
    var eTypeParameters: OrderedSet<ETypeParameter>
    fun isInstance(obj: Any): Boolean
    fun getClassifierID(): Number
}