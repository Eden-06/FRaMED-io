@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.exporter.crom.ecore

external interface EGenericType : InternalEObject {
    var eUpperBound: EGenericType
    var eTypeArguments: OrderedSet<EGenericType>
    var eRawType: EClassifier
    var eLowerBound: EGenericType
    var eTypeParameter: ETypeParameter
    var eClassifier: EClassifier
    fun isInstance(obj: Any): Boolean
}