@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.exporter.ecore

open external class Switch<T> {
    open fun defaultCase(eObject: EObject): T
    open var isEObject: Any
    open fun doSwitch(vararg args: Any): T
    open var doSwitch1: Any
    open var doSwitch2: Any
    open fun doSwitch3(classifierID: Number, eObject: EObject): T
    open fun isSwitchFor(ePackage: EPackage): Boolean
}