@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.export.crom

import io.framed.export.ecore.NotificationChain

abstract external class InterRelationshipConstraintBase : RelationshipConstraintImpl, InterRelationshipConstraint {
    open var _first: Any
    open var _second: Any
    open fun basicSetSecond(newobj: Relationship, msgs: NotificationChain): NotificationChain
    open fun basicSetFirst(newobj: Relationship, msgs: NotificationChain): NotificationChain

    companion object {
        var eStaticClass: Any
    }
}