package io.framed.picto

/**
 * @author lars
 */
class Border(
        var style: BorderStyle = BorderStyle.NONE,
        var width: Double = 0.0,
        var color: Color = Color.TRANSPARENT,
        var radius: Double = 0.0
) {
    enum class BorderStyle {
        SOLID, DASHED, NONE;

        override fun toString(): String =name.toLowerCase()
    }

    fun toCss(): String = "$style ${width}px ${color.toCss()}"
}

fun border(init: Border.() -> Unit):Border = Border().also(init)