@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.export.ecore

external interface EModelElement : InternalEObject {
    var eAnnotations: OrderedSet<EAnnotation>
    fun getEAnnotation(source: String): EAnnotation
}