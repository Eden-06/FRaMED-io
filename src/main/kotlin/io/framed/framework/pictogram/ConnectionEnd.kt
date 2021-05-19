package io.framed.framework.pictogram

class ConnectionEnd {
    var foldback: Double = 1.0

    //TODO: Rethink the attributes for source and target connection ends.
    var direction: Int = 1
    var location: Double = 1.0

    var width: Int = 20
    var length: Int = 20

    var paintStyle: PaintStyle = PaintStyle()
}

fun connectionEnd(init: ConnectionEnd.() -> Unit) = ConnectionEnd().also(init)
