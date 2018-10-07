package io.framed.framework.pictogram

class PaintStyle {
    var stroke: Color = Color.TRANSPARENT
    var strokeWidth: Int = 0

    var outlineStroke: Color = Color.TRANSPARENT
    var outlineWidth: Int = 0

    var dashArray: Array<Int> = emptyArray()
    var dashOffset: Int = 0

    var fill: Color = Color.TRANSPARENT
}

fun paintStyle(init: PaintStyle.() -> Unit) = PaintStyle().also(init)
fun ConnectionEnd.paintStyle(init: PaintStyle.() -> Unit) = PaintStyle().also(init).also {
    this.paintStyle = it
}