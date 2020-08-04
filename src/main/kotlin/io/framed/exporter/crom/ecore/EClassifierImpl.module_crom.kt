@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.exporter.crom.ecore

abstract external class EClassifierImpl : EClassifierBase {
    open var metaObjectID: Number
    open var setClassifierID: (id: Number) -> Unit
    open var generatedInstanceClassName: String
    open var setGeneratedInstanceClass: (isGenerated: Boolean) -> Unit
    open var basicSetInstanceTypeName: (newInstanceTypeName: String) -> Unit
    open var _instanceClass: Any
    override fun getClassifierID(): Number
    open var computeClassifierID: Any
}