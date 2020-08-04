@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.export.crom

external interface CompartmentType : RigidType {
    var parts: Any
    var relationships: Any
    var constraints: Any
    var tr_extends: CompartmentType
    var contains: Any
    var fulfillments: Any
}