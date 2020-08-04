@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.exporter.crom.ecore

external interface EAnnotation : EModelElement {
    var source: String
    var details: EMap<String, String>
    var eModelElement: EModelElement
    var contents: OrderedSet<EObject>
    var references: OrderedSet<EObject>
}