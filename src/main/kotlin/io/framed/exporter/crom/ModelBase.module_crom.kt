@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.exporter.crom

import io.framed.exporter.ecore.BasicEObjectImpl
import io.framed.exporter.ecore.Set

abstract external class ModelBase : BasicEObjectImpl, Model {
    open var _elements: Set<ModelElement>
    open var _relations: Set<Relation>

companion object {
        var eStaticClass: Any
    }
}