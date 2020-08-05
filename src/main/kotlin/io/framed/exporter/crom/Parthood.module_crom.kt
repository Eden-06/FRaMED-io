@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.exporter.crom

open external class Parthood {
    open var value: Any
    open var name: Any
    open var literal: Any
    open fun getLiteral(): String
    open fun getName(): String
    open fun getValue(): Number
    override fun toString(): String

    companion object {
        var UNCONSTRAINED_VALUE: Number
        var EXCLUSIVEPART_VALUE: Number
        var SHARABLEPART_VALUE: Number
        var ESSENTIALPART_VALUE: Number
        var MANDATORYPART_VALUE: Number
        var INSEPARABLEPART_VALUE: Number
        var UNCONSTRAINED: Parthood
        var EXCLUSIVEPART: Parthood
        var SHARABLEPART: Parthood
        var ESSENTIALPART: Parthood
        var MANDATORYPART: Parthood
        var INSEPARABLEPART: Parthood
        var VALUES_ARRAY: Any
        fun get_string(literal: String): Parthood
        fun getByName(name: String): Parthood
        fun get_number(value: Number): Parthood
    }
}