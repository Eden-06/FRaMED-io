@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.exporter.crom.crom

external interface RoleGroup : AbstractRole, RelationTarget {
    var lower: Number
    var upper: Number
    var elements: Any
}