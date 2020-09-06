@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.exporter.crom

import io.framed.exporter.ecore.InternalEObject
import io.framed.exporter.ecore.Set

external interface Model : InternalEObject {
    var elements: Set<ModelElement>
    var relations: Set<Relation>
}