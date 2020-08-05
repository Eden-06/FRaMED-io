@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.exporter.ecore

abstract external class EEnumBase : EDataTypeImpl, EEnum {
    open var _eLiterals: Any
    override fun getEEnumLiteral(vararg args: Any): Any
    override fun getEEnumLiteral_string(name: String): EEnumLiteral
    override fun getEEnumLiteral_number(value: Number): EEnumLiteral
    override fun getEEnumLiteralByLiteral(literal: String): EEnumLiteral
    override fun eStaticClass(): EClass
    override fun eInverseAdd(otherEnd: InternalEObject, featureID: Number, msgs: NotificationChain): NotificationChain
    override fun eInverseRemove(otherEnd: InternalEObject, featureID: Number, msgs: NotificationChain): NotificationChain
    override fun eGet_number_boolean_boolean(featureID: Number, resolve: Boolean, coreType: Boolean): Any
    override fun eSet_number_any(featureID: Number, newValue: Any)

    companion object {
        var eStaticClass: EClass
    }
}