package io.framed.framework.pictogram

/**
 * @author lars
 */
class Border(
        var style: BorderStyle = BorderStyle.NONE,
        var width: Box<Double> = box(0.0),
        var color: Box<Color> = box(Color.TRANSPARENT),
        var radius: Box<Double>? = null,
        var double: Boolean = false
) {
    enum class BorderStyle {
        SOLID, DASHED, NONE;

        override fun toString(): String = name.toLowerCase()
    }

    //fun toCss(): String = "$style ${width}px ${color.toCss()}"
}

//fun border(init: Border.() -> Unit):Border = Border().also(init)
fun Style.border(init: Border.() -> Unit): Border = Border().also(init).also { border = it }