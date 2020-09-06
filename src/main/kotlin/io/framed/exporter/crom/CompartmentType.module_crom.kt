@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.exporter.crom

import io.framed.exporter.ecore.OrderedSet
import io.framed.exporter.ecore.Set

external interface CompartmentType : RigidType {
    var parts: Set<Part>
    var relationships: OrderedSet<Relationship>
    var constraints: OrderedSet<Constraint>
    var tr_extends: CompartmentType
    var contains: OrderedSet<CompartmentType>
    var fulfillments: OrderedSet<Fulfillment>
}