package io.framed.framework.pictogram

import de.westermann.kobserve.basic.flatMapBinding

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

    val leftOffset: Double
        get() = left + (parent?.leftOffset(this) ?: 0.0)

    val topOffset: Double
        get() = top + (parent?.topOffset(this) ?: 0.0)

    var style: Style = Style()

    fun style(init: Style.() -> Unit) {
        init(style)
    }
}
