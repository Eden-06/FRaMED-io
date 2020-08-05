@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.exporter.crom

external interface Relationship : Relation, NamedElement {
    var direction: Any
    var first: Any
    var second: Any
    var tr_constraints: Any
}