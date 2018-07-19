package io.framed.picto

/**
 * @author lars
 */
abstract class Shape {
    var x: Double? = null
    var y: Double? = null

    var width: Double? = null
    var height: Double? = null

    var border: Border = Border.NONE

    var background: Color = Color.TRANSPARENT
}