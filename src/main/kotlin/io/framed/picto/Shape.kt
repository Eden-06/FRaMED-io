package io.framed.picto

/**
 * @author lars
 */
abstract class Shape : Picto() {

    var left: Double? by layer
    var top: Double? by layer
    var width: Double? by layer
    var height: Double? by layer

    var style: Style = Style()
    var acceptRelation: Boolean = false;

    fun style(init: Style.() -> Unit) {
        init(style)
    }
}