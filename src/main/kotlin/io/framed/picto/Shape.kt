package io.framed.picto

import io.framed.util.EventHandler
import io.framed.util.Point

/**
 * @author lars
 */
abstract class Shape : Picto() {

    var left: Double? by layer
    var top: Double? by layer
    var width: Double? by layer
    var height: Double? by layer

    var style: Style = Style()

    fun style(init: Style.() -> Unit) {
        init(style)
    }
}