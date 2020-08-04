@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.export.crom

open external class Direction {
    open var value: Any
    open var name: Any
    open var literal: Any
    open fun getLiteral(): String
    open fun getName(): String
    open fun getValue(): Number
    override fun toString(): String

    companion object {
        var UNDIRECTED_VALUE: Number
        var FIRSTTOSECOND_VALUE: Number
        var SECONDTOFIRST_VALUE: Number
        var UNDIRECTED: Direction
        var FIRSTTOSECOND: Direction
        var SECONDTOFIRST: Direction
        var VALUES_ARRAY: Any
        fun get_string(literal: String): Direction
        fun getByName(name: String): Direction
        fun get_number(value: Number): Direction
    }
}