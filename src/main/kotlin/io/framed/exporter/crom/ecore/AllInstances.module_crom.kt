@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.exporter.crom.ecore

open external class AllInstances {
    open var instances: Any
    open var subclasses: Any
    open fun put(i: EObject, classname: String)
    open fun allInstances(eclass: EClass): Array<EObject>

    companion object {
        var INSTANCE: AllInstances
    }
}