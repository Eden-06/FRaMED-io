package io.framed.framework.pictogram

import de.westermann.kobserve.property.flatMapBinding
import de.westermann.kobserve.property.property
import io.framed.framework.util.Dimension
import io.framed.framework.util.Point
import io.framed.framework.util.center

/**
 * @author lars
 */
abstract class Shape(id: Long?) : Pictogram(id) {

    val leftProperty = layerProperty.flatMapBinding { it[id, this].leftProperty }
    var left by leftProperty

    val topProperty = layerProperty.flatMapBinding { it[id, this].topProperty }
    var top by topProperty

    val autosizeProperty = layerProperty.flatMapBinding { it[id, this].autosizeProperty }
    var autosize by autosizeProperty

    val widthProperty = layerProperty.flatMapBinding { it[id, this].widthProperty }
    var width by widthProperty

    val heightProperty = layerProperty.flatMapBinding { it[id, this].heightProperty }
    var height by heightProperty

    var parent: BoxShape? = null
    var resizeable = false

    val leftOffset: Double
        get() = left + (parent?.leftOffset(this) ?: 0.0)

    val topOffset: Double
        get() = top + (parent?.topOffset(this) ?: 0.0)

    var style: Style = Style()

    val visibleProperty = property(true)
    var visible by visibleProperty

    fun style(init: Style.() -> Unit) {
        init(style)
    }
}

val Shape.dimension: Dimension
    get() = Dimension(leftOffset, topOffset, width, height)

val Shape.center: Point
    get() = dimension.center
