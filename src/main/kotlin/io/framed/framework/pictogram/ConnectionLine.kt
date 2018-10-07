package io.framed.framework.pictogram

class ConnectionLine {
    var paintStyle: PaintStyle = PaintStyle()
    var type: Type = Type.RECTANGLE

    enum class Type {
        STRAIGHT, RECTANGLE
    }
}

fun Connection.line(
        type: ConnectionLine.Type = ConnectionLine.Type.STRAIGHT,
        init: PaintStyle.() -> Unit
): ConnectionLine = ConnectionLine().also {
    it.paintStyle = paintStyle(init)
    it.type = type
    this.lines += it
}