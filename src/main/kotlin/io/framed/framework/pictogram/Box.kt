package io.framed.framework.pictogram

class Box<T>(
        var top: T,
        var right: T,
        var bottom: T,
        var left: T
) {
    fun toCss(unit: String = ""): String = "$top$unit $right$unit $bottom$unit $left$unit"
}

fun <T> box(all: T) = Box<T>(all, all, all, all)
fun <T> box(vertical: T, horizontal: T) = Box<T>(vertical, horizontal, vertical, horizontal)
fun <T> box(top: T, right: T, bottom: T, left: T) = Box<T>(top, right, bottom, left)