@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.exporter.crom.crom

import io.framed.exporter.crom.ecore.NotificationChain

abstract external class IntraRelationshipConstraintBase : RelationshipConstraintImpl, IntraRelationshipConstraint {
    open var _relation: Any
    open fun basicSetRelation(newobj: Relationship, msgs: NotificationChain): NotificationChain

    companion object {
        var eStaticClass: Any
    }
}