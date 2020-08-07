@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.exporter.crom

import io.framed.exporter.ecore.BasicEObjectImpl

abstract external class RelationBase : BasicEObjectImpl, Relation {
    override fun getTarget(): RelationTarget
    override fun getSource(): RelationTarget

companion object {
        var eStaticClass: Any
    }
}