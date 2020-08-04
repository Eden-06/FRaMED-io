@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.exporter.crom.ecore

external interface EEnum : EDataType {
    var eLiterals: OrderedSet<EEnumLiteral>
    fun getEEnumLiteral(vararg args: Any): Any
    fun getEEnumLiteral_string(name: String): EEnumLiteral
    fun getEEnumLiteral_number(value: Number): EEnumLiteral
    fun getEEnumLiteralByLiteral(literal: String): EEnumLiteral
}