@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.exporter.crom.crom

import io.framed.exporter.crom.ecore.BasicEObjectImpl

abstract external class RelationTargetBase : BasicEObjectImpl, RelationTarget {
    open var _name: Any
    open var _incoming: Any
    open var _outgoing: Any

companion object {
        var eStaticClass: Any
    }
}